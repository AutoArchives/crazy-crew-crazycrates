package com.badbones69.crazycrates.commands.v2.args;

import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.objects.Crate;
import com.ryderbelserion.stick.paper.commands.sender.args.ArgumentType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.Collectors;

public class CrateArgument extends ArgumentType {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    @Override
    public List<String> getPossibleValues() {
        return this.plugin.getApiManager().getCrateManager().getCrates().stream().map(Crate::getCrateName).collect(Collectors.toList());
    }
}