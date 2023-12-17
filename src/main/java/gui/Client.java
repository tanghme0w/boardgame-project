package gui;

import entity.BaseGame;
import command.*;

import javax.swing.*;

public class Client {
    public static MoveCommand moveCommand = new MoveCommand();
    public static AbstainCommand abstainCommand = new AbstainCommand();
    public static SurrenderCommand surrenderCommand = new SurrenderCommand();
    public static WithdrawCommand withdrawCommand = new WithdrawCommand();
    public static BaseGame game;
    public static MainFrame frame;
    public static void refresh() {
        frame.refresh();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
