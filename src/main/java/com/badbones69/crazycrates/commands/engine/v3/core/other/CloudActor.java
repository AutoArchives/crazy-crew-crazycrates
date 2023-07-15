package com.badbones69.crazycrates.commands.engine.v3.core.other;

import net.kyori.adventure.text.Component;

public interface CloudActor {

    void reply(String message);

    void reply(Component component);

}