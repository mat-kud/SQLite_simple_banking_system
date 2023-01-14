package org.example.handlers;

import org.example.entities.Account;
import org.example.exceptions.NotPositiveNumberException;
import org.example.managers.DatabaseManager;
import org.example.validators.CardNumberValidator;

import java.util.List;

public class AccountHandler {
    private final DatabaseManager databaseManager;
    private final String tableName = "cards";
    private final CardNumberValidator cardNumberValidator = new CardNumberValidator();

    public AccountHandler(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;

    }

    public void createAccount(String cardNumber, String cardPin) {
        databaseManager.createAccount(cardNumber, cardPin, tableName);
    }

    public Account getAccount(String cardNumber) {
        return databaseManager.getAccount(cardNumber, tableName);
    }

    public List<String> getAllAccountsNumbers() {
        return databaseManager.getAllAccountsNumbers(tableName);
    }

    public boolean verifyCardNumberIsUnique(String cardNumber) {
        return getAllAccountsNumbers().stream()
                .noneMatch(num -> num.equals(cardNumber));
    }

    public void deleteAccount(String cardNumber) {
        databaseManager.deleteAccount(cardNumber, tableName);
    }

    public Account addIncome(Account account, String incomeString) {
        try {
            double income = Double.parseDouble(incomeString);
            if (income <= 0) {
                throw new NotPositiveNumberException();
            } else if (isDoublePrecisionExceedingTwoDecimalPlaces(income)) {
                throw new NumberFormatException();
            }
            databaseManager.updateAccountBalance(account.getCardNumber(), income, tableName);
            account = databaseManager.getAccount(account.getCardNumber(), tableName);
            System.out.println("Income was added!");
        } catch (NumberFormatException e) {
            System.err.println("Wrong income input format.");
        } catch (NotPositiveNumberException e) {
            System.err.println("Income has to be a positive number.");
        }
        return account;
    }

    public Account makeTransfer(Account account, String receiverAccountNumber, String transferAmountString) {
        if (receiverAccountNumber.equals(account.getCardNumber())) {
            System.err.println("You can't transfer money to the same account!");
            return account;
        }
        if (!cardNumberValidator.isCardNumberValid(receiverAccountNumber)) {
            System.err.println("Probably you made a mistake in the card number. Please try again!");
            return account;
        }
        if (getAccount(receiverAccountNumber) == null) {
            System.err.println("Such a card does not exist.");
            return account;
        }

        try {
            double transferAmount = Double.parseDouble(transferAmountString);
            if (transferAmount <= 0) {
                throw new NotPositiveNumberException();
            } else if (isDoublePrecisionExceedingTwoDecimalPlaces(transferAmount)) {
                throw new NumberFormatException();
            }
            if (transferAmount > account.getBalance()) {
                System.err.println("Not enough money!");
                return account;
            }
            databaseManager.updateAccountBalance(receiverAccountNumber, transferAmount, tableName);
            databaseManager.updateAccountBalance(account.getCardNumber(), -transferAmount, tableName);
            System.out.println("Success!");
        } catch (NumberFormatException e) {
            System.err.println("Wrong transfer input format");
            return account;
        } catch (NotPositiveNumberException e) {
            System.err.println("Income has to be a positive number.");
            return account;
        }
        return getAccount(account.getCardNumber());
    }

    private boolean isDoublePrecisionExceedingTwoDecimalPlaces(Double number) {
        return String.valueOf(number).split("\\.")[1].length() > 2;
    }
}
