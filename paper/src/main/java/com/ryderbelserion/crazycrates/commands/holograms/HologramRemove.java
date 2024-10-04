package com.ryderbelserion.crazycrates.commands.holograms;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.crazycrates.CrazyCrates;
import com.ryderbelserion.crazycrates.loader.CrazyPlugin;
import com.ryderbelserion.crazycrates.managers.DisplayManager;
import com.ryderbelserion.vital.common.api.commands.Command;
import com.ryderbelserion.vital.paper.commands.PaperCommand;
import com.ryderbelserion.vital.paper.commands.context.PaperCommandInfo;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import static io.papermc.paper.command.brigadier.Commands.argument;

public class HologramRemove extends PaperCommand {

    private final CrazyPlugin plugin;

    public HologramRemove(CrazyCrates plugin) {
        this.plugin = plugin.getPlugin();
    }

    @Override
    public void execute(PaperCommandInfo info) {
        final Player player = info.getPlayer();

        final String name = info.getStringArgument("name");

        if (name.isEmpty()) {
            player.sendRichMessage("<reset> <gray(<red>!<gray>) Cannot execute the command.");

            return;
        }

        DisplayManager.removeDisplay(name);

        player.sendRichMessage("<reset> <gray>(<red>!<gray>) The hologram with the name <red>" + name + "<gray> has been removed.");
    }

    @Override
    public @NotNull String getPermission() {
        return "crazycrates.hologram.remove";
    }

    @Override
    public @NotNull LiteralCommandNode<CommandSourceStack> literal() {
        final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("remove").requires(source -> source.getSender().hasPermission(getPermission()));

        final RequiredArgumentBuilder<CommandSourceStack, String> arg1 = argument("name", StringArgumentType.string()).suggests((ctx, builder) -> {
            DisplayManager.getDisplays().keySet().forEach(builder::suggest);

            return builder.buildFuture();
        }).executes(context -> {
            execute(new PaperCommandInfo(context));

            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        });

        return root.then(arg1).build();
    }

    @Override
    public @NotNull Command<CommandSourceStack, PaperCommandInfo> registerPermission() {
        final Permission permission = this.plugin.getServer().getPluginManager().getPermission(getPermission());

        if (permission == null) {
            this.plugin.getServer().getPluginManager().addPermission(new Permission(getPermission(), PermissionDefault.OP));
        }

        return this;
    }
}