package com.badbones69.crazycrates.paper.api.v2.utils;

import com.badbones69.crazycrates.paper.api.v2.enums.Permissions;
import org.bukkit.entity.HumanEntity;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

public class MiscUtils {

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