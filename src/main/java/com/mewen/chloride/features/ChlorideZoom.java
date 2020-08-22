package com.mewen.chloride.features;

import com.google.gson.JsonElement;
import com.mewen.chloride.AChlorideFeature;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.options.Option;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class ChlorideZoom extends AChlorideFeature
{
    private ZoomConfiguration zoomConfiguration = new ZoomConfiguration();
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private Boolean currentlyZoomed = false;
    private Boolean originalSmoothCameraEnabled = false;
    private final KeyBinding key = new KeyBinding("key.chloride.zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "category.chloride");

    public ChlorideZoom()
    {
        super(new ZoomGUIConfiguration("option.chloride.category.zoom"));
    }

    @Override
    public void OnRegistration()
    {
        KeyBindingHelper.registerKeyBinding(key);
        ZoomGUIConfiguration fogConfigurationController = (ZoomGUIConfiguration) GetGUI();
        fogConfigurationController.SetZoomController(this);
    }

    @Override
    public JsonElement SerializeConfiguration()
    {
        return gson.toJsonTree(zoomConfiguration);
    }

    @Override
    public void DeserializeConfiguration(JsonElement json)
    {
        zoomConfiguration = gson.fromJson(json, ZoomConfiguration.class);
    }

    public double GetZoomFOV() {return zoomConfiguration.zoomFOV;}
    public void SetZoomFOV(double value) {zoomConfiguration.zoomFOV = value;}

    public boolean IsZooming() {
        return key.isPressed();
    }

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

    @Environment(EnvType.CLIENT)
    public static class ZoomConfiguration
    {
        public double zoomFOV = 19.0;
    }

    @Environment(EnvType.CLIENT)
    public static class ZoomGUIConfiguration extends AGUIConfiguration
    {
        private ChlorideZoom zoom;

        public ZoomGUIConfiguration(String categoryKey)
        {
            super(categoryKey);
        }

        private void SetZoomController(ChlorideZoom zoom)
        {
            this.zoom = zoom;
        }

        @Override
        public Option[] GetOptions()
        {
            DoubleOption ZOOM = new DoubleOption("option.chloride.category.zoom.name", 5.0D, 50.0D, 1.0F,
                    (gameOptions) -> zoom.GetZoomFOV(),
                    (gameOptions, double_) -> zoom.SetZoomFOV(double_),
                    (gameOptions, doubleOption) -> doubleOption.getDisplayPrefix().append(Integer.toString((int) zoom.GetZoomFOV())));
            return new Option[]{ZOOM};
        }
    }
}
