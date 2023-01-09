package org.example.managers;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.BiFunction;

import static org.example.utils.Queries.create_table;
import static org.example.utils.Queries.drop_table;


public class DatabaseManager {
    private final String dbName;
    private final BiFunction<String, String, String> prepareQuery = String::format;
    private static DatabaseManager instanceOfDatabaseManager = null;
    private final SQLiteDataSource dataSource = new SQLiteDataSource();

    private DatabaseManager(String dbName, String dataPath) {
        this.dbName = dbName;
        dataSource.setUrl("jdbc:sqlite:" + dataPath + dbName);
    }

    public static DatabaseManager getInstanceOfDatabaseManager(String dbName, String dataPath) {
        if (instanceOfDatabaseManager == null) {
            instanceOfDatabaseManager = new DatabaseManager(dbName, dataPath);
        }
        return instanceOfDatabaseManager;
    }

    public void dropTable(String tableName) {
        try (final Connection dataSourceConnection = dataSource.getConnection();
             final Statement statement = dataSourceConnection.createStatement()) {

            statement.executeUpdate(prepareQuery.apply(drop_table, tableName));

        } catch (SQLException e) {
            System.out.println("Error while dropping table " + e.getMessage());
        }
    }

    public void createTable(String tableName) {
        try (final Connection dataSourceConnection = dataSource.getConnection();
             final Statement statement = dataSourceConnection.createStatement()) {

            statement.executeUpdate(prepareQuery.apply(create_table, tableName));

        } catch (SQLException e) {
            System.out.println("Error while creating table " + e.getMessage());
        }
    }

    public SQLiteDataSource getDataSource(){
        return dataSource;
    }
}
