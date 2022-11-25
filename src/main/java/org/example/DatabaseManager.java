package org.example;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.example.Utils.create_table;


public class DatabaseManager {
    private static DatabaseManager instanceOfDatabaseManager = null;
    private final SQLiteDataSource dataSource = new SQLiteDataSource();

    private DatabaseManager(String dbName) {
        dataSource.setUrl("jdbc:sqlite:" + dbName);
        init();
    }

    public static DatabaseManager getInstanceOfDatabaseManager(String dbName) {
        if (instanceOfDatabaseManager == null) {
            instanceOfDatabaseManager = new DatabaseManager(dbName);
        }
        return instanceOfDatabaseManager;
    }

    private void init() {
        try (final Connection dataSourceConnection = dataSource.getConnection();
             final Statement statement = dataSourceConnection.createStatement()) {

            //statement.executeUpdate(delete_table);
            statement.executeUpdate(create_table);

        } catch (SQLException e) {
            System.out.println("Error while creating table " + e.getMessage());
        }
    }

    public SQLiteDataSource getDataSource(){
        return dataSource;
    }
}
