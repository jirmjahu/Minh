package net.jirmjahu.minh.feature.extension;

import net.minestom.server.MinecraftServer;
import net.minestom.server.ServerProcess;

public interface Extension {

    void onEnable();

    void onDisable();

    default ServerProcess getServer() {
        return MinecraftServer.getServerProcess();
    }
}
