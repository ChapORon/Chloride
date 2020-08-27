package com.mewen.chloride.mixin;

import com.mewen.chloride.Chloride;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(OptionsScreen.class)
public abstract class ChlorideMenuMixin extends Screen
{
    protected ChlorideMenuMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void AddChlorideOptionConfigMenu(CallbackInfo ci)
    {
        for (AbstractButtonWidget aButton : this.buttons)
        {
            if (aButton.y != this.height / 6 + 168 && aButton.y >= this.height / 6 + 48 - 6)
                aButton.y -= 10;
        }
        this.addButton(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 134 - 6, 150, 20,
                new TranslatableText("option.chloride.main"), (buttonWidget) -> Chloride.OpenConfigMenu(this)));
    }
}
