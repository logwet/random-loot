package me.logwet.random_loot;

import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;

import java.util.function.Consumer;

public class RandomLootDataPackProvider implements ResourcePackProvider {
    private final RandomLootDataPack pack = new RandomLootDataPack(new String[]{"minecraft"});

    public <T extends ResourcePackProfile> void register(Consumer<T> consumer, ResourcePackProfile.Factory<T> factory) {
        T resourcePackProfile = ResourcePackProfile.of("randomLootDataPack", true, () -> this.pack, factory, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.PACK_SOURCE_BUILTIN);
        if (resourcePackProfile != null) {
            consumer.accept(resourcePackProfile);
        }
    }

}
