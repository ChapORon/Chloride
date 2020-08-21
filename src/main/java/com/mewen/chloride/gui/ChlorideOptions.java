package com.mewen.chloride.gui;

import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.options.CyclingOption;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.text.TranslatableText;

public class ChlorideOptions
{
    public static final DoubleOption ZOOM = new DoubleOption("option.chloride.category.zoom.name", 5.0D, 50.0D, 1.0F,
            (gameOptions) -> ChlorideConfigurationController.GetZoomFOV(),
            (gameOptions, double_) -> ChlorideConfigurationController.SetZoomFOV(double_),
            (gameOptions, doubleOption) -> doubleOption.getDisplayPrefix().append(Integer.toString((int)ChlorideConfigurationController.GetZoomFOV())));
    public static final BooleanOption NETHER_FOG = new BooleanOption("option.chloride.category.fog.nether",
            (gameOptions) -> ChlorideConfigurationController.GetNetherFog(),
            (gameOptions, boolean_) -> ChlorideConfigurationController.SetNetherFog(boolean_));
    public static final BooleanOption OVERWORLD_FOG = new BooleanOption("option.chloride.category.fog.overworld",
            (gameOptions) -> ChlorideConfigurationController.GetOverworldFog(),
            (gameOptions, boolean_) -> ChlorideConfigurationController.SetOverworldFog(boolean_));
    public static final BooleanOption END_FOG = new BooleanOption("option.chloride.category.fog.end",
            (gameOptions) -> ChlorideConfigurationController.GetEndFog(),
            (gameOptions, boolean_) -> ChlorideConfigurationController.SetEndFog(boolean_));
    public static final CyclingOption WATER_FOG_TYPE = new CyclingOption("option.chloride.category.fog.water",
            (gameOptions, integer) -> ChlorideConfigurationController.SetWaterFogType(integer),
            (gameOptions, cyclingOption) -> cyclingOption.getDisplayPrefix().append(new TranslatableText(ChlorideConfigurationController.GetWaterFogTypeKey())));
    public static final CyclingOption LAVA_FOG_TYPE = new CyclingOption("option.chloride.category.fog.lava",
            (gameOptions, integer) -> ChlorideConfigurationController.SetLavaFogType(integer),
            (gameOptions, cyclingOption) -> cyclingOption.getDisplayPrefix().append(new TranslatableText(ChlorideConfigurationController.GetLavaFogTypeKey())));
}
