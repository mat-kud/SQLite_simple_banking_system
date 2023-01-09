package org.example.menus;


import org.example.handlers.AccountHandler;

import java.util.Scanner;

public abstract class BaseMenu {
    protected Scanner scanner = new Scanner(System.in);
    protected AccountHandler accountHandler;

    public BaseMenu(AccountHandler accountHandler) {
        this.accountHandler = accountHandler;
    }
}
