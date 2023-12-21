package refactor.client.handler;

import refactor.client.components.SaveGameWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveGameButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        SaveGameWindow.pop();
    }
}
