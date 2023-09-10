package us.crazycrew.crazycrates.common.config.menus;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import ch.jalu.configme.properties.PropertyInitializer;
import java.util.List;

public class CrateMainMenu implements SettingsHolder {

    protected CrateMainMenu() {}

    @Comment("If the menu should be enabled or not.")
    public static final Property<Boolean> CRATE_MENU_TOGGLE = PropertyInitializer.newProperty("settings.crate-menu.toggle", true);

    @Comment({"""
            The title of /crates menu.
            Available Placeholders: %crate%
            """})
    public static final Property<String> CRATE_MENU_TITLE = PropertyInitializer.newProperty("settings.crate-menu.title","<red>Crate Preview</red> <dark_gray>:</dark_gray> <reset>%crate%");

    @Comment({"""
            Choose how large this menu should be.
            9, 18, 27, 36, or 45 are the available options.
            """})
    public static final Property<Integer> CRATE_MENU_SIZE = PropertyInitializer.newProperty("settings.crate-menu.size",45);

    //@Comment({"""
    //        Available types are CHEST, WORKBENCH, HOPPER, DISPENSER, BREWING
    //        Every type except CHEST ignores "crate-menu.size"
    //        """})
    //public static final Property<String> CRATE_MENU_TYPE = PropertyInitializer.newProperty("settings.crate-menu.type","CHEST");

    @Comment("Whether you want the filler to be enabled.")
    public static final Property<Boolean> CRATE_MENU_FILLER_TOGGLE = PropertyInitializer.newProperty("crate-menu-filler.toggle",false);

    @Comment("The item you want to fill it.")
    public static final Property<String> CRATE_MENU_FILLER_ITEM = PropertyInitializer.newProperty("crate-menu-filler.material","BLACK_STAINED_GLASS_PANE");

    @Comment("The name of the item.")
    public static final Property<String> CRATE_MENU_FILLER_NAME = PropertyInitializer.newProperty("crate-menu-filler.name"," ");

    @Comment("The lore of the item.")
    public static final Property<List<String>> CRATE_MENU_FILLER_LORE = PropertyInitializer.newListProperty("crate-menu-filler.lore", "");

}