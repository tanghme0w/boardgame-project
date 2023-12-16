package entity;

import exception.Exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlayerManager {
    Map<Identity, Player> players;
    Player setPlayer(Player player) {
        if (player.id == Identity.RANDOM) {
            return setPlayerWithRandomIdentity(player);
        } else return setPlayerWithCustomIdentity(player);
    }
    Player getPlayerWithIdentity(Identity id) {
        return players.get(id);
    }
    Player removePlayer(Player player) {
    }
    Player setPlayerWithCustomIdentity(Player player) {
        if (isIdentityAvailable(player.id)) {
            players.put(player.id, player);
        } else {
            Exception.warn("Player register error: the desired identity has already been taken, please change your desired identity or wait until other players exit.");
        }
        return player;
    }
    Player setPlayerWithRandomIdentity(Player player) {
        Identity randomAvailableIdentity = getRandomAvailableIdentity();
        if (randomAvailableIdentity != null) {
            player.id = randomAvailableIdentity;
            setPlayerWithCustomIdentity(player);
            return player;
        } else {
            Exception.warn("Player register error: no available identities. The room is already full, please wait until other players exit.");
            return player;
        }
    }
    Boolean isIdentityAvailable(Identity id) {
        return Boolean.FALSE;
    }
    List<Identity> getAvailableIdentities() {
        List<Identity> availableIdentities = new ArrayList<>();
        for(Identity id: Identity.values()) {
            if ((id != Identity.RANDOM) && !players.containsKey(id)) {
                availableIdentities.add(id);
            }
        }
        return availableIdentities;
    }
    Identity getRandomAvailableIdentity() {
        Random rand = new Random();
        List<Identity> availableIdentities = getAvailableIdentities();
        int numberOfAvailableIdentities = availableIdentities.size();
        if (numberOfAvailableIdentities == 0) return null;
        else {
            int randInt = rand.nextInt(numberOfAvailableIdentities);
            return availableIdentities.get(randInt);
        }
    }
}
