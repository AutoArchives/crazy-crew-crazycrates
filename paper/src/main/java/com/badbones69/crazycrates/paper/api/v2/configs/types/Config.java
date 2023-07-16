package com.badbones69.crazycrates.paper.api.v2.configs.types;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import java.util.List;
import static ch.jalu.configme.properties.PropertyInitializer.newListProperty;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class Config implements SettingsHolder {

    public Config() {}

    private static final String settings = "Settings.";
    
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

    public static final Property<Boolean> ENABLE_CRATE_MENU = newProperty(settings + "Enable-Crate-Menu", true);

    @Comment({
            "Warning: The size of this file can crash your server.",
            "It is recommended that you delete the file occasionally or not use it at all."
    })
    public static final Property<Boolean> LOG_CRATE_ACTIONS_FILE = newProperty(settings + "Crate-Actions.Log-File", false);

    @Comment("This is the recommended way to log crate actions as it already is saved to a logs file.")
    public static final Property<Boolean> LOG_CRATE_ACTIONS_CONSOLE = newProperty(settings + "Crate-Actions.Log-Console", true);

    @Comment("The name of the `/cc` inventory.")
    public static final Property<String> INVENTORY_NAME = newProperty(settings + "InventoryName", "<blue>Crazy <red>Crates</red>");

    @Comment("The size of the `/cc` inventory.")
    public static final Property<Integer> INVENTORY_SIZE = newProperty(settings + "InventorySize", 45);

    @Comment("Whether you want crates to knock you back if you don't have a key.")
    public static final Property<Boolean> CRATE_KNOCK_BACK = newProperty(settings + "KnockBack", true);

    @Comment("If the physical crates should accept virtual keys.")
    public static final Property<Boolean> PHYSICAL_ACCEPTS_VIRTUAL_KEYS = newProperty(settings + "Physical-Accepts-Virtual-Keys", true);

    @Comment("If the physical crates should accept physical keys.")
    public static final Property<Boolean> PHYSICAL_ACCEPTS_PHYSICAL_KEYS = newProperty(settings + "Physical-Accepts-Physical-Keys", true);

    @Comment("If the virtual crates should accept a physical key.")
    public static final Property<Boolean> VIRTUAL_ACCEPTS_PHYSICAL_KEYS = newProperty(settings + "Virtual-Accepts-Physical-Keys", true);

    @Comment("Whether to give a virtual key if their inventory is full when receiving a physical key")
    public static final Property<Boolean> GIVE_VIRTUAL_KEYS_WHEN_INVENTORY_FULL = newProperty(settings + "Give-Virtual-Keys-When-Inventory-Full", false);

    @Comment("Whether to send a message if they were given a virtual key if inventory is not empty.")
    public static final Property<Boolean> GIVE_VIRTUAL_KEYS_WHEN_INVENTORY_FULL_MESSAGE = newProperty(settings + "Give-Virtual-Keys-When-Inventory-Full-Message", false);

    @Comment("Whether to play the sound or not.")
    public static final Property<Boolean> NEED_KEY_SOUND_TOGGLE = newProperty(settings + "Need-Key-Sound-Toggle", true);

    @Comment("The sound that plays when a player tries to open a crate with no key.")
    public static final Property<String> NEED_KEY_SOUND = newProperty(settings + "Need-Key-Sound", "ENTITY_VILLAGER_NO");

    @Comment("How long the quad crate should stay open for in seconds.")
    public static final Property<Integer> QUAD_CRATE_TIMER = newProperty(settings + "QuadCrate.Timer", 300);

    @Comment("Which worlds do you want crates to be disabled in?")
    public static final Property<List<String>> DISABLED_WORLDS = newListProperty(settings + "DisabledWorlds", List.of(
            "world_nether"
    ));

    @Comment("The material of the menu button.")
    public static final Property<String> MENU_ITEM = newProperty(settings + "Preview.Buttons.Menu.Item", "COMPASS");

    @Comment("The name of the menu button.")
    public static final Property<String> MENU_NAME = newProperty(settings + "Preview.Buttons.Menu.Name", "&7&l>> &c&lMenu &7&l<<");

    @Comment("The lore of the menu button.")
    public static final Property<List<String>> MENU_LORE = newListProperty(settings + "Preview.Buttons.Menu.Lore", List.of(
            "&7Return to the menu."
    ));

    @Comment("The material of the next button.")
    public static final Property<String> NEXT_ITEM = newProperty(settings + "Preview.Buttons.Next.Item", "FEATHER");

    @Comment("The name of the next button.")
    public static final Property<String> NEXT_NAME = newProperty(settings + "Preview.Buttons.Next.Name", "&6&lNext >>");

    @Comment("The lore of the next button.")
    public static final Property<List<String>> NEXT_LORE = newListProperty(settings + "Preview.Buttons.Next.Lore", List.of(
            "&7&lPage: &b%page%"
    ));

    @Comment("The material of the back button.")
    public static final Property<String> BACK_ITEM = newProperty(settings + "Preview.Buttons.Back.Item", "FEATHER");

    @Comment("The name of the back button.")
    public static final Property<String> BACK_NAME = newProperty(settings + "Preview.Buttons.Back.Name", "&6&l<< Back");

    @Comment("The lore of the back button.")
    public static final Property<List<String>> BACK_LORE = newListProperty(settings + "Preview.Buttons.Back.Lore", "&7&lPage: &b%page%");

    @Comment("Whether to the menu should be filled with items or not.")
    public static final Property<Boolean> FILLER_TOGGLE = newProperty(settings + "Filler.Toggle", false);

    @Comment("The item type you want to use.")
    public static final Property<String> FILLER_ITEM = newProperty(settings + "Filler.Item", "BLACK_STAINED_GLASS_PANE");

    @Comment("The name of the item.")
    public static final Property<String> FILLER_NAME = newProperty(settings + "Filler.Name", " ");

    @Comment("The lore of the item.")
    public static final Property<List<String>> FILLER_LORE = newListProperty(settings + "Filler.Lore", List.of());

    @Comment("Whether you want the customizer to be enabled.")
    public static final Property<Boolean> CUSTOMIZER_TOGGLE = newProperty(settings + "GUI-Customizer-Toggle", true);

    @Comment("Place any fancy item in the gui including custom items.")
    public static final Property<List<String>> CUSTOMIZER = newListProperty(settings + "GUI-Customizer", List.of(
            "Slot:1, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:2, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:3, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:4, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:5, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:6, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:7, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:8, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:9, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:10, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:18, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:19, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:27, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:28, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:36, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:37, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:45, Item:BLACK_STAINED_GLASS_PANE, Name: ",

            "Slot:11, Item:WHITE_STAINED_GLASS_PANE, Name: ",
            "Slot:12, Item:WHITE_STAINED_GLASS_PANE, Name: ",
            "Slot:13, Item:WHITE_STAINED_GLASS_PANE, Name: ",
            "Slot:14, Item:WHITE_STAINED_GLASS_PANE, Name: ",
            "Slot:15, Item:WHITE_STAINED_GLASS_PANE, Name: ",
            "Slot:16, Item:WHITE_STAINED_GLASS_PANE, Name: ",
            "Slot:17, Item:WHITE_STAINED_GLASS_PANE, Name: ",
            "Slot:20, Item:WHITE_STAINED_GLASS_PANE, Name: ",
            "Slot:22, Item:WHITE_STAINED_GLASS_PANE, Name: ",
            "Slot:24, Item:WHITE_STAINED_GLASS_PANE, Name: ",
            "Slot:30, Item:WHITE_STAINED_GLASS_PANE, Name: ",
            "Slot:33, Item:WHITE_STAINED_GLASS_PANE, Name: ",
            "Slot:34, Item:WHITE_STAINED_GLASS_PANE, Name: ",
            "Slot:35, Item:WHITE_STAINED_GLASS_PANE, Name: ",

            "Slot:38, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:39, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:40, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:41, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:42, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:43, Item:BLACK_STAINED_GLASS_PANE, Name: ",
            "Slot:44, Item:BLACK_STAINED_GLASS_PANE, Name: "));
}