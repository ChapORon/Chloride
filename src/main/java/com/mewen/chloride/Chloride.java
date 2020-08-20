package com.mewen.chloride;

import com.mewen.chloride.actions.ReloadAction;
import com.mewen.chloride.features.Fog;
import com.mewen.chloride.features.Zoom;
import net.fabricmc.api.ClientModInitializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Chloride implements ClientModInitializer
{
    private static HashMap<String, AChlorideFeature> features = new HashMap<>();
    private static ArrayList<AChlorideAction> actions = new ArrayList<>();
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
            features.put(name, feature);
        }
    }

    public static void ReloadConfiguration()
    {
        config = ConfigurationSerializer.Load();
        Collection<AChlorideFeature> featureList = features.values();
        for (AChlorideFeature feature : featureList)
            feature.SetConfig(config);
    }

    public static <T extends AChlorideFeature> T GetFeature(String name)
    {
        if (!features.containsKey(name))
            return null;
        return (T)features.get(name);
    }
}
