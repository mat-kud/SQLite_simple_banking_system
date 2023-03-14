package org.example.menus;


import org.example.entities.Account;
import org.example.handlers.AccountHandler;
import org.example.managers.MenuManager;

public class LogInMenu extends BaseMenu implements Menu {

    public LogInMenu(AccountHandler accountHandler) {
        super(accountHandler);
    }

    @Override
    public void execute() {
        System.out.println("Enter your card number:");
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String cardPin = scanner.nextLine();

        Account account = accountHandler.authenticateAccount(cardNumber, cardPin);

        if (account != null && account.getCardNumber().equals(cardNumber)) {
            System.out.println("You have successfully logged in!");
            MenuManager.executeMenu(new AccountMenu(accountHandler, account));
        } else {
            System.err.println("Wrong card number or PIN!");
        }
    }
}
