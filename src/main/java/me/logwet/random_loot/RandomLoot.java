package me.logwet.random_loot;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class RandomLoot implements ModInitializer {

	public static final String MODID = "random-loot";
	public static final String VERSION = FabricLoader.getInstance().getModContainer(MODID).get().getMetadata().getVersion().getFriendlyString();
	public static final boolean IS_CLIENT = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	public static final Logger LOGGER = LogManager.getLogger(MODID);

	private static Long seed;
	private static Random randomInstance = new Random(0);

	public static void log(Level level, String message) {
		LOGGER.log(level, "[" + MODID + " v" + VERSION + "] " + message);
	}

	@NotNull
	private static Long getSeed() {
		return seed;
	}

	public static void setSeed(Long seed) {
		RandomLoot.seed = seed;
	}

	public static void newRandomInstance() {
		log(Level.INFO, "Resetting random instance...");

		long rawSeed = Objects.requireNonNull(getSeed());
		String rawSeedString = Long.toString(rawSeed);

		long seed;
		StringBuilder seedString = new StringBuilder();

        /*
		 This drops every second digit from the world seed and uses the result as the random seed for all RNG in the mod
		 It's a measure to combat a potential divine travel esque situation.
		 */
		for (int i = 0; i < rawSeedString.length(); i += 2) {
			seedString.append(rawSeedString.charAt(i));
			seedString.append("0");
		}

		try {
			seed = Long.parseLong(seedString.toString());
		} catch (NumberFormatException e) {
			log(Level.INFO, "Unable to drop digits from seed. Using complete world seed.");
			seed = rawSeed;
		}

		Random returnRandom = new Random(seed);
		int j = returnRandom.nextInt(50) + 50;
		for (int i = 0; i < j; i++) {
			returnRandom.nextInt();
		}

		randomInstance = returnRandom;
	}

	public static Random getRandomInstance() {
		return randomInstance;
	}

	public static ResourcePackProvider[] addRandomDataPackProvider(ResourcePackProvider... providers) {
		ArrayList<ResourcePackProvider> resourcePackProviderList = new ArrayList<>(Arrays.asList(providers));
		resourcePackProviderList.add(1, new RandomLootDataPackProvider());
		return resourcePackProviderList.toArray(new ResourcePackProvider[providers.length + 1]);
	}

	@Override
	public void onInitialize() {
		log(Level.INFO, "Main class initialized!");
	}

}
