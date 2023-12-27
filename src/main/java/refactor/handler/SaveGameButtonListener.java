package refactor.handler;

import refactor.components.SaveGameWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveGameButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        SaveGameWindow.pop();
    }
}
