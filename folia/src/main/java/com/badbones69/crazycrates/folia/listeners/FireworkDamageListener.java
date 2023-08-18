package com.badbones69.crazycrates.folia.listeners;

import com.badbones69.crazycrates.folia.CrazyCrates;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class FireworkDamageListener implements Listener {

    private static final com.badbones69.crazycrates.folia.CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private static final NamespacedKey noDamage = new NamespacedKey(plugin, "no-damage");

    /**
     * @param firework The firework you want to add.
     */
    public static void addFirework(Entity firework) {
        PersistentDataContainer container = firework.getPersistentDataContainer();

        container.set(noDamage, PersistentDataType.STRING, "no-damage");
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Firework firework) {

            PersistentDataContainer container = firework.getPersistentDataContainer();

            if (container.has(noDamage, PersistentDataType.STRING)) e.setCancelled(true);
        }
    }
}