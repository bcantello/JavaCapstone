package com.payjunction.flytrap;

import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class POS
{
	private final TransactionInfo transactionInfo = new TransactionInfo();
	private static final Scanner scanner = new Scanner(System.in);
	private final TransactionDatabase transactionDatabase = new TransactionDatabase();
	private final HttpClientFactory clientFactory = new HttpClientFactory();
	private final CoreTerminalInfo setup = new CoreTerminalInfo();
	private final TerminalResponse terminalResponse = new TerminalResponse();
	private final PaymentInformation paymentInformation = new PaymentInformation();
	private final ApiService api =
			new ApiService(new ConnectionClient(clientFactory, setup), setup, terminalResponse);
	private final TransactionIdParser idParser =
			new TransactionIdParser(terminalResponse, api);
	private final ApprovalResponseService approvalResponseService =
			new ApprovalResponseService(new ConnectionClient(clientFactory, setup));


	POS() throws IOException
	{
	}

	public void menu()
	{
		transactionDatabase.initializeDatabase();

		MenuOption quit = new QuitMenu();
		MenuOption help = new HelpMenu();
		MenuOption cash = new CashMenu(transactionInfo, scanner);
		MenuOption charge = new ChargeMenu(scanner, paymentInformation, api,
				idParser, approvalResponseService, new ConnectionClient(clientFactory, setup));
		MenuOption refund = new RefundMenu(scanner, paymentInformation, api,
				idParser, approvalResponseService);
		MenuOption cashRefund = new CashRefundMenu(transactionInfo, scanner);
		MenuOption voidTransaction = new VoidMenu(paymentInformation, api);
		MenuOption cashVoid = new CashVoidMenu();
		MenuOption list = new ListMenu();
		MenuOption invalidMenu = new InvalidMenu();

		List<MenuOption> menuOptions = ImmutableList.of(quit, help, cash, charge, refund,
				cashRefund, voidTransaction, cashVoid, list);

		MenuOption currentMenuOpton = help;
		while (!currentMenuOpton.shouldExit())
		{
			System.out.println(currentMenuOpton.getHelpText());
			currentMenuOpton.go();

			System.out.print("Available options ");
			String options = menuOptions.stream().map(MenuOption::getMenuInput)
					.collect(Collectors.joining(", "));
			System.out.println(options);

			final String merchantChoice = scanner.nextLine().toUpperCase();
			currentMenuOpton = menuOptions
					.stream()
					.filter(option->option.getMenuInput().equals(merchantChoice))
					.findFirst()
					.orElse(invalidMenu);
		}
	}
}
