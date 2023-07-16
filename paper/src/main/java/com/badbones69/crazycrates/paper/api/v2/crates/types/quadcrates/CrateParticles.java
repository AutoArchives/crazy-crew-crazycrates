package com.badbones69.crazycrates.paper.api.v2.crates.types.quadcrates;

public enum CrateParticles {
    
    FLAME("Flame"),
    VILLAGER_HAPPY("Villager Happy"),
    SPELL_WITCH("Spell Witch"),
    REDSTONE("Redstone");
    
    private final String name;

    CrateParticles(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}