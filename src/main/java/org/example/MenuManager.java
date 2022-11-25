package org.example;

import org.example.menus.*;

public class MenuManager {
    private static AccountHandler accountHandler;

    public static void setAccountHandler(AccountHandler accountHandler) {
        MenuManager.accountHandler = accountHandler;
    }

    public static void runAccountMenu(Account account) {
        new AccountMenu(accountHandler, account).execute();
    }

    public static void runCreateAccountMenu() {
        new CreateAccountMenu(accountHandler).execute();
    }

    public static void runMainMenu() {
        new MainMenu(accountHandler).execute();
    }

    public static void runLogInMenu() {
        new LogInMenu(accountHandler).execute();
    }
}
