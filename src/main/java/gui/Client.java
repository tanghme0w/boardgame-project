package gui;

import gui.ConsolePane;
import gui.MainFrame;
import response.CustomException;

import javax.swing.*;

public class Client {
    public static MainFrame frame;
    public static ConsolePane consolePane;
    public static void main(String[] args) {
        setLookAndFeel();
        frame = new MainFrame();
    }
    public static void setLookAndFeel() {
        try {
            if (System.getProperty("os.name").contains("Mac")) {
                System.setProperty("apple.laf.useScreenMenuBar", "true");
            }
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            CustomException.warn(e.getMessage());
        }
    }
}
