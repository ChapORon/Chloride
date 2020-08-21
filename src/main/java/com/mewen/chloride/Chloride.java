package com.mewen.chloride;

import com.mewen.chloride.actions.ReloadAction;
import com.mewen.chloride.features.Fog;
import com.mewen.chloride.features.Zoom;
import com.mewen.chloride.gui.ChlorideOptionScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.GameOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Chloride implements ClientModInitializer
{
    private final static HashMap<String, AChlorideFeature> features = new HashMap<>();
    private final static ArrayList<AChlorideAction> actions = new ArrayList<>();
    private static Configuration config;

    @Override
    public void onInitializeClient()
    {
        ConfigurationSerializer.Init();
        config = ConfigurationSerializer.Load();
        RegisterFeature("zoom", new Zoom());
        RegisterFeature("fog", new Fog());
        RegisterAction(new ReloadAction());
    }

    private static void RegisterAction(AChlorideAction action) {actions.add(action);}

    private static void RegisterFeature(String name, AChlorideFeature feature)
    {
        if (!features.containsKey(name))
        {
            feature.SetConfig(config);
            feature.OnRegistration();
            features.put(name, feature);
        }
    }

    public static void UpdateConfiguration(Configuration newConfig)
    {
        config = newConfig;
        Collection<AChlorideFeature> featureList = features.values();
        for (AChlorideFeature feature : featureList)
            feature.SetConfig(config);
    }

    public static void ReloadConfiguration() {UpdateConfiguration(ConfigurationSerializer.Load());}

    public static void OpenConfigMenu(Screen parent, GameOptions gameOptions) {MinecraftClient.getInstance().openScreen(new ChlorideOptionScreen(parent, gameOptions, config));}

    @SuppressWarnings("unchecked")
    public static <T extends AChlorideFeature> T GetFeature(String name)
    {
        if (!features.containsKey(name))
            return null;
        return (T)features.get(name);
    }
}
