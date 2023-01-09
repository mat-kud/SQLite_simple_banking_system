package org.example.generators;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CardNumberGenerator {
    Random random = new Random();

    public String generateCardNumber() {
        String bin = "400000";
        String accountIdentifier = IntStream.generate(() -> random.nextInt(10))
                .limit(9)
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.joining());

        return bin + accountIdentifier + calculateCheckSumDigit(bin + accountIdentifier);
    }

    private int calculateCheckSumDigit(String binAndAccountIdentifier) {
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

        return (10 - sum % 10) % 10;
    }
}
