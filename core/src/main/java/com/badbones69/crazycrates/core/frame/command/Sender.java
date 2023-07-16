package com.badbones69.crazycrates.core.frame.command;

import com.badbones69.crazycrates.core.frame.CrazyCore;
import com.badbones69.crazycrates.core.frame.utils.AdventureUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

/**
 * https://github.com/BillyGalbreath/Pl3xMap/blob/v3/LICENSE
 *
 * @author BillyGalbreath
 */
public abstract class Sender implements ForwardingAudience.Single {

    private final Object sender;

    public <T> Sender(@NotNull T sender) {
        this.sender = sender;
    }

    @SuppressWarnings("unchecked")
    public <T> @NotNull T getSender() {
        return (T) this.sender;
    }

    @Override
    public @NotNull Audience audience() {
        return CrazyCore.api().adventure().console();
    }

    public void sendMessage(String message) {
        sendMessage(message, true);
    }

    public void sendMessage(String message, boolean prefix) {
        for (String part : message.split("\n")) {
            sendMessage(prefix, AdventureUtils.parse(part));
        }
    }

    public void sendMessage(boolean prefix, @NotNull ComponentLike message) {
        audience().sendMessage(prefix ? AdventureUtils.parse(CrazyCore.api().getPrefix()).append(message) : message);
    }

    @Override
    public abstract boolean equals(@Nullable Object o);

    @Override
    public abstract int hashCode();

    @Override
    public abstract @NotNull String toString();

    public interface Player<T> {
        @NotNull T getPlayer();

        @NotNull UUID getUUID();
    }
}