package dev.rob2.combatlogger;

import dev.rob2.combatlogger.managers.CombatManager;

import java.util.UUID;

public class CombatAPI {

    public static boolean isInCombat(UUID uuid) {
        CombatManager manager = CombatLogger.getInstance().getCombatManager();
        return manager.isInCombat(uuid);
    }

    public static long getRemainingSeconds(UUID uuid) {
        CombatManager manager = CombatLogger.getInstance().getCombatManager();
        return manager.getRemainingSeconds(uuid);
    }
}

