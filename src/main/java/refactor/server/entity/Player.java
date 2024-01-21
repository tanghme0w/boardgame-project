package refactor.server.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Player implements Serializable {
    public boolean isAI;
    public Integer AILevel;
    public String playerName;
    public String passHash;
    public Map<String, Integer> joinRecord;
    public Map<String, Integer> winRecord;
    Player(Player player) {
        this.isAI = player.isAI;
        this.AILevel = 0;
        this.playerName = player.playerName;
        this.passHash = player.passHash;
        //perform deep copy for record
        joinRecord = new HashMap<>();
        joinRecord.putAll(player.joinRecord);
        winRecord = new HashMap<>();
        winRecord.putAll(player.winRecord);
    }
    public Player(String playerName) {
        this.isAI = false;
        this.AILevel = 0;
        this.playerName = playerName;
        this.passHash = "";
        this.joinRecord = new HashMap<>();
        this.winRecord = new HashMap<>();
    }
    public Player(String playerName, String passHash) {
        this.isAI = false;
        this.AILevel = 0;
        this.playerName = playerName;
        this.passHash = passHash;
        this.joinRecord = new HashMap<>();
        this.winRecord = new HashMap<>();
    }

    public void joinCount(String gameName) {
        joinRecord.put(gameName, joinRecord.getOrDefault(gameName, 0) + 1);
        //TODO serialization to a file
    }

    public void winCount(String gameName) {
        winRecord.put(gameName, joinRecord.getOrDefault(gameName, 0) + 1);
        //TODO serialization to a file
    }

}
