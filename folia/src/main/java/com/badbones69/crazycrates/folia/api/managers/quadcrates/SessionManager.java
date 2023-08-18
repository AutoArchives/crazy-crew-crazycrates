package com.badbones69.crazycrates.folia.api.managers.quadcrates;

import com.badbones69.crazycrates.folia.api.managers.QuadCrateManager;
import org.bukkit.entity.Player;

public class SessionManager {

    public boolean inSession(Player player) {
        for (com.badbones69.crazycrates.folia.api.managers.QuadCrateManager quadCrateManager : com.badbones69.crazycrates.folia.api.managers.QuadCrateManager.getCrateSessions()) {
            if (quadCrateManager.getPlayer() == player) return true;
        }

        return false;
    }

    public com.badbones69.crazycrates.folia.api.managers.QuadCrateManager getSession(Player player) {
        for (com.badbones69.crazycrates.folia.api.managers.QuadCrateManager quadCrateManager : com.badbones69.crazycrates.folia.api.managers.QuadCrateManager.getCrateSessions()) {
            if (quadCrateManager.getPlayer() == player) return quadCrateManager;
        }

        return null;
    }

    public static void endCrates() {
        com.badbones69.crazycrates.folia.api.managers.QuadCrateManager.getCrateSessions().forEach(session -> session.endCrateForce(false));
        QuadCrateManager.getCrateSessions().clear();
    }
}