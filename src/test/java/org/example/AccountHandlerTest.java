package org.example;

import org.example.entities.Account;
import org.example.handlers.AccountHandler;
import org.example.managers.DatabaseManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.math.BigDecimal;
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
    public void verify_accounts_are_created_with_valid_numbers()  {
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
    public void verify_income_is_added_to_valid_account(){
        String validNumber = "4000004793834684";
        String income = "210.1";
        Account account = createAccountWithValidNumber(validNumber);

        accountHandler.createAccount(validNumber, "9999");
        accountHandler.deposit(account, income);
        accountHandler.deposit(account, income);

        Assert.assertEquals(accountHandler.getAccount(validNumber).getBalance(),
                new BigDecimal(income).multiply(BigDecimal.valueOf(2)),
                "Income was not properly added to account's balance");
    }

    @Test
    public void verify_account_balance_is_not_affected_when_adding_negative_income(){
        String validNumber = "4000004793834684";
        String income = "-210.1";
        Account account = createAccountWithValidNumber(validNumber);

        accountHandler.createAccount(validNumber, "9999");
        accountHandler.deposit(account, income);

        Assert.assertEquals(accountHandler.getAccount(validNumber).getBalance(), BigDecimal.valueOf(0.0),
                "Negative income affected account's balance");
    }

    @Test
    public void verify_account_balance_is_not_affected_when_adding_income_as_not_a_double_number(){
        String validNumber = "4000004793834684";
        String income = "d456.45";
        Account account = createAccountWithValidNumber(validNumber);

        accountHandler.createAccount(validNumber, "9999");
        accountHandler.deposit(account, income);

        Assert.assertEquals(accountHandler.getAccount(validNumber).getBalance(), BigDecimal.valueOf(0.0),
                "Not a double income affected account's balance");
    }

    @Test
    public void verify_account_balance_is_not_affected_when_income_with_precision_three_decimals(){
        String validNumber = "4000004793834684";
        String income = "230.456";
        Account account = createAccountWithValidNumber(validNumber);

        accountHandler.createAccount(validNumber, "9999");
        accountHandler.deposit(account, income);

        Assert.assertEquals(accountHandler.getAccount(validNumber).getBalance(), BigDecimal.valueOf(0.0),
                "Double with precision to three decimal places affected account's balance");
    }

    @Test
    public void verify_money_is_sent_from_one_account_to_another_one(){
        String validNumberSender = "4000004793834684";
        String validNumberReceiver = "4000000000000002";
        String income = "245.5";
        String transferSum = "100.0";

        accountHandler.createAccount(validNumberSender, "9999");
        accountHandler.createAccount(validNumberReceiver, "9999");
        accountHandler.deposit(accountHandler.getAccount(validNumberSender), income);
        accountHandler.makeTransfer(accountHandler.getAccount(validNumberSender), validNumberReceiver, transferSum);

        softAssert.assertEquals(accountHandler.getAccount(validNumberReceiver).getBalance(),
                new BigDecimal(transferSum),
                "Receiver's balance is incorrect");
        softAssert.assertEquals(accountHandler.getAccount(validNumberSender).getBalance(),
                new BigDecimal(income).subtract(new BigDecimal(transferSum)),
                "Sender's balance is incorrect");
        softAssert.assertAll();
    }

    @Test
    public void verify_money_is_not_sent_when_transfer_is_negative(){
        String validNumberSender = "4000004793834684";
        String validNumberReceiver = "4000000000000002";
        String income = "245.5";
        String transferSum = "-100";

        accountHandler.createAccount(validNumberSender, "9999");
        accountHandler.createAccount(validNumberReceiver, "9999");
        accountHandler.deposit(accountHandler.getAccount(validNumberSender), income);
        accountHandler.makeTransfer(accountHandler.getAccount(validNumberSender), validNumberReceiver, transferSum);

        softAssert.assertEquals(accountHandler.getAccount(validNumberReceiver).getBalance(), BigDecimal.valueOf(0.0),
                "Receiver's balance is incorrect");
        softAssert.assertEquals(accountHandler.getAccount(validNumberSender).getBalance(),
                new BigDecimal(income),
                "Sender's balance is incorrect");
        softAssert.assertAll();
    }

    @Test
    public void verify_money_is_not_sent_when_sending_to_self_account(){
        String validNumber = "4000004793834684";
        String income = "245.5";
        String transferSum = "100";

        accountHandler.createAccount(validNumber, "9999");
        accountHandler.deposit(accountHandler.getAccount(validNumber), income);
        accountHandler.makeTransfer(accountHandler.getAccount(validNumber), validNumber, transferSum);

        Assert.assertEquals(accountHandler.getAccount(validNumber).getBalance(),
                new BigDecimal(income),
                "Account's balance was affected");
    }

    @Test
    public void verify_money_is_not_sent_when_transfer_exceeding_balance(){
        String validNumberSender = "4000004793834684";
        String validNumberReceiver = "4000000000000002";
        String income = "245.5";
        String transferSum = "250.0";

        accountHandler.createAccount(validNumberSender, "9999");
        accountHandler.createAccount(validNumberReceiver, "9999");
        accountHandler.deposit(accountHandler.getAccount(validNumberSender), income);
        accountHandler.makeTransfer(accountHandler.getAccount(validNumberSender), validNumberReceiver, transferSum);

        softAssert.assertEquals(accountHandler.getAccount(validNumberReceiver).getBalance(),
                BigDecimal.valueOf(0.0),
                "Receiver's balance is incorrect");
        softAssert.assertEquals(accountHandler.getAccount(validNumberSender).getBalance(),
                new BigDecimal(income),
                "Sender's balance is incorrect");
        softAssert.assertAll();
    }

    @Test
    public void verify_money_is_not_sent_when_sending_to_not_existing_account(){
        String validNumberSender = "4000004793834684";
        String validNumberReceiver = "4000000000000002";
        String income = "245.5";
        String transferSum = "200.0";

        accountHandler.createAccount(validNumberSender, "9999");
        accountHandler.deposit(accountHandler.getAccount(validNumberSender), income);
        accountHandler.makeTransfer(accountHandler.getAccount(validNumberSender), validNumberReceiver, transferSum);

        softAssert.assertEquals(accountHandler.getAccount(validNumberSender).getBalance(),
                new BigDecimal(income),
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
        String transferSum = "200.0";

        accountHandler.createAccount(validNumberSender, "9999");
        accountHandler.createAccount(validNumberReceiver, "9999");
        accountHandler.deposit(accountHandler.getAccount(validNumberSender), income);
        accountHandler.deleteAccount(validNumberReceiver);
        accountHandler.makeTransfer(accountHandler.getAccount(validNumberSender), validNumberReceiver, transferSum);

        softAssert.assertEquals(accountHandler.getAccount(validNumberSender).getBalance(),
                new BigDecimal(income),
                "Sender's balance is incorrect");
        softAssert.assertNull(accountHandler.getAccount(validNumberReceiver),
                "Account should not be present");
        softAssert.assertAll();
    }

    private Account createAccountWithValidNumber(String cardNumber){
        return new Account(cardNumber);
    }
}
