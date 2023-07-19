package com.badbones69.crazycrates.paper.commands.v2.admin.keys;

import cloud.commandframework.context.CommandContext;
import cloud.commandframework.minecraft.extras.MinecraftExtrasMetaKeys;
import com.badbones69.crazycrates.core.frame.command.CloudCommandEngine;
import com.badbones69.crazycrates.core.frame.command.Sender;
import com.badbones69.crazycrates.core.frame.utils.AdventureUtils;
import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.api.frame.command.v1.BukkitCommandManager;
import com.badbones69.crazycrates.paper.api.v2.enums.Permissions;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CommandGiveKeys extends CloudCommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final BukkitCommandManager manager = this.plugin.getCommandManager();

    @Override
    public void registerCommand() {
        this.manager.registerCommand(builder -> builder.literal("addkey")
                .meta(MinecraftExtrasMetaKeys.DESCRIPTION, AdventureUtils.parse(Permissions.ADMIN_GIVE_KEY.getDescription()))
                .permission(Permissions.ADMIN_GIVE_KEY.getBuiltPermission())
                .handler(this::perform));
    }

    @Override
    protected void perform(@NotNull CommandContext<@NotNull Sender> context) {
        this.manager.reply(context.getSender(), "<red>Guten Tag!</red>");
    }
}