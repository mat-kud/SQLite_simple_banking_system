package org.example.menus;

import org.example.generators.CardNumberGenerator;
import org.example.generators.PinGenerator;
import org.example.handlers.AccountHandler;
import org.example.utils.CardDetailsPrinter;

public class CreateAccountMenu extends BaseMenu implements Menu {
    private final CardNumberGenerator cardNumberGenerator = new CardNumberGenerator();
    private final PinGenerator pinGenerator = new PinGenerator();
    private final CardDetailsPrinter cardDetailsPrinter = new CardDetailsPrinter();

    public CreateAccountMenu(AccountHandler accountHandler) {
        super(accountHandler);
    }

    @Override
    public void execute() {
        while (true) {
            System.out.println("Creating new account");
            String cardNumber = cardNumberGenerator.generateCardNumber();
            String pin = pinGenerator.generatePinNumber();

            if (accountHandler.verifyCardNumberIsUnique(cardNumber)) {
                accountHandler.createAccount(cardNumber, pin);
                cardDetailsPrinter.printCardInfo(cardNumber, pin);
                break;
            }
        }
    }
}
