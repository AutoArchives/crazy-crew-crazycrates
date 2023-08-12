package com.badbones69.crazycrates.paper.commands.subs;

import com.badbones69.crazycrates.api.ConfigManager;
import com.badbones69.crazycrates.api.config.types.Locale;
import com.badbones69.crazycrates.api.enums.Permissions;
import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.api.enums.settings.Messages;
import com.badbones69.crazycrates.paper.commands.provider.BukkitCommandProvider;
import com.ryderbelserion.lexicon.bukkit.builders.commands.BukkitCommandContext;
import com.ryderbelserion.lexicon.bukkit.builders.commands.BukkitCommandEngine;
import com.ryderbelserion.lexicon.core.builders.commands.args.Argument;
import com.ryderbelserion.lexicon.core.builders.commands.args.types.IntArgument;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Collections;

public class CrateCommandHelp extends BukkitCommandEngine {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final BukkitCommandProvider provider;

    @SuppressWarnings("deprecation")
    public CrateCommandHelp(BukkitCommandProvider provider) {
        super("help", Permissions.PLAYER_HELP.getDescription(), "/crazycrates:help", Collections.emptyList());

        setPermission(Permissions.PLAYER_HELP.getBuiltPermission());

        setPermissionMessage(Messages.NO_PERMISSION.getMessage());

        addRequiredArgument(new Argument("page", 0, new IntArgument(ConfigManager.getLocale().getProperty(Locale.HELP_MAX_PAGE_ENTRIES))));

        this.provider = provider;
    }

    @Override
    public void perform(BukkitCommandContext context, String[] args) {
        this.provider.generateHelpCommand(this.plugin.getBukkit().getManager(), context).showHelp();

        //if (args.length == 2) {
        //    context.sendLegacyMessage(Messages.CORRECT_USAGE.getMessage().replaceAll("%usage%", this.usageMessage + " [player,admin]"));
        //    return;
        //}

        //if (args[0].equals("player")) {
        //    context.sendLegacyMessage(Messages.HELP.getMessage());
        //    return;
        //}

        //if (!context.hasPermission(Permissions.ADMIN_ACCESS.getBuiltPermission())) {
        //    context.sendLegacyMessage(Messages.NO_PERMISSION.getMessage());
        //    return;
        //}

        //if (args[0].equals("admin")) context.sendLegacyMessage(Messages.ADMIN_HELP.getMessage());
    }

    /*@Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        // args[1] = player/admin
        // args[2] = out of bounds so return empty list, don't need to duplicate a string argument.
        if (args.length == 2) return Collections.emptyList();

        ArrayList<String> completions = new ArrayList<>();

        completions.add("player");

        if (sender.hasPermission(Permissions.ADMIN_ACCESS.getBuiltPermission())) completions.add("admin");

        return completions;
    }*/
}