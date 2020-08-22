package com.mewen.chloride.features;

import com.google.gson.JsonElement;
import com.mewen.chloride.AChlorideFeature;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.BooleanOption;
import net.minecraft.client.options.CyclingOption;
import net.minecraft.client.options.Option;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.dimension.DimensionType;

@Environment(EnvType.CLIENT)
public class ChlorideFogController extends AChlorideFeature
{
    private FogConfiguration fogConfiguration = new FogConfiguration();

    public ChlorideFogController()
    {
        super(new FogGUIConfiguration("option.chloride.category.fog"));
    }

    @Override
    public void OnRegistration()
    {
        FogGUIConfiguration fogConfigurationController = (FogGUIConfiguration) GetGUI();
        fogConfigurationController.SetFogController(this);
    }

    @Override
    public JsonElement SerializeConfiguration()
    {
        return gson.toJsonTree(fogConfiguration);
    }

    @Override
    public void DeserializeConfiguration(JsonElement json)
    {
        fogConfiguration = gson.fromJson(json, FogConfiguration.class);
    }
    public boolean GetNetherFog() {return fogConfiguration.netherFog;}
    public void SetNetherFog(boolean value) {fogConfiguration.netherFog = value;}

    public boolean GetOverworldFog() {return fogConfiguration.overworldFog;}
    public void SetOverworldFog(boolean value) {fogConfiguration.overworldFog = value;}

    public boolean GetEndFog() {return fogConfiguration.endFog;}
    public void SetEndFog(boolean value) {fogConfiguration.endFog = value;}

    public void SetWaterFogType(int index)
    {
        FogConfiguration.WaterFogType[] values = FogConfiguration.WaterFogType.values();
        int currentIndex = 0;
        while (values[currentIndex] != fogConfiguration.waterFog)
            ++currentIndex;
        fogConfiguration.waterFog = values[(currentIndex + index) % values.length];
    }

    public String GetWaterFogTypeKey(CyclingOption cyclingOption)
    {
        switch (fogConfiguration.waterFog)
        {
            case OFF:
                cyclingOption.setTooltip(MinecraftClient.getInstance().textRenderer.wrapLines(new TranslatableText("option.chloride.category.fog.water.off.tooltip"), 200));
                return "option.chloride.category.fog.water.off";
            case OFF_WITH_WATER_BREATHING:
                cyclingOption.setTooltip(MinecraftClient.getInstance().textRenderer.wrapLines(new TranslatableText("option.chloride.category.fog.water.potion.tooltip"), 200));
                return "option.chloride.category.fog.water.potion";
            case FULL:
                cyclingOption.setTooltip(MinecraftClient.getInstance().textRenderer.wrapLines(new TranslatableText("option.chloride.category.fog.water.full.tooltip"), 200));
                return "option.chloride.category.fog.water.full";
        }
        return "";
    }

    public void SetLavaFogType(int index)
    {
        FogConfiguration.LavaFogType[] values = FogConfiguration.LavaFogType.values();
        int currentIndex = 0;
        while (values[currentIndex] != fogConfiguration.lavaFog)
            ++currentIndex;
        fogConfiguration.lavaFog = values[(currentIndex + index) % values.length];
    }

    public String GetLavaFogTypeKey(CyclingOption cyclingOption)
    {
        switch (fogConfiguration.lavaFog)
        {
            case OFF:
                cyclingOption.setTooltip(MinecraftClient.getInstance().textRenderer.wrapLines(new TranslatableText("option.chloride.category.fog.lava.off.tooltip"), 200));
                return "option.chloride.category.fog.lava.off";
            case OFF_WITH_FIRE_RESISTANCE:
                cyclingOption.setTooltip(MinecraftClient.getInstance().textRenderer.wrapLines(new TranslatableText("option.chloride.category.fog.lava.potion.tooltip"), 200));
                return "option.chloride.category.fog.lava.potion";
            case FULL:
                cyclingOption.setTooltip(MinecraftClient.getInstance().textRenderer.wrapLines(new TranslatableText("option.chloride.category.fog.lava.full.tooltip"), 200));
                return "option.chloride.category.fog.lava.full";
        }
        return "";
    }

    public boolean HideFog(Camera camera)
    {
        FluidState fluidState = camera.getSubmergedFluidState();
        Entity entity = camera.getFocusedEntity();
        if (entity instanceof LivingEntity && ((LivingEntity)entity).hasStatusEffect(StatusEffects.BLINDNESS))
            return false;
        if (fluidState.isIn(FluidTags.WATER))
        {
            switch (fogConfiguration.waterFog)
            {
                case OFF:
                    return true;
                case FULL:
                    return false;
                case OFF_WITH_WATER_BREATHING:
                    return (entity instanceof LivingEntity && ((LivingEntity) entity).hasStatusEffect(StatusEffects.WATER_BREATHING));
            }
        }
        else if (fluidState.isIn(FluidTags.LAVA))
        {
            switch (fogConfiguration.lavaFog)
            {
                case OFF:
                    return true;
                case FULL:
                    return false;
                case OFF_WITH_FIRE_RESISTANCE:
                    return (entity instanceof LivingEntity && ((LivingEntity) entity).hasStatusEffect(StatusEffects.FIRE_RESISTANCE));
            }
        }
        DimensionType dimension = entity.world.getDimension();
        if (dimension.hasEnderDragonFight())
            return !fogConfiguration.endFog;
        else if (!dimension.isBedWorking())
            return !fogConfiguration.netherFog;
        return !fogConfiguration.overworldFog;
    }

    @Environment(EnvType.CLIENT)
    public static class FogConfiguration
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

    @Environment(EnvType.CLIENT)
    public static class FogGUIConfiguration extends AGUIConfiguration
    {
        private ChlorideFogController fogController;

        public FogGUIConfiguration(String categoryKey)
        {
            super(categoryKey);
        }

        public void SetFogController(ChlorideFogController fogController)
        {
            this.fogController = fogController;
        }

        @Override
        public Option[] GetOptions()
        {
            BooleanOption NETHER_FOG = new BooleanOption("option.chloride.category.fog.nether",
                    (gameOptions) -> fogController.GetNetherFog(),
                    (gameOptions, boolean_) -> fogController.SetNetherFog(boolean_));
            BooleanOption OVERWORLD_FOG = new BooleanOption("option.chloride.category.fog.overworld",
                    (gameOptions) -> fogController.GetOverworldFog(),
                    (gameOptions, boolean_) -> fogController.SetOverworldFog(boolean_));
            BooleanOption END_FOG = new BooleanOption("option.chloride.category.fog.end",
                    (gameOptions) -> fogController.GetEndFog(),
                    (gameOptions, boolean_) -> fogController.SetEndFog(boolean_));
            CyclingOption WATER_FOG_TYPE = new CyclingOption("option.chloride.category.fog.water",
                    (gameOptions, integer) -> fogController.SetWaterFogType(integer),
                    (gameOptions, cyclingOption) -> cyclingOption.getDisplayPrefix().append(new TranslatableText(fogController.GetWaterFogTypeKey(cyclingOption))));
            CyclingOption LAVA_FOG_TYPE = new CyclingOption("option.chloride.category.fog.lava",
                    (gameOptions, integer) -> fogController.SetLavaFogType(integer),
                    (gameOptions, cyclingOption) -> cyclingOption.getDisplayPrefix().append(new TranslatableText(fogController.GetLavaFogTypeKey(cyclingOption))));
            return new Option[]{NETHER_FOG, OVERWORLD_FOG, END_FOG, WATER_FOG_TYPE, LAVA_FOG_TYPE};
        }
    }
}
