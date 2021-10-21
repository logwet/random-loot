package me.logwet.random_loot;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.Level;

@Environment(EnvType.CLIENT)
public class RandomLootClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        RandomLoot.log(Level.INFO, "Client class initialized!");
    }
}
