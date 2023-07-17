package com.badbones69.crazycrates.paper.commands.v2.admin;

import cloud.commandframework.context.CommandContext;
import cloud.commandframework.minecraft.extras.MinecraftExtrasMetaKeys;
import com.badbones69.crazycrates.core.config.types.Locale;
import com.badbones69.crazycrates.core.frame.command.CloudCommandEngine;
import com.badbones69.crazycrates.core.frame.command.Sender;
import com.badbones69.crazycrates.core.frame.utils.AdventureUtils;
import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.api.frame.command.BukkitCommandManager;
import com.badbones69.crazycrates.paper.api.v2.enums.Permissions;
import com.badbones69.crazycrates.paper.support.PlaceholderSupport;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CommandReload extends CloudCommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final BukkitCommandManager manager = this.plugin.getCommandManager();

    @Override
    public void registerCommand() {
        this.manager.registerCommand(builder -> builder.literal("reload")
                .meta(MinecraftExtrasMetaKeys.DESCRIPTION, AdventureUtils.parse(Permissions.ADMIN_RELOAD.getDescription()))
                .permission(Permissions.ADMIN_RELOAD.getBuiltPermission())
                .handler(this::perform));
    }

    @Override
    protected void perform(@NotNull CommandContext<@NotNull Sender> context) {
        this.plugin.crazyManager().reload(false);

        String msg = this.plugin.getApiManager().getLocale().getProperty(Locale.RELOAD_PLUGIN);

        this.manager.reply(context.getSender(), PlaceholderSupport.setPlaceholders(msg));
    }
}