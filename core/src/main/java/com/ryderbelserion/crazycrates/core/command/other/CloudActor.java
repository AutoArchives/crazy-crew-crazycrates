package com.ryderbelserion.crazycrates.core.command.other;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public interface CloudActor {

    void reply(Audience audience, String message);

    void reply(Audience audience, Component component);

}