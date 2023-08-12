package com.badbones69.crazycrates.paper.support.placeholders;

import com.badbones69.crazycrates.api.ConfigManager;
import com.badbones69.crazycrates.api.config.Locale;

public class PlaceholderManager {

	public String setPlaceholders(String placeholders) {
		placeholders = placeholders.replace("\\{prefix}", ConfigManager.getLocale().getProperty(Locale.COMMAND_PREFIX));

		return placeholders;
	}
}