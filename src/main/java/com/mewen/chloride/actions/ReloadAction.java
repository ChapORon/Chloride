package com.mewen.chloride.actions;

import com.mewen.chloride.AChlorideAction;
import com.mewen.chloride.Chloride;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;
import org.lwjgl.glfw.GLFW;

public class ReloadAction extends AChlorideAction
{
    public ReloadAction() {super("", GLFW.GLFW_KEY_R);}

    @Override
    public void OnAction(MinecraftClient client)
    {
        Chloride.ReloadConfiguration();
        assert client.player != null;
        client.player.sendMessage(new TranslatableText("text.chloride.reloaded"), false);
    }
}
