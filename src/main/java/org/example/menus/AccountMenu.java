package org.example.menus;


import org.example.entities.Account;
import org.example.handlers.AccountHandler;
import org.example.validators.CardNumberValidator;
import org.example.managers.MenuManager;

public class AccountMenu extends BaseMenu implements Menu {
    private final CardNumberValidator cardNumberValidator = new CardNumberValidator();
    private Account account;

    public AccountMenu(AccountHandler accountHandler, Account account) {
        super(accountHandler);
        this.account = account;
    }

    @Override
    public void execute() {
        while(true) {
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
                    case 2 -> account = addIncome(account);
                    case 3 -> account = makeTransfer(account);
                    case 4 -> {
                        deleteAccount(account);
                        MenuManager.executeMenu(new MainMenu(accountHandler));
                    }
                    case 5 -> {
                        System.out.println("You have successfully logged out!");
                        MenuManager.executeMenu(new MainMenu(accountHandler));
                    }
                    case 0 -> System.exit(0);
                }
            } catch (NumberFormatException e){
                System.err.println("Wrong input format. Only following numbers are accepted: 0, 1, 2, 3, 4, 5.");
            }
        }
    }

    private Account addIncome(Account account){
        try {
            System.out.println("Enter income:");
            int income = Integer.parseInt(scanner.nextLine());
            accountHandler.updateAccountBalance(account.getCardNumber(), income);
            account = accountHandler.getAccount(account.getCardNumber());
            System.out.println("Income was added!");
        } catch (NumberFormatException e){
            System.err.println("Wrong income input format.");
        }
        return account;
    }

    private Account makeTransfer(Account account){
        System.out.println("Transfer");
        System.out.println("Enter card number:");

        String transferToAccountNumber = scanner.nextLine();

        if(transferToAccountNumber.equals(account.getCardNumber())){
            System.err.println("You can't transfer money to the same account!");
            return account;
        }
        if(!cardNumberValidator.isCardNumberValid(transferToAccountNumber)){
            System.err.println("Probably you made a mistake in the card number. Please try again!");
            return account;
        }
        if(accountHandler.getAccount(transferToAccountNumber) == null){
            System.err.println("Such a card does not exist.");
            return account;
        }

        try {
            System.out.println("Enter how much money you want to transfer:");
            int transferAmount = Integer.parseInt(scanner.nextLine());

            if (transferAmount > account.getBalance()) {
                System.err.println("Not enough money!");
                return account;
            }
            accountHandler.updateAccountBalance(transferToAccountNumber, transferAmount);
            accountHandler.updateAccountBalance(account.getCardNumber(), -transferAmount);
            System.out.println("Success!");
        } catch (NumberFormatException e){
            System.err.println("Wrong transfer input format");
            return account;
        }
        return accountHandler.getAccount(account.getCardNumber());
    }

    private void deleteAccount(Account account){
        accountHandler.deleteAccount(account.getCardNumber());
        System.out.println("The account has been closed!");
    }
}
