package dev.rob2.combatlogger.managers;

import dev.rob2.combatlogger.CombatLogger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CombatManager {

    private final CombatLogger plugin;
    private final Map<UUID, Long> combatMap = new HashMap<>();

    private long combatTimeMillis;
    private boolean meleeTagging;
    private boolean projectileTagging;
    private Set<String> enabledProjectiles;
    private List<String> blockedCommands;
    private String logoutAction;
    private List<String> logoutCommands;

    public CombatManager(CombatLogger plugin) {
        this.plugin = plugin;
    }

    public void loadSettings() {
        combatTimeMillis = plugin.getConfig().getInt("combat-time", 15) * 1000L;
        meleeTagging = plugin.getConfig().getBoolean("melee-tagging", true);
        projectileTagging = plugin.getConfig().getBoolean("projectile-tagging", true);

        enabledProjectiles = new HashSet<>();
        ConfigurationSection projSec = plugin.getConfig().getConfigurationSection("projectiles");
        if (projSec != null) {
            for (String key : projSec.getKeys(false)) {
                if (projSec.getBoolean(key)) {
                    enabledProjectiles.add(key.toLowerCase());
                }
            }
        }

        blockedCommands = plugin.getConfig().getStringList("blocked-commands");
        logoutAction = plugin.getConfig().getString("logout-action", "kill").toLowerCase();
        logoutCommands = plugin.getConfig().getStringList("logout-commands");
    }

    public void tag(UUID uuid) {
        combatMap.put(uuid, System.currentTimeMillis());
    }

    public boolean isInCombat(UUID uuid) {
        Long last = combatMap.get(uuid);
        if (last == null) return false;
        return System.currentTimeMillis() - last <= combatTimeMillis;
    }

    public long getRemainingSeconds(UUID uuid) {
        Long last = combatMap.get(uuid);
        if (last == null) return 0;
        long remaining = (combatTimeMillis - (System.currentTimeMillis() - last)) / 1000;
        return Math.max(remaining, 0);
    }

    public void cleanupExpiredTags() {
        long now = System.currentTimeMillis();
        combatMap.entrySet().removeIf(e -> now - e.getValue() > combatTimeMillis);
    }

    public Set<UUID> getTaggedPlayers() {
        return new HashSet<>(combatMap.keySet());
    }

    public boolean isMeleeTaggingEnabled() {
        return meleeTagging;
    }

    public boolean isProjectileTaggingEnabled() {
        return projectileTagging;
    }

    public boolean isProjectileTypeEnabled(String type) {
        return enabledProjectiles.contains(type.toLowerCase());
    }

    public List<String> getBlockedCommands() {
        return blockedCommands;
    }

    public void handleLogout(Player player) {
        UUID uuid = player.getUniqueId();
        if (!isInCombat(uuid)) return;

        switch (logoutAction) {
            case "kill" -> player.setHealth(0.0);
            case "drop-inventory" -> {
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null) {
                        player.getWorld().dropItemNaturally(player.getLocation(), item);
                    }
                }
                player.getInventory().clear();
            }
            case "run-command" -> {
                for (String cmd : logoutCommands) {
                    String parsed = cmd.replace("%player%", player.getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsed);
                }
            }
            case "none" -> {
                // do nothing
            }
            default -> player.setHealth(0.0);
        }

        combatMap.remove(uuid);
    }
}

