package dev.rob2.combatlogger.listeners;

import dev.rob2.combatlogger.managers.CombatManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LogoutListener implements Listener {

    private final CombatManager combatManager;

    public LogoutListener(CombatManager combatManager) {
        this.combatManager = combatManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        combatManager.handleLogout(p);
    }
}

