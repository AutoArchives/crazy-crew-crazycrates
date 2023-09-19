package us.crazycrew.crazycrates.common.config.types;

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
    public static final Property<String> plugin_locale = PropertyInitializer.newProperty("plugin_locale", "en-US");

    @Comment("Whether you want CrazyCrates to shut up or not, This option is ignored by errors.")
    public static final Property<Boolean> verbose_logging = PropertyInitializer.newProperty("verbose_logging", true);

    @Comment({
            "Sends anonymous statistics about how the plugin is used to bstats.org.",
            "bstats is a service for plugin developers to find out how the plugin being used,",
            "This information helps us figure out how to better improve the plugin."
    })
    public static final Property<Boolean> toggle_metrics = PropertyInitializer.newProperty("toggle_metrics", true);

    @Comment("The command prefix you want shown in front of commands!")
    public static final Property<String> command_prefix = PropertyInitializer.newProperty("command_prefix", "<red>[CrazyCrates]</red> ");

    @Comment("The console prefix you want shown when the logging messages show up!")
    public static final Property<String> console_prefix = PropertyInitializer.newProperty("console_prefix" ,"<dark_purple>[CrazyCrates]</dark_purple>");

    @Comment({
            "A temporary option to allow you to opt in for using MiniMessage.",
            "",
            "MiniMessage allows you to use gradients as well as hex colors,",
            "&7 or &c will no longer work when you set this option to true.",
            "",
            "Please read up on MiniMessage and how to use it below,",
            "https://docs.advntr.dev/minimessage/format.html#color",
            "",
            "This option will only be here until v2.1 then you will be forced to use MiniMessage,",
            "It is only added as an option to give you time to convert your configurations."
    })
    public static final Property<Boolean> use_mini_message = PropertyInitializer.newProperty("use_minimessage", true);

}