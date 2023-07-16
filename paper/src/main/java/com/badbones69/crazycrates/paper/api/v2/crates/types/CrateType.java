package com.badbones69.crazycrates.paper.api.v2.crates.types;

public enum CrateType {

    csgo("CSGO"),
    quad_crate("QuadCrate"),
    cosmic("Cosmic"),
    quick_crate("QuickCrate"),
    roulette("Roulette"),
    crate_on_the_go("CrateOnTheGo"),
    fire_cracker("FireCracker"),
    wonder("Wonder"),
    wheel("Wheel"),
    war("War");

    private final String crateType;

    CrateType(String crateType) {
        this.crateType = crateType;
    }

    public static CrateType getFromName(String name) {
        for (CrateType crate: CrateType.values()) {
            if (crate.getCrateType().equalsIgnoreCase(name)) return crate;
        }

        return null;
    }

    public String getCrateType() {
        return this.crateType;
    }
}