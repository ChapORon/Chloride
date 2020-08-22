package com.mewen.chloride;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.options.Option;

@Environment(EnvType.CLIENT)
public abstract class AChlorideFeature
{
    protected final Gson gson = new Gson();
    private final AGUIConfiguration gui;

    protected AChlorideFeature(AGUIConfiguration gui) {this.gui = gui;}
    public AGUIConfiguration GetGUI() {return gui;}

    public abstract void OnRegistration();
    public abstract JsonElement SerializeConfiguration();
    public abstract void DeserializeConfiguration(JsonElement json);

    @Environment(EnvType.CLIENT)
    public static abstract class AGUIConfiguration
    {
        private final String categoryKey;

        public AGUIConfiguration(String categoryKey) {this.categoryKey = categoryKey;}

        public abstract Option[] GetOptions();
        public final String GetCategoryKey() {return categoryKey;}
    }

}
