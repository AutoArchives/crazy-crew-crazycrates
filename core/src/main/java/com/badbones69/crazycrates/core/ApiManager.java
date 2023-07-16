package com.badbones69.crazycrates.core;

import ch.jalu.configme.SettingsManager;

public class ApiManager {

    private SettingsManager locale;
    private SettingsManager config;
    private SettingsManager pluginConfig;

    public ApiManager load() {
        return this;
    }
}