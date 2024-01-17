package refactor.server.entity;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Account {
    Map<String, Player> accountMap;
    String path;
    public Account(String path) {
        // load account info from specific path
        this.path = path;
        try (FileInputStream fi = new FileInputStream(path)) {
            ObjectInputStream in = new ObjectInputStream(fi);
            this.accountMap = (Map<String, Player>) in.readObject();
        } catch (FileNotFoundException e) {
            this.accountMap = new HashMap<>();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addNewPlayer(Player player) {
        accountMap.put(player.playerName, player);
    }

    public void flush() {
        try (FileOutputStream fs = new FileOutputStream(path)) {
            ObjectOutputStream out = new ObjectOutputStream(fs);
            out.writeObject(this.accountMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Player loginWithCredentials(String playerName, String passHash) {
        Player player = accountMap.get(playerName);
        if (player == null || !Objects.equals(player.passHash, passHash)) return null;
        else return player;
    }

    public boolean accountExists(String playerName) {
        return (accountMap.get(playerName) != null);
    }
}
