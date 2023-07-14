package com.badbones69.crazycrates.commands.engine.v2.example;

import com.badbones69.crazycrates.commands.engine.v2.CommandContext;
import com.badbones69.crazycrates.commands.engine.v2.CommandEngine;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FirstCommand extends CommandEngine {

    public FirstCommand() {
        super("first", "example command", Collections.emptyList());

        //CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
        //String message = plugin.getApiManager().getPlaceholderSupport().setPlaceholders(plugin.getApiManager().getLocale().getProperty(Locale.NO_PERMISSION));

        //updatePermissionMessage(message);
    }

    @Override
    protected void perform(CommandContext context, String[] args) {
        if (args[0].isEmpty() || args[0].isBlank()) {
            context.reply("No material provided.");
            return;
        }

        String material = args[0];

        Material fetched = Material.matchMaterial(material);

        if (fetched != null) {
            context.reply(fetched.name());
            return;
        }

        context.reply(material + " is not a valid material!");

        //this.plugin.getManager().generateCommandHelp(context).showHelp();
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            List<String> numbers = new ArrayList<>();

            for (int value = 1; value <= 100; value++) numbers.add(String.valueOf(value));

            return numbers;
        }

        return new ArrayList<>();
    }
}