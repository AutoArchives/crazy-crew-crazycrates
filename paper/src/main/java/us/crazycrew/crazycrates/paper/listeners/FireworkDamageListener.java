package us.crazycrew.crazycrates.paper.listeners;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import us.crazycrew.crazycrates.paper.CrazyCrates;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class FireworkDamageListener implements Listener {

    private final @NotNull CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Firework firework) {
            PersistentDataContainer container = firework.getPersistentDataContainer();

            NamespacedKey noDamage = new NamespacedKey(this.plugin, "no-damage");

            if (container.has(noDamage, PersistentDataType.STRING)) e.setCancelled(true);
        }
    }
}