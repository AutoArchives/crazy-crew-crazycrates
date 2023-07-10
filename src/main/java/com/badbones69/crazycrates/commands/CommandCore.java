package com.badbones69.crazycrates.commands;

import ch.jalu.configme.SettingsManager;
import com.badbones69.crazycrates.CrazyCrates;
import com.badbones69.crazycrates.api.configs.types.Locale;
import com.badbones69.crazycrates.api.configs.types.PluginConfig;
import com.ryderbelserion.stick.core.StickCore;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandCore extends StickCore {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);

    private final SettingsManager locale = plugin.getApiManager().getLocale();

    private final SettingsManager pluginConfig = plugin.getApiManager().getPluginConfig();

    @Override
    public String commandTooFewArgs() {
        return this.locale.getProperty(Locale.NOT_ENOUGH_ARGS);
    }

    @Override
    public String commandTooManyArgs() {
        return this.locale.getProperty(Locale.TOO_MANY_ARGS);
    }

    @Override
    public String commandOptionalMsg() {
        return this.locale.getProperty(Locale.OPTIONAL_ARGUMENT);
    }

    @Override
    public String commandRequiredMsg() {
        return this.locale.getProperty(Locale.REQUIRED_ARGUMENT);
    }

    @Override
    public String commandRequirementNotPlayer() {
        return this.locale.getProperty(Locale.MUST_BE_PLAYER);
    }

    @Override
    public String commandRequirementNoPermission() {
        return this.locale.getProperty(Locale.NO_PERMISSION);
    }

    @Override
    public String commandHelpHeader() {
        return this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_HEADER);
    }

    @Override
    public String commandHelpFooter() {
        return this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_FOOTER);
    }

    @Override
    public String commandInvalidPage() {
        return this.pluginConfig.getProperty(PluginConfig.INVALID_HELP_PAGE);
    }

    @Override
    public String commandPageFormat() {
        return this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_FORMAT);
    }

    @Override
    public String commandHoverFormat() {
        return this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_HOVER_FORMAT);
    }

    @Override
    public String commandHoverAction() {
        return this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_HOVER_ACTION);
    }

    @Override
    public String commandNavigationText() {
        return this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_GO_TO_PAGE);
    }

    @Override
    public String commandNavigationNextButton() {
        return this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_NEXT);
    }

    @Override
    public String commandNavigationBackButton() {
        return this.pluginConfig.getProperty(PluginConfig.HELP_PAGE_BACK);
    }
}