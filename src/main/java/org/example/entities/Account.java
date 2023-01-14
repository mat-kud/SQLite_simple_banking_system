package org.example.entities;

public class Account {
    private int id;
    private final String cardNumber;
    private final String cardPin;
    private final double balance;

    public Account(int id, String cardNumber, String cardPin, double balance){
        this.id = id;
        this.cardNumber = cardNumber;
        this.cardPin = cardPin;
        this.balance = balance;
    }

    public Account(String cardNumber, String cardPin){
        this.cardNumber = cardNumber;
        this.cardPin = cardPin;
        this.balance = 0;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public double getBalance(){
        return this.balance;
    }

    public boolean verifyCardNumber(String cardNumber){
        return this.cardNumber.equals(cardNumber);
    }

    public boolean verifyCardPin(String cardPin){
        return this.cardPin.equals(cardPin);
    }

}
