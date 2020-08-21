package com.mewen.chloride.gui;

import com.mewen.chloride.Chloride;
import com.mewen.chloride.Configuration;
import com.mewen.chloride.ConfigurationSerializer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.options.GameOptionsScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.Option;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringRenderable;
import net.minecraft.text.TranslatableText;

import java.util.List;
import java.util.Optional;

public class ChlorideOptionScreen extends GameOptionsScreen
{
    private ButtonListWidget list;
    private List<StringRenderable> tooltip;
    private Configuration config;

    public static final Option[] FOG_OPTIONS;

    static
    {
        FOG_OPTIONS = new Option[]{ChlorideOptions.NETHER_FOG, ChlorideOptions.OVERWORLD_FOG, ChlorideOptions.END_FOG, ChlorideOptions.WATER_FOG_TYPE, ChlorideOptions.LAVA_FOG_TYPE};
    }

    public ChlorideOptionScreen(Screen parent, GameOptions gameOptions, Configuration config)
    {
        super(parent, gameOptions, new TranslatableText("option.chloride.menuTitle.general"));
        this.config = config;
    }

    @Override
    public void init()
    {
        ChlorideConfigurationController.SetConfiguration(config);
        this.list = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.list.addSingleOptionEntry(ChlorideOptions.ZOOM);
        this.list.addAll(FOG_OPTIONS);
        this.children.add(this.list);
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, (button) -> {
            this.config = ChlorideConfigurationController.GetConfiguration();
            Chloride.UpdateConfiguration(this.config);
            ConfigurationSerializer.Save(this.config);
            if (client != null)
                client.openScreen(this.parent);
        }));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
    {
        this.tooltip = null;
        Optional<AbstractButtonWidget> optional = this.list.getHoveredButton(mouseX, mouseY);
        if (optional.isPresent() && optional.get() instanceof OptionButtonWidget)
        {
            Optional<List<StringRenderable>> optional2 = ((OptionButtonWidget)optional.get()).getOption().getTooltip();
            optional2.ifPresent((tooltip) -> this.tooltip = tooltip);
        }

        this.renderBackground(matrices);
        this.list.render(matrices, mouseX, mouseY, delta);
        this.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 5, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
        if (this.tooltip != null)
            this.renderTooltip(matrices, this.tooltip, mouseX, mouseY);
    }
}
