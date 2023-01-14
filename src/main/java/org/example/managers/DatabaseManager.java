package org.example.managers;

import org.example.entities.Account;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static org.example.utils.AccountQueries.*;


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

    public void createAccount(String cardNumber, String cardPin, String tableName) {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement =
                     connection.prepareStatement(prepareQuery.apply(insert, tableName))) {

            statement.setString(1, cardNumber);
            statement.setString(2, cardPin);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error while adding Account parameters" + e.getMessage());
        }
    }

    public Account getAccount(String number, String tableName) {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement =
                     connection.prepareStatement(prepareQuery.apply(get_account_by_number, tableName))) {

            statement.setString(1, number);

            try (final ResultSet res = statement.executeQuery()) {
                if (res.next()) {
                    final int id = res.getInt("id");
                    final String pin = res.getString("pin");
                    final double balance = res.getDouble("balance");
                    return new Account(id, number, pin, balance);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading from file");
            throw new RuntimeException("Error while finding element" + e.getMessage());
        }
    }

    public List<String> getAllAccountsNumbers(String tableName){
        List<String> cardNumbers = new ArrayList<>();
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement =
                     connection.prepareStatement(prepareQuery.apply(get_accounts_by_number, tableName))) {

            try (final ResultSet res = statement.executeQuery()) {
                while (res.next()) {
                    String cardNumber = res.getString("number");
                    cardNumbers.add(cardNumber);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cardNumbers;
    }

    public void updateAccountBalance(String accNumber, double amount, String tableName){
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement =
                     connection.prepareStatement(prepareQuery.apply(update_account_balance, tableName))) {

            statement.setDouble(1, amount);
            statement.setString(2, accNumber);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error while updating balance" + e.getMessage());
        }
    }

    public void deleteAccount(String accNumber, String tableName){
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement =
                     connection.prepareStatement(prepareQuery.apply(delete_account_by_number, tableName))) {

            statement.setString(1, accNumber);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error while deleting account" + e.getMessage());
        }
    }
}
