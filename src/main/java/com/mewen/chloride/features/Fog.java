package com.mewen.chloride.features;

import com.mewen.chloride.AChlorideFeature;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.world.dimension.DimensionType;

public class Fog extends AChlorideFeature
{
    @Override
    public void OnRegistration() {}

    public boolean HideFog(Camera camera, BackgroundRenderer.FogType fogType)
    {
        FluidState fluidState = camera.getSubmergedFluidState();
        Entity entity = camera.getFocusedEntity();
        if (entity instanceof LivingEntity && ((LivingEntity)entity).hasStatusEffect(StatusEffects.BLINDNESS))
            return false;
        if (fluidState.isIn(FluidTags.WATER))
        {
            switch (config.fog.waterFog)
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
            switch (config.fog.lavaFog)
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
            return !config.fog.endFog;
        else if (!dimension.isBedWorking())
            return !config.fog.netherFog;
        return !config.fog.overworldFog;
    }
}
