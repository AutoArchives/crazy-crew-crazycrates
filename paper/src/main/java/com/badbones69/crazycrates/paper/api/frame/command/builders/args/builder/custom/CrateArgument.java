package com.badbones69.crazycrates.paper.api.frame.command.builders.args.builder.custom;

import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.api.frame.command.builders.args.ArgumentType;
import com.badbones69.crazycrates.paper.api.objects.Crate;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;
import java.util.stream.Collectors;

public class CrateArgument extends ArgumentType {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    @Override
    public List<String> getPossibleValues() {
        return this.plugin.crazyManager().getCrateManager().getCrates().stream().map(Crate::getCrateName).collect(Collectors.toList());
    }
}