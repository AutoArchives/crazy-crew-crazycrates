package com.badbones69.crazycrates.fabric.server;

import com.badbones69.crazycrates.fabric.server.frame.FabricCore;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class CrazyCrates implements DedicatedServerModInitializer {

    private MinecraftServer server;
    private FabricCore fabricCore;

    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            this.server = server;

            this.fabricCore = new FabricCore(server);
        });
    }

    public MinecraftServer getServer() {
        return server;
    }

    public FabricCore getFabricCore() {
        return fabricCore;
    }
}