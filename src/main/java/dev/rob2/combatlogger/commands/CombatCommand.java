package dev.rob2.combatlogger.commands;

import dev.rob2.combatlogger.managers.CombatManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CombatCommand implements CommandExecutor {

    private final CombatManager combatManager;

    public CombatCommand(CombatManager combatManager) {
        this.combatManager = combatManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (!combatManager.isInCombat(p.getUniqueId())) {
            p.sendMessage("§aYou are not in combat.");
            return true;
        }

        long remaining = combatManager.getRemainingSeconds(p.getUniqueId());
        p.sendMessage("§cYou are in combat for another §e" + remaining + "s§c.");
        return true;
    }
}
