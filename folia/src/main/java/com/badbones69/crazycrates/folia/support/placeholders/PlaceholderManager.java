package com.badbones69.crazycrates.folia.support.placeholders;

import com.badbones69.crazycrates.api.ConfigManager;
import com.badbones69.crazycrates.api.config.types.Locale;

public class PlaceholderManager {

	public static String setPlaceholders(String placeholders) {
		placeholders = placeholders.replace("\\{prefix}", ConfigManager.getLocale().getProperty(Locale.COMMAND_PREFIX));

		return placeholders;
	}
}