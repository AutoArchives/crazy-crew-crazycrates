package com.badbones69.crazycrates.paper.api.v2.configs.types.sections;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.CommentsConfiguration;
import ch.jalu.configme.properties.Property;
import com.badbones69.crazycrates.paper.api.v2.enums.support.HologramSupport;
import static ch.jalu.configme.properties.PropertyInitializer.newProperty;

public class PluginSupportSection implements SettingsHolder {

    public PluginSupportSection() {}

    @Override
    public void registerComments(CommentsConfiguration conf) {
        String[] header = {
                "=========================================================",
                "",
                "This section is solely related to plugin dependencies.",
                "You can now have easy control over what gets enabled or not",
                "",
                "========================================================="
        };

        conf.setComment("support", header);
    }

    @Comment({
            "PlaceholderAPI is required for this option to be true!",
            "Download: https://www.spigotmc.org/resources/placeholderapi.6245/"
    })
    public static final Property<Boolean> PLACEHOLDERAPI_SUPPORT = newProperty("support.placeholderapi", false);

    @Comment({
            "Whether you want holograms support to be enabled or not.",
            "You must have a holograms plugin on your server before this is enabled."
    })
    public static final Property<Boolean> HOLOGRAMS_SUPPORT_ENABLED = newProperty("support.holograms.toggle", false);

    @Comment({
            "Available Values: decent_holograms, cmi_holograms",
            "You must have at least one of these plugins on your server in order for this to work!",
            "",
            "CMI: https://www.spigotmc.org/resources/cmi-298-commands-insane-kits-portals-essentials-economy-mysql-sqlite-much-more.3742/",
            "DecentHolograms: https://www.spigotmc.org/resources/decentholograms-1-8-1-20-1-papi-support-no-dependencies.96927/"
    })
    public static final Property<HologramSupport> HOLOGRAMS_SUPPORT_TYPE = newProperty(HologramSupport.class, "support.holograms.type", HologramSupport.decent_holograms);
}