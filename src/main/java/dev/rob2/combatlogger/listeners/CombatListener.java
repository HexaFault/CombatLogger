package dev.rob2.combatlogger.listeners;

import dev.rob2.combatlogger.CombatLogger;
import dev.rob2.combatlogger.managers.CombatManager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CombatListener implements Listener {

    private final CombatLogger plugin;
    private final CombatManager combatManager;

    public CombatListener(CombatLogger plugin, CombatManager combatManager) {
        this.plugin = plugin;
        this.combatManager = combatManager;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player victim)) return;

        Player attacker = null;

        if (e.getDamager() instanceof Player p) {
            if (combatManager.isMeleeTaggingEnabled()) {
                attacker = p;
            }
        }

        if (e.getDamager() instanceof Projectile projectile) {
            if (!combatManager.isProjectileTaggingEnabled()) return;
            if (projectile.getShooter() instanceof Player shooter) {
                String type = projectile.getType().name().toLowerCase();
                if (combatManager.isProjectileTypeEnabled(type)) {
                    attacker = shooter;
                }
            }
        }

        if (attacker == null) return;

        combatManager.tag(victim.getUniqueId());
        combatManager.tag(attacker.getUniqueId());

        victim.sendMessage("§cYou are now in combat!");
        attacker.sendMessage("§cYou are now in combat!");
    }
}

