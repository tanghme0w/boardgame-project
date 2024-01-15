package refactor;

import globals.BoardMode;
import refactor.components.MainFrame;
import refactor.server.entity.Identity;
import refactor.vo.PromptVO;
import refactor.vo.RenderVO;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public static MainFrame mainFrame;
    public static BoardMode boardMode = BoardMode.NORMAL;
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
        mainFrame.repaintBoard(renderVO.board, boardMode);

        //repaint bottom panel if necessary
        mainFrame.refreshBottomPanel(boardMode);
    }

    private static List<String> getPlayerStrings(RenderVO renderVO, Identity id) {
        List<String> playerText = new ArrayList<>();
        if (id.player == null)  {
            playerText.add("N/A");
            return playerText;
        }
        playerText.add("Name: " + id.player.playerName);
        playerText.add("Side: " + switch (id.stoneColor) {
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

    public static void popUpMessage(PromptVO promptVO) {
        JFrame frame = new JFrame("End of game");
        JOptionPane.showMessageDialog(frame, promptVO.message);
    }

    public static void removeDeadPieces(RenderVO renderVO) {
        boardMode = BoardMode.REMOVE;
        render(renderVO);
    }
}
