package us.crazycrew.crazycrates.common.enums;

import ch.jalu.configme.SettingsManager;
import us.crazycrew.crazycrates.common.config.ConfigManager;

public class MessagesHandler {

    private final ConfigManager configManager;
    private final SettingsManager messages;

    public MessagesHandler(ConfigManager configManager) {
        this.configManager = configManager;

        this.messages = this.configManager.getMessages();
    }
}