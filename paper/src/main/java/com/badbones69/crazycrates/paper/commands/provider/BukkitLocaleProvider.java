package com.badbones69.crazycrates.paper.commands.provider;

import com.badbones69.crazycrates.api.ConfigManager;
import com.badbones69.crazycrates.api.config.Locale;
import com.badbones69.crazycrates.paper.support.placeholders.PlaceholderManager;
import com.ryderbelserion.lexicon.core.builders.commands.interfaces.LocaleProvider;

public class BukkitLocaleProvider implements LocaleProvider {

    @Override
    public String optionalMessage() {
        return PlaceholderManager.setPlaceholders(ConfigManager.getLocale().getProperty(Locale.OPTIONAL_ARGUMENT));
    }

    @Override
    public String requiredMessage() {
        return PlaceholderManager.setPlaceholders(ConfigManager.getLocale().getProperty(Locale.REQUIRED_ARGUMENT));
    }

    @Override
    public String hoverMessage() {
        return PlaceholderManager.setPlaceholders(ConfigManager.getLocale().getProperty(Locale.HOVER_FORMAT));
    }

    @Override
    public String tooManyArgs() {
        return PlaceholderManager.setPlaceholders(ConfigManager.getLocale().getProperty(Locale.TOO_MANY_ARGS));
    }

    @Override
    public String notEnoughArgs() {
        return PlaceholderManager.setPlaceholders(ConfigManager.getLocale().getProperty(Locale.NOT_ENOUGH_ARGS));
    }
}