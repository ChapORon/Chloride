package com.mewen.chloride;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;

public abstract class AChlorideAction
{
    private final KeyBinding actionKey;

    public AChlorideAction(String translationKey, int code)
    {
        actionKey = new KeyBinding(translationKey, InputUtil.Type.KEYSYM, code, "category.chloride");
        KeyBindingHelper.registerKeyBinding(actionKey);
        ClientTickCallback.EVENT.register(new ChlorideActionCallback());
    }

    private class ChlorideActionCallback implements ClientTickCallback
    {
        private boolean lastStatePressed = false;
        @Override
        public void tick(MinecraftClient minecraftClient)
        {
            boolean currentStatePressed = actionKey.isPressed();
            if (!currentStatePressed && lastStatePressed)
            {
                OnAction(minecraftClient);
            }
            lastStatePressed = currentStatePressed;
        }
    }

    public abstract void OnAction(MinecraftClient client);
}
