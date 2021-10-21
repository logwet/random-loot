package me.logwet.random_loot.mixin.server;

import me.logwet.random_loot.RandomLoot;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.SERVER)
@Mixin(MinecraftDedicatedServer.class)
public abstract class MinecraftDedicatedServerMixin {

    @Inject(method = "setupServer", at = @At("HEAD"))
    private void injectSetupServer(CallbackInfoReturnable<Boolean> cir) {
        RandomLoot.log(Level.INFO, "This line is printed by a MinecraftDedicatedServer mixin!");
    }

}
