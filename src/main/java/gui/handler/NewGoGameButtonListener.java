package gui.handler;

import entity.GoGame;
import gui.Client;
import logger.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewGoGameButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String input = Client.frame.getInput();
        if (input == null) {
            Logger.log("New Go game with board size 19*19");
            Client.game = new GoGame();
            Client.game.startGame();
            Logger.log("Tips: If you would like to customize chessboard size, " +
                    "please input board size (an integer) at the input box before clicking the start button.");
        } else {
            int inputBoardSize;
            try {
                inputBoardSize = Integer.parseInt(input);
            } catch (Exception ignored) {
                Logger.log("Start new game failed: invalid input, please enter a single integer ranging from 13~19.");
                return;
            }
            Logger.log("New Go game with board size" + inputBoardSize + "*" + inputBoardSize);
            Client.game = new GoGame(Integer.parseInt(input));
            Client.game.startGame();
        }
    }
}
