package com.mewen.chloride;

import com.google.gson.*;
import com.mewen.chloride.features.ChlorideFogController;
import com.mewen.chloride.features.ChlorideZoom;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.GameOptions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class Chloride implements ClientModInitializer
{
    private final static Path configPath = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("chloride.json");
    private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final static HashMap<String, AChlorideFeature> features = new HashMap<>();

    static
    {
        RegisterFeature("fog", new ChlorideFogController());
        RegisterFeature("zoom", new ChlorideZoom());
    }

    @Override
    public void onInitializeClient()
    {
        if (Files.exists(configPath))
        {
            try
            {
                BufferedReader reader = Files.newBufferedReader(configPath);
                String line;
                StringBuilder content = new StringBuilder();
                while ((line = reader.readLine()) != null)
                    content.append(line);
                reader.close();
                JsonObject jsonData = new JsonParser().parse(content.toString()).getAsJsonObject();
                Set<String> keys = features.keySet();
                for (String featureName : keys)
                {
                    JsonElement featureJsonConfig = jsonData.get(featureName);
                    AChlorideFeature feature = features.get(featureName);
                    if (feature != null && featureJsonConfig != null)
                    {
                        JsonObject featureJsonConfigObject = featureJsonConfig.getAsJsonObject();
                        JsonElement featureEnableElement = featureJsonConfigObject.remove("enable");
                        if (featureEnableElement != null)
                            feature.SetEnable(featureEnableElement.getAsBoolean());
                        feature.DeserializeConfiguration(featureJsonConfigObject);
                    }
                }
            }
            catch (IOException | JsonParseException ignored) {}
        }
    }

    public static void RegisterFeature(String name, AChlorideFeature feature)
    {
        if (!features.containsKey(name))
        {
            feature.OnRegistration();
            features.put(name, feature);
        }
    }

    public static void SaveConfiguration()
    {
        try
        {
            Files.createDirectories(configPath.getParent());
            BufferedWriter writer = Files.newBufferedWriter(configPath);
            JsonObject configurationObject = new JsonObject();
            Set<String> keys = features.keySet();
            for (String featureName : keys)
            {
                AChlorideFeature feature = features.get(featureName);
                if (feature != null)
                {
                    JsonElement element = feature.SerializeConfiguration();
                    if (element != null)
                    {
                        JsonObject featureObject = element.getAsJsonObject();
                        featureObject.add("enable", new JsonPrimitive(feature.IsEnable()));
                        configurationObject.add(featureName, featureObject);
                    }
                }
            }
            gson.toJson(configurationObject, writer);
            writer.close();
        }
        catch (IOException ignored) {}

    }

    static Collection<AChlorideFeature> GetFeatureList() { return features.values();}

    public static void OpenConfigMenu(Screen parent) {MinecraftClient.getInstance().openScreen(new ChlorideOptionScreen(parent));}

    @SuppressWarnings("unchecked")
    public static <T extends AChlorideFeature> T GetFeature(String name)
    {
        if (!features.containsKey(name))
            return null;
        AChlorideFeature feature = features.get(name);
        if (feature.IsEnable())
            return (T)feature;
        return null;
    }
}
