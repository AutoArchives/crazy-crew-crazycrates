package com.badbones69.crazycrates.paper.commands.engine.paper;

import com.ryderbelserion.stick.core.registry.keys.Key;
import com.ryderbelserion.stick.core.registry.senders.types.Sender;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BukkitCloudCommandSender extends Sender {

    public static @NotNull Sender create(@NotNull CommandSender sender) {
        return new BukkitCloudCommandSender(sender);
    }

    public BukkitCloudCommandSender(CommandSender sender) {
        super(sender);
    }

    @Override
    public void send(boolean hasPrefix, String prefix, @NotNull ComponentLike message) {

    }

    @Override
    public boolean equals(@Nullable Object instance) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return null;
    }
}