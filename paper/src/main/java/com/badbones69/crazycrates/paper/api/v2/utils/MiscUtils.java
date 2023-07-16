package com.badbones69.crazycrates.paper.api.v2.utils;

import com.badbones69.crazycrates.paper.api.v2.enums.Permissions;
import org.bukkit.entity.HumanEntity;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

public class MiscUtils {

    public static String convertLegacyPlaceholders(String message) {
        return message
                .replaceAll("%command%", "{command}")
                .replaceAll("%crate%", "{crate}")
                .replaceAll("%key%", "{key}")
                .replaceAll("%usage%", "{usage}")
                .replaceAll("%prize%", "{prize}")
                .replaceAll("%player%", "{player}")
                .replaceAll("%amount%", "{amount}")
                .replaceAll("%world%", "{world}")
                .replaceAll("%id%", "{id}")
                .replaceAll("%prefix%", "{prefix}")
                .replaceAll("%number%", "{number}")
                .replaceAll("%cratetype%", "{cratetype}")
                .replaceAll("%keys%", "{keys}");
    }

    public static void registerPermissions(PluginManager pluginManager) {
        for (Permissions permission : Permissions.values()) {
            if (pluginManager.getPermission(permission.getBuiltPermission()) != null) return;

            pluginManager.addPermission(new Permission(permission.getBuiltPermission(), permission.getDescription(), permission.getPermissionDefault()));
        }
    }

    public static boolean hasPermission(HumanEntity entity, Permission permissions) {
        return entity.hasPermission(permissions);
    }

    public static boolean hasPermission(HumanEntity entity, String rawPermission) {
        return entity.hasPermission(rawPermission);
    }
}