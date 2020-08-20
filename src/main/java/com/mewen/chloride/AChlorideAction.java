package com.mewen.chloride;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public abstract class AChlorideAction
{
    private KeyBinding actionKey = new KeyBinding("key.chloride.menu",InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "category.chloride");

    public AChlorideAction(String translationKey, int code)
    {
        actionKey = new KeyBinding(translationKey, InputUtil.Type.KEYSYM, code, "category.chloride");
        KeyBindingHelper.registerKeyBinding(actionKey);
        ClientTickCallback.EVENT.register(client -> {
            if (actionKey.wasPressed()) {
                OnAction(client);
            }
        });
    }

    public abstract void OnAction(MinecraftClient client);
}
