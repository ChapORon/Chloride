package com.mewen.chloride.mixin;

import com.mewen.chloride.Chloride;
import com.mewen.chloride.features.Fog;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public class ChlorideFogMixin
{
    @Inject(method = "applyFog", at = @At("HEAD"), cancellable = true)
    private static void DisableFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo ci)
    {
        Fog fog = Chloride.GetFeature("fog");
        if (fog != null)
        {
            if (fog.HideFog(camera, fogType))
                ci.cancel();
        }
    }
}
