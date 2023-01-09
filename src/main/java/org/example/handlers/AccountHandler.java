package org.example.handlers;

import org.example.entities.Account;
import org.example.managers.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static org.example.utils.Queries.*;

public class AccountHandler {
    private final DatabaseManager databaseManager;
    private final String tableName = "cards";
    private final BiFunction<String, String, String> prepareQuery = String::format;


    public AccountHandler(DatabaseManager databaseManager){
        this.databaseManager = databaseManager;

    }

    public void createAccount(String cardNumber, String cardPin) {
        try (final Connection connection = databaseManager.getDataSource().getConnection();
             final PreparedStatement statement =
                     connection.prepareStatement(prepareQuery.apply(insert, tableName))) {

            statement.setString(1, cardNumber);
            statement.setString(2, cardPin);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error while adding Account parameters" + e.getMessage());
        }
    }

    public Account getAccount(String number) {
        try (final Connection connection = databaseManager.getDataSource().getConnection();
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

    public List<String> getAllAccountsNumbers(){
        List<String> cardNumbers = new ArrayList<>();
        try (final Connection connection = databaseManager.getDataSource().getConnection();
             final PreparedStatement statement =
                     connection.prepareStatement(prepareQuery.apply(get_accounts_by_number, tableName))) {

            try (final ResultSet res = statement.executeQuery()) {
                if (res.next()) {
                    String cardNumber = res.getString("number");
                    cardNumbers.add(cardNumber);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cardNumbers;
    }

    public void updateAccountBalance(String accNumber, int amount){
        try (final Connection connection = databaseManager.getDataSource().getConnection();
             final PreparedStatement statement =
                     connection.prepareStatement(prepareQuery.apply(update_account_balance, tableName))) {

            statement.setInt(1, amount);
            statement.setString(2, accNumber);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error while updating amount" + e.getMessage());
        }
    }

    public void deleteAccount(String accNumber){
        try (final Connection connection = databaseManager.getDataSource().getConnection();
             final PreparedStatement statement =
                     connection.prepareStatement(prepareQuery.apply(delete_account_by_number, tableName))) {

            statement.setString(1, accNumber);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error while deleting account" + e.getMessage());
        }
    }
}
