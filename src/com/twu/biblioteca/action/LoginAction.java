package com.twu.biblioteca.action;

import com.twu.biblioteca.constants.Messages;
import com.twu.biblioteca.view.ConsoleView;

import java.io.IOException;

public class LoginAction implements MenuAction {


    private ConsoleView consoleView;

    public LoginAction(ConsoleView consoleView) {
        this.consoleView = consoleView;
    }

    @Override
    public void performAction() {
        consoleView.print(Messages.USERNAME_PROMPT);
        String username = "";
        try {
            username = consoleView.getName();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}