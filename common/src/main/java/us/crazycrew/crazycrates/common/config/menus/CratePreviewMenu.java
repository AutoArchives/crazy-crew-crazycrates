package us.crazycrew.crazycrates.common.config.menus;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import ch.jalu.configme.properties.PropertyInitializer;
import java.util.List;

public class CratePreviewMenu implements SettingsHolder {

    @Comment({"""
            The title of the menu.
            Available Placeholders: %crate%
            """})
    public static final Property<String> CRATE_PREVIEW_TITLE = PropertyInitializer.newProperty("settings.crate-menu.title","<red>Crate Preview</red> <dark_gray>:</dark_gray> <reset>%crate%</reset>");

    @Comment("The material the main menu button will be.")
    public static final Property<String> CRATE_PREVIEW_MENU_BUTTON_MATERIAL = PropertyInitializer.newProperty("settings.preview-buttons.menu-button.material","COMPASS");

    @Comment("The name of the main menu button.")
    public static final Property<String> CRATE_PREVIEW_MENU_BUTTON_NAME = PropertyInitializer.newProperty("settings.preview-buttons.menu-button.name","<gray><bold>»»</bold></gray> <red><bold>Menu</bold></red> <gray><bold>««</bold></gray>");

    @Comment("The lore for the main menu button.")
    public static final Property<List<String>> CRATE_PREVIEW_MENU_BUTTON_LORE = PropertyInitializer.newListProperty("settings.preview-buttons.menu-button.lore", "");

    @Comment("The material the next button will be.")
    public static final Property<String> CRATE_PREVIEW_NEXT_BUTTON_MATERIAL = PropertyInitializer.newProperty("settings.preview-buttons.next-button.material","FEATHER");

    @Comment("The name of the next button.")
    public static final Property<String> CRATE_PREVIEW_NEXT_BUTTON_NAME = PropertyInitializer.newProperty("settings.preview-buttons.next-button.name","<orange><bold>Next »</bold></orange>");

    @Comment("The lore for the next button.")
    public static final Property<List<String>> CRATE_PREVIEW_NEXT_BUTTON_LORE = PropertyInitializer.newListProperty("settings.preview-buttons.next-button.lore", "");

    @Comment("The material the back button will be.")
    public static final Property<String> CRATE_PREVIEW_BACK_BUTTON_MATERIAL = PropertyInitializer.newProperty("settings.preview-buttons.back-button.material","FEATHER");

    @Comment("The name of the Back button.")
    public static final Property<String> CRATE_PREVIEW_BACK_BUTTON_NAME = PropertyInitializer.newProperty("settings.preview-buttons.back-button.name","<orange><bold>« Back</bold></orange>");

    @Comment("The lore for the back button.")
    public static final Property<List<String>> CRATE_PREVIEW_BACK_BUTTON_LORE = PropertyInitializer.newListProperty("settings.preview-buttons.back-button.lore", "");
}