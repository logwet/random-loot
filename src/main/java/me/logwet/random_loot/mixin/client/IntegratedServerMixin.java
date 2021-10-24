package me.logwet.random_loot.mixin.client;

import me.logwet.random_loot.RandomLoot;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin {

    @Inject(method = "setupServer", at = @At("HEAD"))
    private void setupServer(CallbackInfoReturnable<Boolean> cir) {
        RandomLoot.setMS(((MinecraftServer) (Object) this));
        RandomLoot.log(Level.INFO, "Integrated server object assigned.");
    }

}
