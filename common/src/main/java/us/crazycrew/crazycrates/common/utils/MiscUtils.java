package us.crazycrew.crazycrates.common.utils;

import net.kyori.adventure.text.minimessage.MiniMessage;
import java.util.List;

public class MiscUtils {

    /**
     * Loops through a string-list and parses the colors then returns a string builder
     *
     * @param list to convert
     * @return the string-builder
     */
    public static String convertList(List<String> list) {
        StringBuilder message = new StringBuilder();

        for (String line : list) {
            message.append(MiniMessage.miniMessage().deserialize(line)).append("\n");
        }

        return message.toString();
    }
}