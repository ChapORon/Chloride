package com.mewen.chloride.gui;

import com.mewen.chloride.Configuration;
import com.mewen.chloride.configuration.FogConfiguration;

public class ChlorideConfigurationController
{
    private static Configuration config;

    public static void SetConfiguration(Configuration newConfig) {config = newConfig;}
    public static Configuration GetConfiguration() {return config;}

    public static double GetZoomFOV() {return config.zoom.zoomFOV;}
    public static void SetZoomFOV(double value) {config.zoom.zoomFOV = value;}

    public static boolean GetNetherFog() {return config.fog.netherFog;}
    public static void SetNetherFog(boolean value) {config.fog.netherFog = value;}

    public static boolean GetOverworldFog() {return config.fog.overworldFog;}
    public static void SetOverworldFog(boolean value) {config.fog.overworldFog = value;}

    public static boolean GetEndFog() {return config.fog.endFog;}
    public static void SetEndFog(boolean value) {config.fog.endFog = value;}

    public static void SetWaterFogType(int index)
    {
        FogConfiguration.WaterFogType[] values = FogConfiguration.WaterFogType.values();
        int currentIndex = 0;
        while (values[currentIndex] != config.fog.waterFog)
            ++currentIndex;
        config.fog.waterFog = values[(currentIndex + index) % values.length];
    }

    public static String GetWaterFogTypeKey()
    {
        switch (config.fog.waterFog)
        {
            case OFF:
                return "option.chloride.category.fog.water.off";
            case OFF_WITH_WATER_BREATHING:
                return "option.chloride.category.fog.water.potion";
            case FULL:
                return "option.chloride.category.fog.water.full";
        }
        return "";
    }

    public static void SetLavaFogType(int index)
    {
        FogConfiguration.LavaFogType[] values = FogConfiguration.LavaFogType.values();
        int currentIndex = 0;
        while (values[currentIndex] != config.fog.lavaFog)
            ++currentIndex;
        config.fog.lavaFog = values[(currentIndex + index) % values.length];
    }

    public static String GetLavaFogTypeKey()
    {
        switch (config.fog.lavaFog)
        {
            case OFF:
                return "option.chloride.category.fog.lava.off";
            case OFF_WITH_FIRE_RESISTANCE:
                return "option.chloride.category.fog.lava.potion";
            case FULL:
                return "option.chloride.category.fog.lava.full";
        }
        return "";
    }
}
