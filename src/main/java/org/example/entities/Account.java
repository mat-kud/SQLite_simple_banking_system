package org.example.entities;

import java.math.BigDecimal;

public class Account {
    private final String cardNumber;
    private final BigDecimal balance;

    public Account(String cardNumber){
        this.cardNumber = cardNumber;
        this.balance = new BigDecimal("0");
    }

    public Account(String cardNumber, BigDecimal balance){
        this.cardNumber = cardNumber;
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public BigDecimal getBalance(){
        return this.balance;
    }
}
