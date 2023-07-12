package com.badbones69.crazycrates.commands.engine.v2.builders;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

public interface CommandActor {

    void reply(String message);

    void reply(boolean hasPrefix, String prefix, String message);

    void reply(boolean hasPrefix, String prefix, Component component);

    void reply(Component component);

    void send(Audience audience, String message);

    void send(Audience audience, Component component);

    void send(Audience audience, String message, String prefix, boolean hasPrefix);

    void send(Audience audience, Component message, String prefix, boolean hasPrefix);

    //boolean hasPermission(Permission permission);

    //boolean hasPermission(String rawPermission);

    //CommandSender getSender();

    //boolean isPlayer();

    //Player getPlayer();

    //void setAlias(String alias);

    //String getAlias();

    //List<String> getArgs();

    //void removeArgs(int arg);

    //CommandHelpEntry getHelpEntry();

}