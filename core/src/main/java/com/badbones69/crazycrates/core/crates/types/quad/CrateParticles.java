package com.badbones69.crazycrates.core.crates.types.quad;

public enum CrateParticles {
    
    flame("flame"),
    villager_happy("villager_happy"),
    spell_witch("spell_witch"),
    redstone("redstone");
    
    private final String name;

    CrateParticles(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}