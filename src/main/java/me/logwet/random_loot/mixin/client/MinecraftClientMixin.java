package me.logwet.random_loot.mixin.client;

import com.mojang.datafixers.util.Function4;
import me.logwet.random_loot.RandomLoot;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.*;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.registry.RegistryTracker;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @ModifyArg(
            method = "method_29604",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/resource/ResourcePackManager;<init>(Lnet/minecraft/resource/ResourcePackProfile$Factory;[Lnet/minecraft/resource/ResourcePackProvider;)V"
            ),
            index = 1
    )
    private ResourcePackProvider[] addRandomDataPackProvider(ResourcePackProvider... providers) {
        return RandomLoot.addRandomDataPackProvider(providers);
    }

    @Inject(
            method = "method_29604",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/resource/ServerResourceManager;reload(Ljava/util/List;Lnet/minecraft/server/command/CommandManager$RegistrationEnvironment;ILjava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"
            ),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private void injectBeforeResourceReloadAndGetSeed(RegistryTracker.Modifiable modifiable, Function<LevelStorage.Session, DataPackSettings> function, Function4<LevelStorage.Session, RegistryTracker.Modifiable, ResourceManager, DataPackSettings, SaveProperties> function4, boolean bl, LevelStorage.Session session, CallbackInfoReturnable<MinecraftClient.IntegratedResourceManager> cir, DataPackSettings dataPackSettings, ResourcePackManager resourcePackManager, DataPackSettings dataPackSettings2) {
        SaveProperties saveProperties = (SaveProperties) function4.apply(session, modifiable, new ServerResourceManager(CommandManager.RegistrationEnvironment.INTEGRATED, 2).getResourceManager(), dataPackSettings2);

        RandomLoot.setSeed(Objects.requireNonNull(saveProperties).getGeneratorOptions().getSeed());
    }

}
