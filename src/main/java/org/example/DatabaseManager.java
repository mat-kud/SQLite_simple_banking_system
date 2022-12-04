package org.example;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.example.Utils.create_table;


public class DatabaseManager {
    private final String dbName = "CARDS";
    private static DatabaseManager instanceOfDatabaseManager = null;
    private final SQLiteDataSource dataSource = new SQLiteDataSource();

    private DatabaseManager() {
        dataSource.setUrl("jdbc:sqlite:" + dbName);
        init();
    }

    public static DatabaseManager getInstanceOfDatabaseManager() {
        if (instanceOfDatabaseManager == null) {
            instanceOfDatabaseManager = new DatabaseManager();
        }
        return instanceOfDatabaseManager;
    }

    private void init() {
        try (final Connection dataSourceConnection = dataSource.getConnection();
             final Statement statement = dataSourceConnection.createStatement()) {

            statement.executeUpdate(create_table);

        } catch (SQLException e) {
            System.out.println("Error while creating table " + e.getMessage());
        }
    }

    public SQLiteDataSource getDataSource(){
        return dataSource;
    }
}
