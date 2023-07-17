package com.badbones69.crazycrates.core.enums;

public enum KeyType {
    
    PHYSICAL_KEY("physical_key"),
    VIRTUAL_KEY("virtual_key"),
    FREE_KEY("free_key");
    
    private final String name;

    KeyType(String name) {
        this.name = name;
    }

    public static KeyType getFromName(String type) {
        if (type.equalsIgnoreCase("virtual") || type.equalsIgnoreCase("v")) {
            return KeyType.VIRTUAL_KEY;
        } else if (type.equalsIgnoreCase("physical") || type.equalsIgnoreCase("p")) {
            return KeyType.PHYSICAL_KEY;
        } else if (type.equalsIgnoreCase("free") || type.equalsIgnoreCase("f")) {
            return KeyType.FREE_KEY;
        }

        return null;
    }

    public String getName() {
        return name;
    }
}