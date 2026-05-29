package dev.rob2.combatlogger.util;

import org.bukkit.entity.Player;

public class ActionBarUtil {

    public static void sendActionBar(Player player, String message) {
        // Paper/Spigot 1.20+ has this directly
        player.sendActionBar(message);
    }
}
