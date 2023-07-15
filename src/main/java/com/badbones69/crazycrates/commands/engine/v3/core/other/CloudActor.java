package com.badbones69.crazycrates.commands.engine.v3.core.other;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public interface CloudActor {

    void reply(Audience audience, String message);

    void reply(Audience audience, Component component);

}