package com.badbones69.crazycrates.core.crates.particles;

import java.util.UUID;

public abstract class ParticleType {

    private final String particle;

    protected ParticleType(String particle) {
        this.particle = particle;
    }

    public abstract void spawn(UUID uuid);

    public String getParticle() {
        return this.particle;
    }
}