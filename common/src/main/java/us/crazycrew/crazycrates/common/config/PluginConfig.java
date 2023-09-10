package us.crazycrew.crazycrates.common.config;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import ch.jalu.configme.properties.PropertyInitializer;

public class PluginConfig implements SettingsHolder {

    protected PluginConfig() {}

    @Comment({
            "Choose the language you prefer to use on your server!",
            "",
            "Currently Available:",
            " > en-US ( English )",
            "",
            "If you do not see your language above, You can contribute by modifying the current en-US.yml",
            "https://github.com/Crazy-Crew/CrazyCrates/blob/main/paper/src/main/resources/locale/en-US.yml",
            "Submit your finalized config using https://bin.bloom.host/ and send it to us in https://discord.gg/badbones-s-live-chat-182615261403283459",
            ""
    })
    public static final Property<String> PLUGIN_LOCALE = PropertyInitializer.newProperty("plugin_locale", "en-US");

    @Comment({
            "Sends anonymous statistics about how the plugin is used to bstats.org.",
            "bstats is a service for plugin developers to find out how the plugin being used,",
            "This information helps us figure out how to better improve the plugin."
    })
    public static final Property<Boolean> TOGGLE_METRICS = PropertyInitializer.newProperty("toggle_metrics", true);

    @Comment("The command prefix you want shown in front of commands!")
    public static final Property<String> COMMAND_PREFIX = PropertyInitializer.newProperty("command_prefix", "<red>[CrazyCrates]</red> ");

}