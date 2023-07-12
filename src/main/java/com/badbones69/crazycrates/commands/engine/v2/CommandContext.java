package com.badbones69.crazycrates.commands.engine.v2;

import com.badbones69.crazycrates.commands.engine.v2.builders.CommandActor;
import com.ryderbelserion.stick.core.utils.AdventureUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandContext implements CommandActor {

    private final Audience audience;
    private final ArrayList<String> args;

    public CommandContext(Audience audience, ArrayList<String> args) {
        this.audience = audience;
        this.args = args;
    }

    @Override
    public void reply(String message) {
        if (message.isBlank() || message.isEmpty()) return;

        Component component = AdventureUtils.parse(message);

        this.audience.sendMessage(component);
    }

    @Override
    public void reply(boolean hasPrefix, String prefix, String message) {
        if (message.isBlank() || message.isEmpty()) return;

        if (hasPrefix) {
            Component component = AdventureUtils.parse(prefix).append(AdventureUtils.parse(prefix));

            this.audience.sendMessage(component);

            return;
        }

        Component component = AdventureUtils.parse(message);

        this.audience.sendMessage(component);
    }

    @Override
    public void reply(boolean hasPrefix, String prefix, Component component) {
        if (hasPrefix) {
            this.audience.sendMessage(AdventureUtils.parse(prefix).append(component));
            return;
        }

        this.audience.sendMessage(component);
    }

    @Override
    public void reply(Component component) {
        this.audience.sendMessage(component);
    }

    @Override
    public void send(Audience audience, String message) {
        if (message.isBlank() || message.isEmpty()) return;

        Component component = AdventureUtils.parse(message);

        audience.sendMessage(component);
    }

    @Override
    public void send(Audience audience, Component component) {
        audience.sendMessage(component);
    }

    @Override
    public void send(Audience audience, String message, String prefix, boolean hasPrefix) {
        if (hasPrefix) {
            audience.sendMessage(AdventureUtils.parse(prefix).append(AdventureUtils.parse(message)));
            return;
        }

        send(audience, message);
    }

    @Override
    public void send(Audience audience, Component message, String prefix, boolean hasPrefix) {
        if (hasPrefix) {
            audience.sendMessage(AdventureUtils.parse(prefix).append(message));
            return;
        }

        send(audience, message);
    }

    public List<String> getArgs() {
        return Collections.unmodifiableList(this.args);
    }
}