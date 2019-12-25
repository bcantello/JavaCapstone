package com.payjunction.flytrap;

class TransactionInfo
{
	private int transactionID;
	private String transactionAmount;
	private String transactionApproval;
	private String action;

	public int getTransactionID()
	{
		return transactionID;
	}

	public void setTransactionID(int transactionID)
	{
		this.transactionID = transactionID;
	}

	public String getTransactionAmount()
	{
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount)
	{
		this.transactionAmount = transactionAmount;
	}

	public String getTransactionApproval()
	{
		return transactionApproval;
	}

	public void setTransactionApproval(String transactionApproval)
	{
		this.transactionApproval = transactionApproval;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public void setStatus(String status)
	{
	}
}
