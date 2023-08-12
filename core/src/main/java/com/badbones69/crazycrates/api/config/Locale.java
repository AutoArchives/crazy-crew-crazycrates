package com.badbones69.crazycrates.api.config;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import ch.jalu.configme.properties.PropertyInitializer;

public class Locale implements SettingsHolder {

    public Locale() {}

    @Override
    public void registerComments(CommentsConfiguration conf) {
        String[] header = {
                "Legacy color codes such as &7,&c no longer work. You must use minimessage",
                "https://docs.advntr.dev/minimessage/format.html#color"
        };

        String[] deprecation = {
                "",
                "Warning: This section is subject to change so it is considered deprecated.",
                "This is your warning before the change happens.",
                ""
        };

        conf.setComment("commands", header);
    }

    private static final String path = "commands.";

    public static final Property<String> REQUIRED_ARGUMENT = PropertyInitializer.newProperty(path + "required-argument", "{prefix}<red>This argument is not optional</red>");

    public static final Property<String> OPTIONAL_ARGUMENT = PropertyInitializer.newProperty(path + "optional-argument", "{prefix}<green>This argument is optional</green>");

    public static final Property<String> NOT_ENOUGH_ARGS = PropertyInitializer.newProperty(path + "not-enough-args", "{prefix}<red>You did not supply enough arguments.</red>");

    public static final Property<String> TOO_MANY_ARGS = PropertyInitializer.newProperty(path + "too-many-args", "{prefix}<red>You put more arguments then I can handle.</red>");
}