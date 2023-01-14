package org.example;

import org.example.handlers.AccountHandler;
import org.example.managers.DatabaseManager;
import org.example.managers.MenuManager;
import org.example.menus.MainMenu;

public class Main {

    public static void main(String[] args) {
        final String dbName = "CARDS";
        final String dataPath = "src/main/java/org/example/data/";
        final String tableName = "cards";
        DatabaseManager databaseManager = DatabaseManager
                .getInstanceOfDatabaseManager(dbName, dataPath);
        databaseManager.createTable(tableName);
        AccountHandler accountHandler = new AccountHandler(databaseManager);
        MenuManager.executeMenu(new MainMenu(accountHandler));
    }
}