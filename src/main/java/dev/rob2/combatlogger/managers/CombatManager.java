package dev.rob2.combatlogger;

import dev.rob2.combatlogger.commands.CombatCommand;
import dev.rob2.combatlogger.listeners.CombatListener;
import dev.rob2.combatlogger.listeners.CommandBlocker;
import dev.rob2.combatlogger.listeners.LogoutListener;
import dev.rob2.combatlogger.managers.CombatManager;
import dev.rob2.combatlogger.util.ActionBarUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class CombatLogger extends JavaPlugin {

    private static CombatLogger instance;   // NEW
    private CombatManager combatManager;

    @Override
    public void onEnable() {
        instance = this;                    // NEW

        saveDefaultConfig();
        this.combatManager = new CombatManager(this);
        combatManager.loadSettings();

        Bukkit.getPluginManager().registerEvents(new CombatListener(this, combatManager), this);
        Bukkit.getPluginManager().registerEvents(new CommandBlocker(combatManager), this);
        Bukkit.getPluginManager().registerEvents(new LogoutListener(combatManager), this);

        if (getCommand("combat") != null) {
            getCommand("combat").setExecutor(new CombatCommand(combatManager));
        }

        // Cleanup expired tags
        Bukkit.getScheduler().runTaskTimer(this, combatManager::cleanupExpiredTags, 20L, 20L);

        // ActionBar countdown
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (UUID uuid : combatManager.getTaggedPlayers()) {
                Player p = Bukkit.getPlayer(uuid);
                if (p == null) continue;
                long remaining = combatManager.getRemainingSeconds(uuid);
                if (remaining > 0) {
                    ActionBarUtil.sendActionBar(p, "§cIn combat: §e" + remaining + "s");
                }
            }
        }, 20L, 20L);

        getLogger().info("CombatLogger enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("CombatLogger disabled.");
    }

    // --- NEW: API accessors for other plugins ---
    public static CombatLogger getInstance() {
        return instance;
    }

    public CombatManager getCombatManager() {
        return combatManager;
    }
}

