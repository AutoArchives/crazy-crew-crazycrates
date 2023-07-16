package com.badbones69.crazycrates.paper.api.v2.configs.types.legacy;

import com.badbones69.crazycrates.paper.api.v2.utils.MiscUtils;
import com.ryderbelserion.crazycrates.core.CrazyLogger;
import org.bukkit.configuration.Configuration;
import java.util.ArrayList;
import java.util.List;

public enum LocaleEnum {

    NO_TELEPORTING("No-Teleporting", "misc.no-teleporting"),
    NO_COMMANDS_WHILE_CRATE_OPENED("No-Commands-While-In-Crate", "misc.no-commands"),
    FEATURE_DISABLED("Feature-Disabled", "misc.feature-disabled"),
    NO_KEY("No-Key", "misc.no-keys"),
    NO_VIRTUAL_KEY("No-Virtual-Key", "misc.no-virtual-keys"),
    NOT_ON_BLOCK("Not-On-Block", "crates.not-on-block"),
    ALREADY_OPENING_CRATE("Already-Opening-Crate", "crates.already-open"),
    QUICK_CRATE_IN_USE("Quick-Crate-In-Use", "crates.in-use"),
    WORLD_DISABLED("World-Disabled", "crates.world-disabled"),
    RELOAD("Reload", "command.reload-plugin"),
    NOT_ONLINE("Not-Online", "general.player-checks.target-not-online"),
    NO_PERMISSION("No-Permission", "general.no-permission"),
    CANT_BE_A_VIRTUAL_CRATE("Cant-Be-A-Virtual-Crate", "crates.cannot-be-a-virtual-crate"),
    INVENTORY_FULL("Inventory-Full", "players.inventory-not-empty"),
    TO_CLOSE_TO_ANOTHER_PLAYER("To-Close-To-Another-Player", "players.too-close-to-another-player"),
    NEEDS_MORE_ROOM("Needs-More-Room", "crates.need-more-room"),
    OUT_OF_TIME("Out-Of-Time", "crates.out-of-time"),
    MUST_BE_A_PLAYER("Must-Be-A-Player", "general.player-checks.must-be-player"),
    MUST_BE_A_CONSOLE_SENDER("Must-Be-A-Console-Sender", "general.player-checks.must-be-console-sender"),
    MUST_BE_LOOKING_AT_A_BLOCK("Must-Be-Looking-At-A-Block", "general.player-checks.must-be-looking-at-block"),
    NOT_A_CRATE("Not-A-Crate", "crates.requirements.not-a-crate"),
    NOT_A_NUMBER("Not-A-Number", "crates.requirements.not-a-number"),
    GIVEN_A_PLAYER_KEYS("Given-A-Player-Keys", "command.give.given-player-keys"),
    CANNOT_GIVE_PLAYER_KEYS("Cannot-Give-Player-Keys", "command.give.cannot-give-player-keys"),
    OBTAINING_KEYS("Obtaining-Keys", "players.obtaining-keys"),
    GIVEN_EVERYONE_KEYS("Given-Everyone-Keys", "command.give.given-everyone-keys"),
    GIVEN_OFFLINE_PLAYER_KEYS("Given-Offline-Player-Keys", "command.give.given-offline-player-keys"),
    TAKE_A_PLAYER_KEYS("Take-A-Player-Keys", "command.take.take-player-keys"),
    TAKE_OFFLINE_PLAYER_KEYS("Take-Offline-Player-Keys", "command.take.take-offline-player-keys"),
    OPENED_A_CRATE("Opened-A-Crate", "command.open.opened-a-crate"),
    INTERNAL_ERROR("Internal-Error", "general.errors.internal-error"),
    UNKNOWN_COMMAND("Unknown-Command", "general.unknown-command"),
    NO_ITEM_IN_HAND("No-Item-In-Hand", "command.additem.no-item-hand"),
    ADDED_ITEM_WITH_EDITOR("Added-Item-With-Editor", "command.additem.add-item-from-hand"),
    CREATED_PHYSICAL_CRATE("Created-Physical-Crate", "crates.physical-crate.created", List.of(
            "&7You have set that block to &6{crate}.",
            "&7To remove &6{crate}, &7Shift Click Break in Creative to remove."
    )),
    REMOVED_PHYSICAL_CRATE("Removed-Physical-Crate", "crates.physical-crate.removed"),
    PERSONAL_NO_VIRTUAL_KEYS("Keys.Personal.No-Virtual-Keys", "command.keys.personal.no-virtual-keys"),
    PERSONAL_HEADER("Keys.Personal.Header", "command.keys.personal.header", List.of(
            "&8(&6!&8) &7A list of your current amount of keys."
    )),
    OTHER_PLAYER_NO_VIRTUAL_KEYS("Keys.Other-Player.No-Virtual-Keys", "command.keys.other-player.no-virtual-keys"),
    OTHER_PLAYER_HEADER("Keys.Other-Player.Header", "command.keys.other-player.header", List.of(
            "&8(&6!&8) &7A list of &c{player}''s &7current amount of keys."
    )),
    PER_CRATE("Keys.Per-Crate", "command.keys.crate-format"),
    PREVIEW_DISABLED("Preview-Disabled", "crates.preview-disabled"),
    NO_SCHEMATICS_FOUND("No-Schematics-Found", "general.errors.no-schematics-found"),
    NO_PRIZES_FOUND("No-Prizes-Found", "general.errors.no-prizes-found"),
    SAME_PLAYER("Same-Player", "players.target-same-player"),
    PRIZE_ERROR("Prize-Error", "errors.prize-error"),
    NOT_ENOUGH_KEYS("Transfer-Keys.Not-Enough-Keys", "command.transfer.not-enough-keys"),
    TRANSFERRED_KEYS("Transfer-Keys.Transferred-Keys", "command.transfer.transferred-keys"),
    RECEIVED_TRANSFERRED_KEYS("Transfer-Keys.Received-Transferred-Keys", "command.transfer.transferred-keys-received");

    private final String path;
    private final String newPath;
    private List<String> defaultList;

    LocaleEnum(String path, String newPath) {
        this.path = path;
        this.newPath = newPath;
    }

    LocaleEnum(String path, String newPath, List<String> defaultList) {
        this.path = path;
        this.newPath = newPath;

        this.defaultList = defaultList;
    }

    private String getLegacyPath() {
        return "Messages." + path;
    }

    private String getNewPath() {
        return newPath;
    }

    private boolean contains(Configuration reader, String value) {
        return reader.contains(value);
    }

    public void setMessage(Configuration configuration) {
        try {
            if (contains(configuration, getLegacyPath())) {
                if (configuration.isList(getLegacyPath())) {
                    List<String> legacy = configuration.getStringList(getLegacyPath());

                    if (legacy.isEmpty()) {
                        configuration.set(getNewPath(), this.defaultList);
                        return;
                    }

                    ArrayList<String> newList = new ArrayList<>();

                    legacy.forEach(line -> newList.add(MiscUtils.convertLegacyPlaceholders(line)));

                    configuration.set(getNewPath(), newList);
                } else {
                    configuration.set(getNewPath(), MiscUtils.convertLegacyPlaceholders(getValue(configuration)));
                }
            }
        } catch (Exception exception) {
            CrazyLogger.warn("Failed to set: " + getNewPath());
            CrazyLogger.warn("Tried to use: " + getLegacyPath());
        }
    }

    public String getValue(Configuration configuration) {
        if (configuration.contains(getLegacyPath())) return configuration.getString(getLegacyPath());

        return "&cYou need to edit this message @ &e" + getNewPath() + ".";
    }
}