package refactor.handler;

import refactor.components.LoadGameWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadGameButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        LoadGameWindow.pop();
    }
}
