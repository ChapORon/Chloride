package com.mewen.chloride.mixin;

import com.mewen.chloride.Chloride;
import com.mewen.chloride.features.ChlorideFogController;
import com.mewen.chloride.features.ChlorideZoom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class ChlorideZoomMixin
{
    static
    {
        Chloride.RegisterFeature("fog", new ChlorideFogController());
    }

    @Inject(method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D", at = @At("HEAD"), cancellable = true)
    public void ApplyZoom(CallbackInfoReturnable<Double> callbackInfo)
    {
        ChlorideZoom zoom = Chloride.GetFeature("zoom");
        if (zoom != null)
        {
            if (zoom.IsZooming())
                callbackInfo.setReturnValue(zoom.GetZoomFOV());
            zoom.ManageSmoothCamera();
        }
    }
}