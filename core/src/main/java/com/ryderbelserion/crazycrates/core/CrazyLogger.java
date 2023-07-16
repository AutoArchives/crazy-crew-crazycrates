package com.ryderbelserion.crazycrates.core;

import com.ryderbelserion.crazycrates.core.utils.AdventureUtils;

public class CrazyLogger {

    public static void debug(String message) {
        debug(message, null);
    }

    public static void debug(String message, Exception exception) {
        log("<yellow>[DEBUG] " + message);

        if (exception != null) exception.printStackTrace();
    }

    public static void info(String message) {
        log("<aqua>[INFO] " + message);
    }

    public static void severe(String message) {
        severe(message, null);
    }

    public static void severe(String message, Exception exception) {
        log("<red>[ERROR] " + message);

        if (exception != null) exception.printStackTrace();
    }

    public static void warn(String message) {
        warn(message, null);
    }

    public static void warn(String message, Exception exception) {
        log("<yellow>[WARN] " + message);

        if (exception != null) exception.printStackTrace();
    }

    private static void log(String message) {
       CrazyCore.api().adventure().console().sendMessage(AdventureUtils.parse(message));
    }
}