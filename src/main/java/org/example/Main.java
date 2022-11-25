package org.example;

public class Main {

    public static void main(String[] args) {
        if (args.length > 1 && args[0].equals("-fileName") && args[1] != null) {
            DatabaseManager databaseManager = DatabaseManager.getInstanceOfDatabaseManager(args[1]);
            AccountHandler accountHandler = new AccountHandler(databaseManager);
            MenuManager.setAccountHandler(accountHandler);
            MenuManager.runMainMenu();
        } else {
            System.out.println("No Database found.");
        }
    }
}