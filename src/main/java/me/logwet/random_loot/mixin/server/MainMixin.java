package me.logwet.random_loot.mixin.server;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.serialization.Lifecycle;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import me.logwet.random_loot.RandomLoot;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.datafixer.NbtOps;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.Main;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.dedicated.EulaReader;
import net.minecraft.server.dedicated.ServerPropertiesHandler;
import net.minecraft.server.dedicated.ServerPropertiesLoader;
import net.minecraft.util.UserCache;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.registry.RegistryTracker;
import net.minecraft.world.GameRules;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

@Environment(EnvType.SERVER)
@Mixin(Main.class)
public abstract class MainMixin {

    @ModifyArg(
            method = "main",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/resource/ResourcePackManager;<init>(Lnet/minecraft/resource/ResourcePackProfile$Factory;[Lnet/minecraft/resource/ResourcePackProvider;)V"
            ),
            index = 1
    )
    private static ResourcePackProvider[] addRandomDataPackProvider(ResourcePackProvider... providers) {
        return RandomLoot.addRandomDataPackProvider(providers);
    }

    @Inject(
            method = "main",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/resource/ServerResourceManager;reload(Ljava/util/List;Lnet/minecraft/server/command/CommandManager$RegistrationEnvironment;ILjava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"
            ),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private static void injectBeforeResourceReloadAndGetSeed(String[] args, CallbackInfo ci, OptionParser optionParser, OptionSpec optionSpec, OptionSpec optionSpec2, OptionSpec optionSpec3, OptionSpec optionSpec4, OptionSpec optionSpec5, OptionSpec optionSpec6, OptionSpec optionSpec7, OptionSpec optionSpec8, OptionSpec optionSpec9, OptionSpec optionSpec10, OptionSpec optionSpec11, OptionSpec optionSpec12, OptionSpec optionSpec13, OptionSpec optionSpec14, OptionSet optionSet, Path path, ServerPropertiesLoader serverPropertiesLoader, Path path2, EulaReader eulaReader, File file, YggdrasilAuthenticationService yggdrasilAuthenticationService, MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository, UserCache userCache, String string, LevelStorage levelStorage, LevelStorage.Session session, DataPackSettings dataPackSettings, boolean bl, ResourcePackManager resourcePackManager, DataPackSettings dataPackSettings2) {
        SaveProperties saveProperties = (SaveProperties) session.readLevelProperties(RegistryOps.of(NbtOps.INSTANCE, new ServerResourceManager(CommandManager.RegistrationEnvironment.DEDICATED, 2).getResourceManager(), RegistryTracker.create()), dataPackSettings2);

        if (Objects.isNull(saveProperties)) {
            ServerPropertiesHandler serverPropertiesHandler = serverPropertiesLoader.getPropertiesHandler();

            LevelInfo levelInfo2 = new LevelInfo(serverPropertiesHandler.levelName, serverPropertiesHandler.gameMode, serverPropertiesHandler.hardcore, serverPropertiesHandler.difficulty, false, new GameRules(), dataPackSettings2);
            GeneratorOptions generatorOptions2 = serverPropertiesHandler.field_24623;

            saveProperties = new LevelProperties(levelInfo2, generatorOptions2, Lifecycle.stable());
        }

        RandomLoot.setSeed(Objects.requireNonNull(saveProperties).getGeneratorOptions().getSeed());
    }

}
