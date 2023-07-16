package com.badbones69.crazycrates.core.config.types;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import ch.jalu.configme.properties.PropertyInitializer;

public class PluginConfig implements SettingsHolder {

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
    public static final Property<String> LOCALE_FILE = PropertyInitializer.newProperty("language", "en-US");

    @Comment("How many commands should be displayed per page in /crazycrates help?")
    public static final Property<Integer> MAX_HELP_PAGE_ENTRIES = PropertyInitializer.newProperty("help.max-help-page-entries", 10);

    public static final Property<String> INVALID_HELP_PAGE = PropertyInitializer.newProperty("help.invalid-page", "{prefix}<red>The page</red> <gold>{page}</gold> <red>does not exist.</red>");

    public static final Property<String> HELP_PAGE_FORMAT = PropertyInitializer.newProperty("help.page-format", "<gold>{command}</gold> <dark_gray>»</dark_gray> <reset>{description}");

    public static final Property<String> HELP_PAGE_HEADER = PropertyInitializer.newProperty("help.header", "<dark_gray>────────</dark_gray> <gold>CrazyCrates Help {page}</gold> <dark_gray>────────</dark_gray>");

    public static final Property<String> HELP_PAGE_FOOTER = PropertyInitializer.newProperty("help.footer", "<dark_gray>────────</dark_gray> <gold>CrazyCrates Help {page}");

    @Comment({
            "The only options that work here are run_command, suggest_command, copy_to_clipboard",
            "",
            "Warning: They are case-sensitive names so type them exactly as given above!",
            "",
            "This is what happens if you click the command in the /crazycrates help menu."
    })
    public static final Property<String> HELP_PAGE_HOVER_ACTION = PropertyInitializer.newProperty("help.hover.action", "copy_to_clipboard");

    public static final Property<String> HELP_PAGE_HOVER_FORMAT = PropertyInitializer.newProperty("help.hover.format", "{prefix}<gray>Click me to run the command.</gray> <gold>{commands}</gold>");

    public static final Property<String> HELP_PAGE_NEXT = PropertyInitializer.newProperty("help.page-next", " <green>»»»</green>");

    public static final Property<String> HELP_PAGE_BACK = PropertyInitializer.newProperty("help.page-back", " <red>«««</red>");

    public static final Property<String> HELP_PAGE_GO_TO_PAGE = PropertyInitializer.newProperty("help.go-to-page", "<gray>Go to page</gray> <gold>{page}</gold>");

    @Comment("The command prefix that is shown at the beginning of every message.")
    public static final Property<String> COMMAND_PREFIX = PropertyInitializer.newProperty("commands.prefix.command-value", "<blue>[</blue><dark_aqua>CrazyCrates</dark_aqua><blue>]</blue> <reset>");

    @Comment("The prefix that is shown for messages sent in console such as logging messages.")
    public static final Property<String> CONSOLE_PREFIX = PropertyInitializer.newProperty("commands.prefix.console-value", "<gradient:#fe5f55:#6b55b5>[CrazyCrates]</gradient> ");

    @Comment("Whether you want to have verbose logging enabled.")
    public static final Property<Boolean> VERBOSE_LOGGING = PropertyInitializer.newProperty("verbose-logging", true);

    @Comment("Should auto save be enabled?")
    public static final Property<Boolean> AUTO_SAVE_TOGGLE = PropertyInitializer.newProperty("auto-save.toggle", true);

    @Comment("How long in between auto save intervals? Value is in milliseconds! 300,000 = 5 minutes")
    public static final Property<Integer> AUTO_SAVE_TIME = PropertyInitializer.newProperty("auto-save.time", 300000);

    @Comment("Whether you want statistics sent to https://bstats.org.")
    public static final Property<Boolean> TOGGLE_METRICS = PropertyInitializer.newProperty("toggle-metrics", true);

    //@Comment({
    //        "Available Values: json / yaml",
    //        "This is only related to how user data is stored!",
    //        "Defaults to users.yml"
    //})
    //public static final Property<DataSupport> DATA_TYPE = newProperty(DataSupport.class, "data.type", DataSupport.yaml);
}