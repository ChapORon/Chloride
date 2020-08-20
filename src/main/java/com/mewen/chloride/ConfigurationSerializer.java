package com.mewen.chloride;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigurationSerializer
{
    private static Gson gson;
    private static Path configPath;

    public static void Init()
    {
        configPath = FabricLoader.getInstance().getConfigDirectory().toPath().resolve("chloride.json");
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public static Configuration Load()
    {
        if (Files.exists(configPath))
        {
            try
            {
                BufferedReader reader = Files.newBufferedReader(configPath);
                Configuration loadedConfiguration = gson.fromJson(reader, Configuration.class);
                reader.close();
                return loadedConfiguration;
            }
            catch (IOException | JsonParseException e)
            {
                return null;
            }
        }
        else
        {
            Configuration newConfiguration = new Configuration();
            Save(newConfiguration);
            return newConfiguration;
        }
    }

    public static boolean Save(Configuration other)
    {
        try
        {
            Files.createDirectories(configPath.getParent());
            BufferedWriter writer = Files.newBufferedWriter(configPath);
            gson.toJson(other, writer);
            writer.close();
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }
}
