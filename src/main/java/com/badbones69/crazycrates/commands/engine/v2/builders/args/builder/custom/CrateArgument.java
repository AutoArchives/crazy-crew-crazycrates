package com.badbones69.crazycrates.commands.engine.v2.builders.args.builder.custom;

import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.commands.engine.v2.builders.args.ArgumentType;
import com.badbones69.crazycrates.api.objects.Crate;
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