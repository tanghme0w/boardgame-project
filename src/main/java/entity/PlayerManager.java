package entity;

import response.CustomException;
import response.Success;

import java.util.*;

public class PlayerManager {
    Map<Identity, Player> existingPlayers;
    IdentityOperations identityOperations;
    PlayerManager() {
        existingPlayers = new HashMap<>(2);
        identityOperations = new IdentityOperations();
    }
    Player addNewPlayer(Player newPlayer) {
        AddNewPlayerStrategy strategy;
        if (newPlayer.id == null) {
            strategy = new addNewPlayerWithRandomId();
        } else {
            strategy = new addNewPlayerWithCustomId();
        }
        return strategy.execute(existingPlayers, newPlayer, identityOperations);
    }
    void removePlayer(Player newPlayer) {
        if (!existingPlayers.containsKey(newPlayer.id)) return;
        else {
            existingPlayers.remove(newPlayer.id);
            Success.show("Successfully removed player: "+newPlayer.name);
        }
    }
    Player getPlayerWithIdentity(Identity id) {
        return existingPlayers.get(id);
    }
    Boolean roomFull() {
        return (identityOperations.getAvailableIds(existingPlayers).isEmpty());
    }
}

interface AddNewPlayerStrategy {
    public Player execute(Map<Identity, Player> existingPlayers, Player newPlayer, IdentityOperations idOps);
}

class addNewPlayerWithRandomId implements AddNewPlayerStrategy {
    @Override
    public Player execute(Map<Identity, Player> existingPlayers, Player newPlayer, IdentityOperations idOps) {
        Identity randomAvailableIdentity = idOps.getOneRandomAvailableId(existingPlayers);
        if (randomAvailableIdentity == null) {
            CustomException.warn("Cannot add new player: no available identities. The room is already full. " +
                    "Please wait until other players exit.");
        } else {
            newPlayer.id = randomAvailableIdentity;
            existingPlayers.put(newPlayer.id, newPlayer);
            Success.show("Successfully add player: "+newPlayer.name+", player identity: "+newPlayer.id);
        }
        return newPlayer;
    }
}

class addNewPlayerWithCustomId implements AddNewPlayerStrategy {
    @Override
    public Player execute(Map<Identity, Player> existingPlayers, Player newPlayer, IdentityOperations idOps) {
        if (idOps.checkIdAvailability(existingPlayers, newPlayer.id)) {
            existingPlayers.put(newPlayer.id, newPlayer);
            Success.show("Successfully add player: "+newPlayer.name+", player identity: "+newPlayer.id);
        } else {
            CustomException.warn("Cannot add new player: the desired identity has already been taken, " +
                    "please change your desired identity or wait until other players exit.");
        }
        return newPlayer;
    }
}

class IdentityOperations {
    Boolean checkIdAvailability(Map<Identity, Player> existingPlayers, Identity id) {
        return ((id != null) && !existingPlayers.containsKey(id));
    }
    List<Identity> getAvailableIds(Map<Identity, Player> existingPlayers) {
        List<Identity> availableIdentities = new ArrayList<>();
        for(Identity id: Identity.values()) {
            if (checkIdAvailability(existingPlayers, id)) {
                availableIdentities.add(id);
            }
        }
        return availableIdentities;
    }
    Identity getOneRandomAvailableId(Map<Identity, Player> existingPlayers) {
        Random rand = new Random();
        List<Identity> availableIdentities = getAvailableIds(existingPlayers);
        int numberOfAvailableIdentities = availableIdentities.size();
        if (numberOfAvailableIdentities == 0) return null;
        else {
            int randInt = rand.nextInt(numberOfAvailableIdentities);
            return availableIdentities.get(randInt);
        }
    }
}