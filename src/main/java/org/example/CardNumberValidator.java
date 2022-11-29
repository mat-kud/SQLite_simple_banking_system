package org.example;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CardNumberValidator {
    public boolean isCardNumberValid(String cardNumber){
        if(cardNumber.length() != 16 || cardNumber.charAt(0) == '-'
            || !cardNumber.startsWith("400000")){
            return false;
        }
        String lastDigit = cardNumber.substring(15);
        String binAndAccountIdentifier = cardNumber.substring(0,15);
        int checkSum = calculateLuhnAlgorithmSum(binAndAccountIdentifier);
        String calculatedLastDigit = String.valueOf((10 - checkSum % 10) % 10);

        return lastDigit.equals(calculatedLastDigit);
    }

    private int calculateLuhnAlgorithmSum(String binAndAccountIdentifier){
        List<Integer> digits = Arrays.stream(binAndAccountIdentifier
                .split(""))
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toList());
        /*Luhn-Algorithm
        odd, even based on table where digits' numerations start from 1*/
        int sum = 0;

        for (int i = 0; i < digits.size(); i++) {
            int value = digits.get(i);
            if (i % 2 == 0) {
                value = (value * 2) > 9 ? value * 2 - 9 : value * 2;
            }
            sum += value;
        }

        return sum;
    }
}
