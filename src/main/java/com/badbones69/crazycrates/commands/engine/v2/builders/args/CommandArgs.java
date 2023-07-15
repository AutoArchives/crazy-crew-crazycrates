package com.badbones69.crazycrates.commands.engine.v2.builders.args;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface CommandArgs {

    int getArgAsInt(int index, boolean notifySender);

    long getArgAsLong(int index, boolean notifySender);

    double getArgAsDouble(int index, boolean notifySender);

    boolean getArgAsBoolean(int index, boolean notifySender);

    float getArgAsFloat(int index, boolean notifySender);

    Player getArgAsPlayer(int index, boolean notifySender);

    OfflinePlayer getArgAsOfflinePlayer(int index);

}