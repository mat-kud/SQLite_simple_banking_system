package org.example.menus;


import org.example.AccountHandler;
import org.example.MenuManager;

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
                    0. Exit
                    """);

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
        }
    }
}
