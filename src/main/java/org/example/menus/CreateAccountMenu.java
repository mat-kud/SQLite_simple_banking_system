package org.example.menus;


import org.example.AccountHandler;

public class CreateAccountMenu extends BaseMenu implements Menu {


    public CreateAccountMenu(AccountHandler accountHandler) {
        super(accountHandler);
    }

    @Override
    public void execute() {
        String[] cardInfo = accountHandler.createAccount();
        printCardInfo(cardInfo[0], cardInfo[1]);
    }

    private void printCardInfo(String cardNumber, String cardPin) {
        System.out.printf("""
                Your card has been created
                Your card number:
                %s
                Your card PIN:
                %s""", cardNumber, cardPin);
        System.out.println();
    }
}
