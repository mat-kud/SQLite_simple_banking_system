package org.example;

import org.example.entities.Account;
import org.example.handlers.AccountHandler;
import org.example.managers.DatabaseManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class AccountHandlerTest {
    private final SoftAssert softAssert = new SoftAssert();
    private final String cardsTableName = "cards";
    private final String dataPath = "src/test/java/org/example/data/";
    private final String dbName = "TEST_CARDS";
    private final DatabaseManager databaseManager = DatabaseManager
            .getInstanceOfDatabaseManager(dbName, dataPath);
    private final AccountHandler accountHandler = new AccountHandler(databaseManager);

    @BeforeMethod
    public void setUp(){
        databaseManager.dropTable(cardsTableName);
        databaseManager.createTable(cardsTableName);
    }

    @Test
    public void verifyAccountsAreCreatedWithValidNumbers()  {
        String validNumber1 = "4000004793834684";
        String validNumber2 = "4000000000000002";

        accountHandler.createAccount(validNumber1, "9999");
        accountHandler.createAccount(validNumber2, "9607");
        List<String> numbers = accountHandler.getAllAccountsNumbers();

        softAssert.assertTrue(numbers.contains(validNumber1), "Account 1 was not created.");
        softAssert.assertTrue(numbers.contains(validNumber2), "Account 2 was not created.");
        softAssert.assertTrue(numbers.size() == 2,
                "There should be two accounts in the database, but found: " + numbers.size());
        softAssert.assertAll();
    }

    @Test
    public void verifyIncomeIsAddedToValidAccount(){
        String validNumber = "4000004793834684";
        String income = "210.1";
        Account account = createAccountWithValidNumber(validNumber);

        accountHandler.createAccount(validNumber, "9999");
        accountHandler.addIncome(account, income);
        accountHandler.addIncome(account, income);

        Assert.assertEquals(Double.parseDouble(income)*2, accountHandler.getAccount(validNumber).getBalance(),
                "Income was not properly added to account's balance");
    }

    @Test
    public void verifyAccountBalanceIsNotAffectedWhenAddingNegativeIncome(){
        String validNumber = "4000004793834684";
        String income = "-210.1";
        Account account = createAccountWithValidNumber(validNumber);

        accountHandler.createAccount(validNumber, "9999");
        accountHandler.addIncome(account, income);

        Assert.assertEquals(accountHandler.getAccount(validNumber).getBalance(), 0,
                "Negative income affected account's balance");
    }

    @Test
    public void verifyAccountBalanceIsNotAffectedWhenAddingIncomeAsNotADoubleNumber(){
        String validNumber = "4000004793834684";
        String income = "d456.45";
        Account account = createAccountWithValidNumber(validNumber);

        accountHandler.createAccount(validNumber, "9999");
        accountHandler.addIncome(account, income);

        Assert.assertEquals(accountHandler.getAccount(validNumber).getBalance(), 0,
                "Not a double income affected account's balance");
    }

    private Account createAccountWithValidNumber(String cardNumber){
        return new Account(cardNumber, "9665");
    }

    @Test
    public void verifyAccountBalanceIsNotAffectedWhenIncomeWithPrecisionThreeDecimals(){
        String validNumber = "4000004793834684";
        String income = "230.456";
        Account account = createAccountWithValidNumber(validNumber);

        accountHandler.createAccount(validNumber, "9999");
        accountHandler.addIncome(account, income);

        Assert.assertEquals(accountHandler.getAccount(validNumber).getBalance(), 0,
                "Double with precision to three decimal places affected account's balance");
    }

    @Test
    public void verifyMoneyIsSentFromOneAccountToAnotherOne(){
        String validNumberSender = "4000004793834684";
        String validNumberReceiver = "4000000000000002";
        String income = "245.5";
        String transferSum = "100";

        accountHandler.createAccount(validNumberSender, "9999");
        accountHandler.createAccount(validNumberReceiver, "9999");
        accountHandler.addIncome(accountHandler.getAccount(validNumberSender), income);
        accountHandler.makeTransfer(accountHandler.getAccount(validNumberSender), validNumberReceiver, transferSum);

        softAssert.assertEquals(accountHandler.getAccount(validNumberReceiver).getBalance(), Double.parseDouble(transferSum),
                "Receiver's balance is incorrect");
        softAssert.assertEquals(accountHandler.getAccount(validNumberSender).getBalance(),
                Double.parseDouble(income) - Double.parseDouble(transferSum),
                "Sender's balance is incorrect");
        softAssert.assertAll();
    }

    @Test
    public void verifyMoneyIsNotSentWhenTransferSumIsNegative(){
        String validNumberSender = "4000004793834684";
        String validNumberReceiver = "4000000000000002";
        String income = "245.5";
        String transferSum = "-100";

        accountHandler.createAccount(validNumberSender, "9999");
        accountHandler.createAccount(validNumberReceiver, "9999");
        accountHandler.addIncome(accountHandler.getAccount(validNumberSender), income);
        accountHandler.makeTransfer(accountHandler.getAccount(validNumberSender), validNumberReceiver, transferSum);

        softAssert.assertEquals(accountHandler.getAccount(validNumberReceiver).getBalance(), 0.0,
                "Receiver's balance is incorrect");
        softAssert.assertEquals(accountHandler.getAccount(validNumberSender).getBalance(),
                Double.parseDouble(income),
                "Sender's balance is incorrect");
        softAssert.assertAll();
    }

    @Test
    public void verifyMoneyIsNotSentWhenSendingToSelfAccount(){
        String validNumber = "4000004793834684";
        String income = "245.5";
        String transferSum = "100";

        accountHandler.createAccount(validNumber, "9999");
        accountHandler.addIncome(accountHandler.getAccount(validNumber), income);
        accountHandler.makeTransfer(accountHandler.getAccount(validNumber), validNumber, transferSum);

        Assert.assertEquals(accountHandler.getAccount(validNumber).getBalance(), Double.parseDouble(income),
                "Account's balance was affected");
    }

    @Test
    public void verifyMoneyIsNotSentWhenTransferSumExceedingBalance(){
        String validNumberSender = "4000004793834684";
        String validNumberReceiver = "4000000000000002";
        String income = "245.5";
        String transferSum = "250";

        accountHandler.createAccount(validNumberSender, "9999");
        accountHandler.createAccount(validNumberReceiver, "9999");
        accountHandler.addIncome(accountHandler.getAccount(validNumberSender), income);
        accountHandler.makeTransfer(accountHandler.getAccount(validNumberSender), validNumberReceiver, transferSum);

        softAssert.assertEquals(accountHandler.getAccount(validNumberReceiver).getBalance(), 0.0,
                "Receiver's balance is incorrect");
        softAssert.assertEquals(accountHandler.getAccount(validNumberSender).getBalance(),
                Double.parseDouble(income),
                "Sender's balance is incorrect");
        softAssert.assertAll();
    }

    @Test
    public void verifyMoneyIsNotSentWhenSendingToNotExistingAccount(){
        String validNumberSender = "4000004793834684";
        String validNumberReceiver = "4000000000000002";
        String income = "245.5";
        String transferSum = "200";

        accountHandler.createAccount(validNumberSender, "9999");
        accountHandler.addIncome(accountHandler.getAccount(validNumberSender), income);
        accountHandler.makeTransfer(accountHandler.getAccount(validNumberSender), validNumberReceiver, transferSum);

        softAssert.assertEquals(accountHandler.getAccount(validNumberSender).getBalance(),
                Double.parseDouble(income),
                "Sender's balance is incorrect");
        softAssert.assertNull(accountHandler.getAccount(validNumberReceiver),
                "Account should not be present");
        softAssert.assertAll();
    }

    @Test
    public void verifyAccountIsDeleted(){
        String validNumber = "4000004793834684";

        accountHandler.createAccount(validNumber, "9999");
        accountHandler.deleteAccount("4000004793834684");

        Assert.assertNull(accountHandler.getAccount(validNumber), "Account is not deleted.");
    }

    @Test
    public void verifyMoneyIsNotSentToPreviouslyDeletedAccount(){
        String validNumberSender = "4000004793834684";
        String validNumberReceiver = "4000000000000002";
        String income = "245.5";
        String transferSum = "200";

        accountHandler.createAccount(validNumberSender, "9999");
        accountHandler.createAccount(validNumberReceiver, "9999");
        accountHandler.addIncome(accountHandler.getAccount(validNumberSender), income);
        accountHandler.deleteAccount(validNumberReceiver);
        accountHandler.makeTransfer(accountHandler.getAccount(validNumberSender), validNumberReceiver, transferSum);

        softAssert.assertEquals(accountHandler.getAccount(validNumberSender).getBalance(),
                Double.parseDouble(income),
                "Sender's balance is incorrect");
        softAssert.assertNull(accountHandler.getAccount(validNumberReceiver),
                "Account should not be present");
        softAssert.assertAll();
    }
}
