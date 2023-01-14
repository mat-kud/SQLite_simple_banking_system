package org.example;

import org.example.handlers.AccountHandler;
import org.example.managers.DatabaseManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;


public class DatabaseManagerTest {
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

        databaseManager.createAccount(validNumber1, "9999", cardsTableName);
        databaseManager.createAccount(validNumber2, "9607", cardsTableName);
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
        double income = 210.1;

        databaseManager.createAccount(validNumber, "9999", cardsTableName);
        databaseManager.updateAccountBalance(validNumber, income, cardsTableName);
        databaseManager.updateAccountBalance(validNumber, income, cardsTableName);

        Assert.assertEquals(income*2, databaseManager.getAccount(validNumber, cardsTableName).getBalance(),
                "Income was not properly added to account's balance.");
    }


    @Test
    public void verifyAccountIsDeleted(){
        String validNumber = "4000004793834684";

        databaseManager.createAccount(validNumber, "9999", cardsTableName);
        databaseManager.deleteAccount("4000004793834684", cardsTableName);

        Assert.assertNull(databaseManager.getAccount(validNumber, cardsTableName), "Account is not deleted.");
    }
}
