package com.mewen.chloride.features;

import com.mewen.chloride.AChlorideFeature;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Zoom extends AChlorideFeature
{
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private Boolean currentlyZoomed = false;
    private Boolean originalSmoothCameraEnabled = false;
    private final KeyBinding key = new KeyBinding("key.chloride.zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "category.chloride");

    @Override
    public void OnRegistration()
    {
        KeyBindingHelper.registerKeyBinding(key);
    }

    public boolean IsZooming() {
        return key.isPressed();
    }

    public double GetZoomFOV() {return config.zoom.zoomFOV;}

    public void ManageSmoothCamera()
    {
        boolean isZooming = key.isPressed();
        if (isZooming && !currentlyZoomed)
        {
            originalSmoothCameraEnabled = mc.options.smoothCameraEnabled;
            currentlyZoomed = true;
            mc.options.smoothCameraEnabled = true;
        }
        else if (!isZooming && currentlyZoomed)
        {
            currentlyZoomed = false;
            mc.options.smoothCameraEnabled = originalSmoothCameraEnabled;
        }
    }
}
