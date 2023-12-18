package deprecated.gui.handler;

import deprecated.logger.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewGomokuGameButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Logger.log("New Gomoku game");
    }
}
