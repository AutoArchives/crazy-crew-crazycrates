package com.badbones69.crazycrates.paper.api.v2.storage;

public record CustomLocation(int id, String world, double x, double y, double z) {

    @Override
    public int id() {
        return id;
    }

    @Override
    public String world() {
        return world;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

}