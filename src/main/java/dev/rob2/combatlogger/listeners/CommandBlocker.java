package dev.rob2.combatlogger.listeners;

import dev.rob2.combatlogger.managers.CombatManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandBlocker implements Listener {

    private final CombatManager combatManager;

    public CommandBlocker(CombatManager combatManager) {
        this.combatManager = combatManager;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();

        if (!combatManager.isInCombat(p.getUniqueId())) return;
        if (p.hasPermission("combatlogger.bypass")) return;

        String msg = e.getMessage().toLowerCase();
        for (String blocked : combatManager.getBlockedCommands()) {
            String prefix = "/" + blocked.toLowerCase();
            if (msg.startsWith(prefix)) {
                e.setCancelled(true);
                p.sendMessage("§cYou cannot use §e" + prefix + " §cwhile in combat!");
                return;
            }
        }
    }
}
