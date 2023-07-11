package com.badbones69.crazycrates.api.configs.types;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import org.bukkit.configuration.file.YamlConfiguration;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class PluginConfig extends YamlConfiguration implements SettingsHolder {

    public PluginConfig() {}

    @Override
    public void registerComments(CommentsConfiguration conf) {
        String[] header = {
                "Github: https://github.com/Crazy-Crew",
                "",
                "Issues: https://github.com/Crazy-Crew/CrazyCrates/issues",
                "Features: https://github.com/Crazy-Crew/CrazyCrates/discussions/categories/features",
                "",
                "Legacy color codes such as &7,&c no longer work. You must use MiniMessage",
                "https://docs.advntr.dev/minimessage/format.html#color"
        };

        String[] deprecation = {
                "",
                "Warning: This section is subject to change so it is considered deprecated.",
                "This is your warning before the change happens.",
                ""
        };

        conf.setComment("settings", header);
    }

    @Comment({
            "Choose what language you want the plugin to be in.",
            "",
            "Available Languages: en-US"
    })
    public static final Property<String> LOCALE_FILE = newProperty("language", "en-US");

    @Comment("How many commands should be displayed per page in /crazycrates help?")
    public static final Property<Integer> MAX_HELP_PAGE_ENTRIES = newProperty("help.max-help-page-entries", 10);

    public static final Property<String> INVALID_HELP_PAGE = newProperty("help.invalid-page", "{prefix}<red>The page</red> <gold>{page}</gold> <red>does not exist.</red>");

    public static final Property<String> HELP_PAGE_FORMAT = newProperty("help.page-format", "<gold>{command}</gold> <dark_gray>»</dark_gray> <reset>{description}");

    public static final Property<String> HELP_PAGE_HEADER = newProperty("help.header", "<dark_gray>────────</dark_gray> <gold>CrazyCrates Help {page}</gold> <dark_gray>────────</dark_gray>");

    public static final Property<String> HELP_PAGE_FOOTER = newProperty("help.footer", "<dark_gray>────────</dark_gray> <gold>CrazyCrates Help {page}");

    @Comment({
            "The only options that work here are run_command, suggest_command, copy_to_clipboard",
            "",
            "Warning: They are case-sensitive names so type them exactly as given above!",
            "",
            "This is what happens if you click the command in the /crazycrates help menu."
    })
    public static final Property<String> HELP_PAGE_HOVER_ACTION = newProperty("help.hover.action", "copy_to_clipboard");

    public static final Property<String> HELP_PAGE_HOVER_FORMAT = newProperty("help.hover.format", "{prefix}<gray>Click me to run the command.</gray> <gold>{commands}</gold>");

    public static final Property<String> HELP_PAGE_NEXT = newProperty("help.page-next", " <green>»»»</green>");

    public static final Property<String> HELP_PAGE_BACK = newProperty("help.page-back", " <red>«««</red>");

    public static final Property<String> HELP_PAGE_GO_TO_PAGE = newProperty("help.go-to-page", "<gray>Go to page</gray> <gold>{page}</gold>");

    @Comment("The command prefix that is shown at the beginning of every message.")
    public static final Property<String> COMMAND_PREFIX = newProperty("commands.prefix.value", "<blue>[</blue><dark_aqua>CrazyCrates</dark_aqua><blue>]</blue> <reset>");

    @Comment("Whether you want to have verbose logging enabled.")
    public static final Property<Boolean> VERBOSE_LOGGING = newProperty("verbose-logging", true);

    @Comment("Should auto save be enabled?")
    public static final Property<Boolean> AUTO_SAVE_TOGGLE = newProperty("auto-save.toggle", true);

    @Comment("How long in between auto save intervals? Value is in milliseconds! 300,000 = 5 minutes")
    public static final Property<Integer> AUTO_SAVE_TIME = newProperty("auto-save.time", 300000);

    @Comment("Whether you want statistics sent to https://bstats.org.")
    public static final Property<Boolean> TOGGLE_METRICS = newProperty("toggle-metrics", true);

    //@Comment({
    //        "Available Values: json / yaml",
    //        "This is only related to how user data is stored!",
    //        "Defaults to users.yml"
    //})
    //public static final Property<DataSupport> DATA_TYPE = newProperty(DataSupport.class, "data.type", DataSupport.yaml);
}