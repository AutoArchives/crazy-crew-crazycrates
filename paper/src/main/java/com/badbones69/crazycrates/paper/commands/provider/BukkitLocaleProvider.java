package com.badbones69.crazycrates.paper.commands.provider;

import com.ryderbelserion.lexicon.core.builders.commands.interfaces.LocaleProvider;

public class BukkitLocaleProvider implements LocaleProvider {

    @Override
    public String optionalMessage() {
        return "This argument is optional.";
    }

    @Override
    public String requiredMessage() {
        return "This argument is required.";
    }

    @Override
    public String hoverMessage() {
        return "Hover over me";
    }

    @Override
    public String tooManyArgs() {
        return "Too many args";
    }

    @Override
    public String notEnoughArgs() {
        return "Not enough args";
    }
}