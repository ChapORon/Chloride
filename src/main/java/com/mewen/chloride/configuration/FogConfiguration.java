package com.mewen.chloride.configuration;

public class FogConfiguration
{
    public enum WaterFogType
    {
        FULL,
        OFF_WITH_WATER_BREATHING,
        OFF
    }
    public enum LavaFogType
    {
        FULL,
        OFF_WITH_FIRE_RESISTANCE,
        OFF
    }

    public WaterFogType waterFog = WaterFogType.FULL;
    public LavaFogType lavaFog = LavaFogType.FULL;
    public boolean endFog = true;
    public boolean netherFog = true;
    public boolean overworldFog = true;
}
