package com.badbones69.crazycrates.paper.commands.provider;

import com.badbones69.crazycrates.api.ConfigManager;
import com.badbones69.crazycrates.api.config.types.Locale;
import com.ryderbelserion.lexicon.core.builders.provider.command.LocaleProvider;

public class BukkitLocaleProvider implements LocaleProvider {

    @Override
    public String optionalMessage() {
        return ConfigManager.getLocale().getProperty(Locale.OPTIONAL_ARGUMENT).replaceAll("\\{prefix}", ConfigManager.getLocale().getProperty(Locale.COMMAND_PREFIX));
    }

    @Override
    public String requiredMessage() {
        return ConfigManager.getLocale().getProperty(Locale.REQUIRED_ARGUMENT).replaceAll("\\{prefix}", ConfigManager.getLocale().getProperty(Locale.COMMAND_PREFIX));
    }

    @Override
    public String hoverMessage() {
        return ConfigManager.getLocale().getProperty(Locale.HELP_HOVER_FORMAT).replaceAll("\\{prefix}", ConfigManager.getLocale().getProperty(Locale.COMMAND_PREFIX));
    }

    @Override
    public String hoverAction() {
        return ConfigManager.getLocale().getProperty(Locale.HELP_HOVER_ACTION);
    }

    @Override
    public String tooManyArgs() {
        return ConfigManager.getLocale().getProperty(Locale.TOO_MANY_ARGS).replaceAll("\\{prefix}", ConfigManager.getLocale().getProperty(Locale.COMMAND_PREFIX));
    }

    @Override
    public String notEnoughArgs() {
        return ConfigManager.getLocale().getProperty(Locale.NOT_ENOUGH_ARGS).replaceAll("\\{prefix}", ConfigManager.getLocale().getProperty(Locale.COMMAND_PREFIX));
    }

    @Override
    public String invalidPage() {
        return ConfigManager.getLocale().getProperty(Locale.HELP_INVALID_PAGE).replaceAll("\\{prefix}", ConfigManager.getLocale().getProperty(Locale.COMMAND_PREFIX));
    }

    @Override
    public String pageHeader() {
        return ConfigManager.getLocale().getProperty(Locale.HELP_PAGE_HEADER);
    }

    @Override
    public String pageFormat() {
        return ConfigManager.getLocale().getProperty(Locale.HELP_PAGE_FORMAT);
    }

    @Override
    public String pageFooter() {
        return ConfigManager.getLocale().getProperty(Locale.HELP_PAGE_FOOTER);
    }

    @Override
    public String pageNavigation() {
        return ConfigManager.getLocale().getProperty(Locale.HELP_PAGE_GO_TO_PAGE);
    }

    @Override
    public String pageNextButton() {
        return ConfigManager.getLocale().getProperty(Locale.HELP_PAGE_NEXT);
    }

    @Override
    public String pageBackButton() {
        return ConfigManager.getLocale().getProperty(Locale.HELP_PAGE_BACK);
    }
}