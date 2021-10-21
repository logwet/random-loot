package me.logwet.random_loot;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.Level;

@Environment(EnvType.SERVER)
public class RandomLootServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        RandomLoot.log(Level.INFO, "Server class initialized!");
    }
}
