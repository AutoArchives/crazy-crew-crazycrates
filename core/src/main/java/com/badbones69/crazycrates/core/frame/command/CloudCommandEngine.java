package com.badbones69.crazycrates.core.frame.command;

import cloud.commandframework.context.CommandContext;
import cloud.commandframework.minecraft.extras.RichDescription;
import com.badbones69.crazycrates.core.frame.utils.AdventureUtils;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

public abstract class CloudCommandEngine {

    public abstract void registerCommand();

    //TODO() I don't think this will work with cloud, the way the command tree in cloud is made. I can delete all commands but individual ones seems iffy.
    //public abstract void unregisterCommand();

    protected abstract void perform(@NotNull CommandContext<@NotNull Sender> context);

    protected static @NotNull RichDescription description(@NotNull String description, @NotNull TagResolver.@NotNull Single... placeholders) {
        return RichDescription.of(AdventureUtils.parse(description, placeholders));
    }
}