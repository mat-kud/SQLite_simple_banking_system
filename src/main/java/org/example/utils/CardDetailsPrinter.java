package org.example.utils;

public class CardDetailsPrinter {
    public void printCardInfo(String cardNumber, String cardPin) {
        System.out.printf("""
                Your card has been created
                Your card number:
                %s
                Your card PIN:
                %s""", cardNumber, cardPin);
        System.out.println();
    }
}
