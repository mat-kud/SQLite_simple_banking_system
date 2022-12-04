package org.example;

public class Main {

    public static void main(String[] args) {
        DatabaseManager databaseManager = DatabaseManager.getInstanceOfDatabaseManager();
        AccountHandler accountHandler = new AccountHandler(databaseManager);
        MenuManager.setAccountHandler(accountHandler);
        MenuManager.runMainMenu();

    }
}