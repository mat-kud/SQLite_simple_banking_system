package org.example.handlers;

import org.example.entities.Account;
import org.example.exceptions.NotPositiveNumberException;
import org.example.managers.DatabaseManager;
import org.example.validators.CardNumberValidator;

import java.math.BigDecimal;
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

    public Account authenticateAccount(String cardNumber, String cardPin) {
        return databaseManager.authenticateAccount(cardNumber, cardPin, tableName);
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

    public Account deposit(Account account, String incomeString) {
        try {
            BigDecimal income = new BigDecimal(incomeString);
            if (income.compareTo(BigDecimal.ZERO) <= 0) {
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
            BigDecimal transferAmount = new BigDecimal(transferAmountString);
            if (transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new NotPositiveNumberException();
            } else if (isDoublePrecisionExceedingTwoDecimalPlaces(transferAmount)) {
                throw new NumberFormatException();
            }
            if (account.getBalance().compareTo(transferAmount) <= 0) {
                System.err.println("Not enough money!");
                return account;
            }
            databaseManager.updateAccountBalance(receiverAccountNumber, transferAmount, tableName);
            databaseManager.updateAccountBalance(account.getCardNumber(), transferAmount.negate(), tableName);
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

    private boolean isDoublePrecisionExceedingTwoDecimalPlaces(BigDecimal number) {
        return number.scale() > 2;
    }
}
