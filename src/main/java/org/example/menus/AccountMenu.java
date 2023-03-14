package org.example.menus;


import org.example.entities.Account;
import org.example.handlers.AccountHandler;
import org.example.managers.MenuManager;

public class AccountMenu extends BaseMenu implements Menu {
    private Account account;

    public AccountMenu(AccountHandler accountHandler, Account account) {
        super(accountHandler);
        this.account = account;
    }

    @Override
    public void execute() {
        while (true) {
            System.out.println("""
                    1. Balance
                    2. Add income
                    3. Do transfer
                    4. Close account
                    5. Log out
                    0. Exit
                    """);

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> System.out.println("Balance:" + account.getBalance());
                    case 2 -> account = deposit(account);
                    case 3 -> account = makeTransfer(account);
                    case 4 -> {
                        accountHandler.deleteAccount(account.getCardNumber());
                        MenuManager.executeMenu(new MainMenu(accountHandler));
                    }
                    case 5 -> {
                        System.out.println("You have successfully logged out!");
                        MenuManager.executeMenu(new MainMenu(accountHandler));
                    }
                    case 0 -> System.exit(0);
                }
            } catch (NumberFormatException e) {
                System.err.println("Wrong input format. Only following numbers are accepted: 0, 1, 2, 3, 4, 5.");
            }
        }
    }

    private Account deposit(Account account){
        System.out.println("Enter income:");
        String incomeString = scanner.nextLine();
        return accountHandler.deposit(account, incomeString);
    }

    private Account makeTransfer(Account account){
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String transferToAccountNumber = scanner.nextLine();
        System.out.println("Enter how much money you want to transfer:");
        String transferAmount = scanner.nextLine();
        return accountHandler.makeTransfer(account, transferToAccountNumber, transferAmount);
    }
}
