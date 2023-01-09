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

        Account account = accountHandler.getAccount(cardNumber);

        if (account != null && account.verifyCardNumber(cardNumber)
                && account.verifyCardPin(cardPin)) {
            System.out.println("You have successfully logged in!");
            MenuManager.runAccountMenu(account);
        } else {
            System.out.println("Wrong card number or PIN!");
        }
    }
}
