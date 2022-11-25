package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.example.Utils.*;


public class AccountHandler {
    private final CardNumberGenerator cardNumberGenerator = new CardNumberGenerator();
    private final PinGenerator pinGenerator = new PinGenerator();
    private final DatabaseManager databaseManager;


    public AccountHandler(DatabaseManager databaseManager){
        this.databaseManager = databaseManager;

    }

    public String[] createAccount(){
        while (true) {
            String cardNumber = cardNumberGenerator.generateCardNumber();
            String pin = pinGenerator.generatePinNumber();

            if (verifyCardNumberIsUnique(cardNumber)) {
                addAccount(cardNumber, pin);
                return new String[]{cardNumber, pin};
            }
        }
    }

    private boolean verifyCardNumberIsUnique(String cardNumber){
        return getAllAccountsNumbers().stream()
                .noneMatch(num -> num.equals(cardNumber));
    }

    private void addAccount(String cardNumber, String cardPin) {
        try (final Connection connection = databaseManager.getDataSource().getConnection();
             final PreparedStatement statement = connection.prepareStatement(insert)) {

            statement.setString(1, cardNumber);
            statement.setString(2, cardPin);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while adding Account parameters" + e.getMessage());
        }
    }

    public Account getAccount(String number) {
        try (final Connection connection = databaseManager.getDataSource().getConnection();
             final PreparedStatement statement = connection.prepareStatement(get_account_by_number)) {

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
            System.out.println("Error reading from file");
            throw new RuntimeException("Error while finding element" + e.getMessage());
        }
    }

    public List<String> getAllAccountsNumbers(){
        List<String> cardNumbers = new ArrayList<>();
        try (final Connection connection = databaseManager.getDataSource().getConnection();
             final PreparedStatement statement = connection.prepareStatement(get_accounts_by_number)) {

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
             final PreparedStatement statement = connection.prepareStatement(update_account_balance)) {

            statement.setInt(1, amount);
            statement.setString(2, accNumber);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while updating amount" + e.getMessage());
        }
    }

    public void deleteAccount(String accNumber){
        try (final Connection connection = databaseManager.getDataSource().getConnection();
             final PreparedStatement statement = connection.prepareStatement(delete_account_by_number)) {

            statement.setString(1, accNumber);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while deleting account" + e.getMessage());
        }
    }
}
