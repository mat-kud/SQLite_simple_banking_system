package org.example;

import org.example.validators.CardNumberValidator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CardNumberValidatorTest {
    CardNumberValidator cardNumberValidator = new CardNumberValidator();

    @DataProvider(name = "validCardNumbers")
    public Object[][] validCardNumbers() {
        return new Object[][]{
                {"4000000000000002"},
                {"4000004793834684"},
                {"4000009999999991"}
        };
    }

    @DataProvider(name = "invalidCardNumbers")
    public Object[][] invalidCardNumbers() {
        return new Object[][]{
                {"7550945537211406"},
                {"4000007859334578"},
                {"40000085774886961"},
                {"400000846065304"},
                {"-4000004793834684"}
        };
    }

    @Test(dataProvider = "invalidCardNumbers")
    public void givenInvalidCardNumberWhenValidatedThenShouldReturnFalse(String cardNumber){
        Assert.assertFalse(cardNumberValidator.isCardNumberValid(cardNumber));
    }

    @Test(dataProvider = "validCardNumbers")
    public void givenValidCardNumberWhenValidatedThenShouldReturnTrue(String cardNumber){
        Assert.assertTrue(cardNumberValidator.isCardNumberValid(cardNumber));
    }
}
