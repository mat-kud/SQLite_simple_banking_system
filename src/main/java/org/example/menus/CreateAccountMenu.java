package org.example.menus;

import org.example.handlers.AccountHandler;
import org.example.generators.CardNumberGenerator;
import org.example.generators.PinGenerator;

public class CreateAccountMenu extends BaseMenu implements Menu {
    private final CardNumberGenerator cardNumberGenerator = new CardNumberGenerator();
    private final PinGenerator pinGenerator = new PinGenerator();

    public CreateAccountMenu(AccountHandler accountHandler) {
        super(accountHandler);
    }

    @Override
    public void execute() {
        while (true) {
            String cardNumber = cardNumberGenerator.generateCardNumber();
            String pin = pinGenerator.generatePinNumber();

            if (verifyCardNumberIsUnique(cardNumber)) {
                accountHandler.createAccount(cardNumber, pin);
                printCardInfo(cardNumber, pin);
                break;
            }
        }
    }

    private boolean verifyCardNumberIsUnique(String cardNumber){
        return accountHandler.getAllAccountsNumbers().stream()
                .noneMatch(num -> num.equals(cardNumber));
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
