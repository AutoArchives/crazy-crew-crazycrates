package com.badbones69.crazycrates.paper.api.v2.enums.support;

import com.badbones69.crazycrates.paper.api.v2.support.holograms.interfaces.HologramManager;
import com.badbones69.crazycrates.paper.api.v2.support.holograms.types.CMIHologramSupport;
import com.badbones69.crazycrates.paper.api.v2.support.holograms.types.DecentHologramSupport;

public enum HologramSupport {

    decent_holograms(DecentHologramSupport.class),
    cmi_holograms(CMIHologramSupport.class);
    //fancy_holograms(FancyHologramSupport.class),
    //internal_holograms(InternalHologramSupport.class);

    private final Class<? extends HologramManager> classObject;

    HologramSupport(Class<? extends HologramManager> classObject) {
        this.classObject = classObject;
    }

    public Class<? extends HologramManager> getClassObject() {
        return this.classObject;
    }
}