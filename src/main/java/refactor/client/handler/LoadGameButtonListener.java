package refactor.client.handler;

import refactor.client.components.LoadGameWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadGameButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        LoadGameWindow.pop();
    }
}
