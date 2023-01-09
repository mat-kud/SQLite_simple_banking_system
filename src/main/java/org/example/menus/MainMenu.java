package org.example.menus;


import org.example.handlers.AccountHandler;
import org.example.managers.MenuManager;

public class MainMenu extends BaseMenu implements Menu {

    public MainMenu(AccountHandler accountHandler) {
        super(accountHandler);
    }

    @Override
    public void execute() {
        while (true) {
            System.out.println("""
                    1. Create an account
                    2. Log into account
                    0. Exit""");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> MenuManager.runCreateAccountMenu();
                    case 2 -> MenuManager.runLogInMenu();
                    case 0 -> {
                        System.out.println();
                        System.out.println("Bye!");
                        System.exit(0);
                    }
                    default -> System.out.println("Wrong number");
                }
            } catch (NumberFormatException e){
                System.err.println("Wrong input format. Only following numbers are accepted: 0, 1, 2.");
            }
        }
    }
}
