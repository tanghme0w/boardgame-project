package refactor.server.entity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Objects;

public class Account {
    Map<String, Player> accountMap;
    String path;
    Account(String path) {
        // load account info from specific path
        this.path = path;
        try (FileInputStream fs = new FileInputStream(path)) {
            ObjectInputStream in = new ObjectInputStream(fs);
            this.accountMap = (Map<String, Player>) in.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Player player) {
        accountMap.put(player.playerName, player);
        try (FileOutputStream fs = new FileOutputStream(path)) {
            ObjectOutputStream out = new ObjectOutputStream(fs);
            out.writeObject(this.accountMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Player findPlayerWithNameAndPass(String playerName, String passHash) {
        Player player = accountMap.get(playerName);
        if (player == null || !Objects.equals(player.passHash, passHash)) return null;
        else return player;
    }
}
