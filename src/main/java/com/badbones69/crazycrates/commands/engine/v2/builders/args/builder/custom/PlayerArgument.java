package com.badbones69.crazycrates.commands.engine.v2.builders.args.builder.custom;

import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.engine.v2.builders.args.ArgumentType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerArgument extends ArgumentType {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    @Override
    public List<String> getPossibleValues() {
        return this.plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }
}