package com.payjunction.flytrap;

import com.google.common.util.concurrent.AtomicDouble;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class TransactionDatabase
{
	public TransactionDatabase()
	{
	}

	public void initializeDatabase()
	{
		try (Connection dbConnection = getConnection();
				Statement statement = dbConnection.createStatement())
		{
			statement.execute("CREATE TABLE IF NOT EXISTS transactions "
					+ "(transactionID INTEGER, amount TEXT, approval TEXT, charge TEXT)");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	public void updateList(int userInput) throws SQLException
	{
		String sql =
				"UPDATE transactions SET charge = 'VOIDED' WHERE ROWID=" + userInput;
		try (Connection dbConnection = getConnection();
				PreparedStatement pstmt = dbConnection.prepareStatement(sql))
		{
			pstmt.executeUpdate();
		}
		voidPrintList(userInput);
	}

	public long appendDatabase(TransactionInfo transactionInfo)
	{
		String SQL = "INSERT INTO transactions (transactionID, amount, approval, charge) "
				+ "VALUES (?, ?, ?, ?)";

		long id = 0;
		try (Connection dbConnection = getConnection();
				PreparedStatement pstmt = dbConnection.prepareStatement(SQL,
						Statement.RETURN_GENERATED_KEYS))
		{
			pstmt.setInt(1, transactionInfo.getTransactionID());
			pstmt.setString(2, transactionInfo.getTransactionAmount());
			pstmt.setString(3, transactionInfo.getTransactionApproval());
			pstmt.setString(4, transactionInfo.getAction());

			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0)
			{
				try (ResultSet rs = pstmt.getGeneratedKeys())
				{
					if (rs.next())
					{
						id = rs.getLong(1);
					}
				}
				catch (SQLException ex)
				{
					System.out.println(ex.getMessage());
				}
			}
		}
		catch (SQLException ex)
		{
			System.out.println(ex.getMessage());
		}
		return id;
	}

	public void listAllTransactions()
	{
		String sql = "SELECT ROWID, amount, approval, charge FROM transactions";

		query(sql, rs->{
			String row = String.format(
					"%8s %12s %12s %12s\n", rs.getString("ROWID") + "\t|",
					rs.getDouble("amount") + "\t|",
					rs.getString("approval") + "\t|", rs.getString("charge"));

			System.out.println(row);
		});

		System.out.println("\n");
		printApprovedTotal();
		printRefundedTotal();
		double net = printTotal("CHARGE") - printTotal("REFUND");
		System.out.println("The net total for today is: " + net);
		System.out.println("\n");

	}

	private void voidPrintList(int userInput)
	{
		String sql = "SELECT ROWID, amount, approval, charge FROM transactions "
				+ "WHERE ROWID=" + userInput;

		query(sql, rs->{
			String row = String.format(
					"%8s %12s %12s %12s\n", rs.getString("ROWID") + "\t|",
					rs.getDouble("amount") + "\t|",
					rs.getString("approval") + "\t|", rs.getString("charge"));

			System.out.println(row);
		});
	}

	private void printApprovedTotal()
	{
		System.out.println("Total amount 'Approved' = " + printTotal("CHARGE"));
	}

	private void printRefundedTotal()
	{
		System.out.println("Total amount 'Refunded' = " + printTotal("REFUND"));
	}

	private double printTotal(String chargeType)
	{
		AtomicDouble sum = new AtomicDouble(0);
		String sql = "SELECT amount FROM transactions where charge='"+chargeType+"'";
		query(sql, res->{
			double c = res.getDouble(1);
			sum.addAndGet(c);
		});

		return sum.get();
	}

	boolean searchTransactionList(int userInput)
	{
		AtomicBoolean idPresent = new AtomicBoolean(false);
		String sql =
				"SELECT COUNT(1) transactionID FROM transactions WHERE ROWID=" + userInput;

		query(sql, res->{
			idPresent.set(true);
		});
		return idPresent.get();
	}

	boolean voidTransactionIsCredit(int userInput)
	{
		AtomicBoolean isCredit = new AtomicBoolean(true);
		String sql =
				"SELECT approval FROM transactions WHERE ROWID=" + userInput;

		query(sql, res->{
			String cash = res.getString(1);
			if (cash.equals("CASH"))
			{
				isCredit.set(false);
			}
		});
		return isCredit.get();
	}

	boolean voidTransactionIsDeclined(int userInput)
	{
		AtomicBoolean isCHARGE = new AtomicBoolean(false);
		String sql =
				"SELECT approval FROM transactions WHERE ROWID=" + userInput;

		query(sql, res->{
			String charge = res.getString(1);
			if (charge.equals("Declined"))
			{
				isCHARGE.set(true);
			}
		});
		return isCHARGE.get();
	}

	int getTransactionID(int userInput)
	{
		AtomicInteger idPresent = new AtomicInteger(userInput);
		String sql =
				"SELECT transactionID FROM transactions WHERE ROWID=" + userInput;

		query(sql, res->{
			int txnID = res.getInt(1);
			idPresent.set(txnID);
		});
		return idPresent.get();
	}

	boolean transactionIdInRange(int userInput)
	{
		AtomicBoolean ROWID = new AtomicBoolean(false);
		String sql =
				"SELECT MAX(ROWID) FROM transactions";

		query(sql, res->{
			int idMax = res.getInt(1);
			if (userInput <= idMax)
			{
				ROWID.set(true);
			}
		});
		return ROWID.get();
	}

	private void query(String query, ThrowingConsumer<ResultSet> forEachResult)
	{
		try (Connection dbConnection = getConnection();
				Statement stmt  = dbConnection.createStatement();
				ResultSet res    = stmt.executeQuery(query))
		{
			while (res.next())
			{
				forEachResult.accept(res);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	private Connection getConnection() throws SQLException
	{
		return DriverManager.getConnection("jdbc:sqlite:flytrapDaily.db");
	}

	@FunctionalInterface
	interface ThrowingConsumer<T>
	{
		void accept(T t) throws SQLException;
	}
}

