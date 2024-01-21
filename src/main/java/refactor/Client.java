package refactor;

import globals.BoardMode;
import refactor.components.MainFrame;
import refactor.server.entity.Identity;
import refactor.server.entity.Player;
import refactor.vo.PromptVO;
import refactor.vo.RenderVO;

import javax.swing.*;
import java.util.*;

public class Client {
    public static MainFrame mainFrame;
    public static BoardMode boardMode = BoardMode.WAIT;
    public static boolean isAIActing = false;
    public static Map<Integer, Boolean> playerLoginStatus = new HashMap<>(Map.of(0, false, 1, false));
    public static Map<Integer, Boolean> playerISAI = new HashMap<>(Map.of(0, false, 1, false));
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
    public static void render(RenderVO renderVO) {
        //construct player info
        for(int i = 0; i < renderVO.players.size(); i++) {
            Player player = renderVO.players.get(i);
            if (player == null) playerLoginStatus.put(i, false);
            else {
                playerLoginStatus.put(i, true);
                playerISAI.put(i, player.isAI);
            }
            List<String> playerInfo = formulatePlayerInfo(player);
            List<String> identityInfo;
            if (boardMode == BoardMode.IN_GAME) {
                identityInfo = formulateIdentityInfo(renderVO.identities.get(i), renderVO.currentActingIdentity);
                playerInfo.addAll(identityInfo);
            }
            mainFrame.setPlayerText(i, playerInfo);
        }

        //repaint game-related components
        if (boardMode == BoardMode.IN_GAME || boardMode == BoardMode.REMOVE) {
            mainFrame.setCurrentActingPlayerIndex(renderVO.players.indexOf(renderVO.currentActingIdentity.player));
            //repaint board
            mainFrame.repaintBoard(renderVO.board, boardMode);
        }

        //construct log info
        mainFrame.setLogText(renderVO.log);

        //repaint side panel
        mainFrame.refreshSidePanel();

        //repaint bottom panel if necessary
        mainFrame.refreshBottomPanel(boardMode);
    }

    private static List<String> formulateIdentityInfo(Identity identity, Identity currentActingIdentity) {
        List<String> content = new ArrayList<>();
        String sb = "Side: " + identity.stoneColor.string() +
                "  Withdraw times: " + identity.withdrawCount;
        content.add(sb);
        if (identity.hasAbstained) content.add("ABSTAIN");
        if (Objects.equals(identity, currentActingIdentity)) content.add("CURRENT ACTING PLAYER");
        return content;
    }

    private static List<String> formulatePlayerInfo(Player player) {
        List<String> content = new ArrayList<>();
        if (player == null) {
            content.add("Empty Slot.");
            return content;
        }
        content.add("Name: " + player.playerName);
        content.add("Joined Games: " +
                player.joinRecord.getOrDefault("total", 0) + " (total) / " +
                player.joinRecord.getOrDefault("go", 0) + " (Go) / " +
                player.joinRecord.getOrDefault("gomoku", 0) + " (Gomoku) / " +
                player.joinRecord.getOrDefault("reversi", 0) + " (Reversi) "
        );
        content.add("Winning Times: " +
                player.winRecord.getOrDefault("total", 0) + " (total) / " +
                player.winRecord.getOrDefault("go", 0) + " (Go) / " +
                player.winRecord.getOrDefault("gomoku", 0) + " (Gomoku) / " +
                player.winRecord.getOrDefault("reversi", 0) + " (Reversi) "
        );
        return content;
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
