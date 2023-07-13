package com.badbones69.crazycrates.commands.engine.v2.message.enums;

public enum MessageKeys {

    UNKNOWN_COMMAND,
    INVALID_SYNTAX,
    NO_PERMISSION,
    REQUIRED_ARG,
    OPTIONAL_ARG,
    NOT_ENOUGH_ARGS,
    TOO_MANY_ARGS,
    INTERNAL_ERROR,
    PLAYER_NOT_FOUND,
    CANNOT_USE_COMMAND_ON_YOURSELF,
    MUST_BE_LOOKING_AT_BLOCK,
    MUST_BE_CONSOLE,
    MUST_BE_PLAYER,
    GRABBING_OFFLINE_PLAYER;

    /*private final MessageKey key;

    MessageKeys() {
        this.key = MessageKey.of("crazycrates." + name().toLowerCase(Locale.ENGLISH));
    }

    public MessageKey getKey() {
        return this.key;
    }*/
}