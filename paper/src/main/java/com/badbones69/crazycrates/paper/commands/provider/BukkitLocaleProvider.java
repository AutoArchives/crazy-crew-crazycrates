package com.badbones69.crazycrates.paper.commands.provider;

import com.badbones69.crazycrates.api.ConfigManager;
import com.badbones69.crazycrates.api.config.Locale;
import com.badbones69.crazycrates.paper.CrazyCrates;
import com.badbones69.crazycrates.paper.support.placeholders.PlaceholderManager;
import com.ryderbelserion.lexicon.core.builders.commands.interfaces.LocaleProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitLocaleProvider implements LocaleProvider {

    private final CrazyCrates plugin = JavaPlugin.getPlugin(CrazyCrates.class);
    private final PlaceholderManager placeholderManager = this.plugin.getPlaceholderManager();

    @Override
    public String optionalMessage() {
        return this.placeholderManager.setPlaceholders(ConfigManager.getLocale().getProperty(Locale.OPTIONAL_ARGUMENT));
    }

    @Override
    public String requiredMessage() {
        return this.placeholderManager.setPlaceholders(ConfigManager.getLocale().getProperty(Locale.REQUIRED_ARGUMENT));
    }

    @Override
    public String hoverMessage() {
        return this.placeholderManager.setPlaceholders(ConfigManager.getLocale().getProperty(Locale.HOVER_FORMAT));
    }

    @Override
    public String tooManyArgs() {
        return this.placeholderManager.setPlaceholders(ConfigManager.getLocale().getProperty(Locale.TOO_MANY_ARGS));
    }

    @Override
    public String notEnoughArgs() {
        return this.placeholderManager.setPlaceholders(ConfigManager.getLocale().getProperty(Locale.NOT_ENOUGH_ARGS));
    }
}