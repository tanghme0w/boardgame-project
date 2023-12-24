package refactor.client;

import refactor.EndGameVO;
import refactor.Identity;
import refactor.RenderVO;
import refactor.client.components.MainFrame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public static MainFrame mainFrame;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
    public static void render(RenderVO renderVO) {
        //construct player info
        for(int i = 0; i < renderVO.identities.size(); i++) {
            //player name
            Identity id = renderVO.identities.get(i);
            List<String> playerText = getPlayerStrings(renderVO, id);
            mainFrame.setPlayerText(i, playerText);
        }

        //construct log info
        mainFrame.setLogText(renderVO.log);

        //repaint side panel
        mainFrame.setCurrentActingPlayerIndex(renderVO.players.indexOf(renderVO.currentActingIdentity.player));
        mainFrame.refreshSidePanel();

        //repaint board
        mainFrame.repaintBoard(renderVO.board);
    }

    private static List<String> getPlayerStrings(RenderVO renderVO, Identity id) {
        List<String> playerText = new ArrayList<>();
        if (id.player == null)  {
            playerText.add("N/A");
            return playerText;
        }
        playerText.add("Name: " + id.player.name);
        playerText.add("Side: " + switch (id.chessType) {
            case BLACK -> "black";
            case WHITE -> "white";
        });
        playerText.add("Withdraw times: " + id.withdrawCount);
        if (id.hasAbstained) {
            playerText.add("ABSTAIN");
        }
        if (renderVO.currentActingIdentity.equals(id)) {
            playerText.add("CURRENT ACTING PLAYER");
        }
        return playerText;
    }

    public static void popUpMessage(EndGameVO endGameVO) {
        JFrame frame = new JFrame("End of game");
        JOptionPane.showMessageDialog(frame, endGameVO.message);
    }
}
