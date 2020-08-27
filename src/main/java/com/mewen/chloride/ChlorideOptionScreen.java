package com.mewen.chloride;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
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
import net.minecraft.text.LiteralText;
import net.minecraft.text.StringRenderable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.*;

@Environment(EnvType.CLIENT)
class ChlorideOptionScreen extends GameOptionsScreen
{
    private ButtonListWidget list;
    private List<StringRenderable> tooltip;
    private final ArrayList<AChlorideFeature.AGUIConfiguration> controllers = new ArrayList<>();

    public ChlorideOptionScreen(Screen parent)
    {
        super(parent, null, new TranslatableText("option.chloride.menuTitle.general"));
        Collection<AChlorideFeature> featureList = Chloride.GetFeatureList();
        for (AChlorideFeature feature : featureList)
        {
            AChlorideFeature.AGUIConfiguration gui = feature.GetGUI();
            if (gui != null)
                controllers.add(gui);
        }
    }

    @Override
    public void init()
    {
        this.list = new ButtonListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        for (AChlorideFeature.AGUIConfiguration controller : controllers)
        {
            Option[] options = controller.GetOptions();
            if (options != null && options.length > 0)
            {
                String categoryKey = controller.GetCategoryKey();
                if (categoryKey != null && !categoryKey.isEmpty())
                    this.list.addSingleOptionEntry(new ChlorideSectionTitle(this, categoryKey, controller.GetParent()));
                if (options.length == 1)
                    this.list.addSingleOptionEntry(options[0]);
                else
                    this.list.addAll(options);
            }
        }
        this.children.add(this.list);
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 27, 200, 20, ScreenTexts.DONE, (button) -> {
            Chloride.SaveConfiguration();
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

    @Environment(EnvType.CLIENT)
    private static class ChlorideSectionTitle extends Option
    {
        private final Screen parent;
        private final String textKey;
        private final AChlorideFeature feature;

        ChlorideSectionTitle(Screen parent, String key, AChlorideFeature feature)
        {
            super(key);
            this.parent = parent;
            this.textKey = key;
            this.feature = feature;
        }

        @Override
        public AbstractButtonWidget createButton(GameOptions options, int x, int y, int width)
        {
            return new TextAbstractButtonWidget(x, y, parent.width, 20, new TranslatableText(textKey), feature);
        }

        @Environment(EnvType.CLIENT)
        public static class TextAbstractButtonWidget extends AbstractButtonWidget
        {
            private final Text text;
            private final TextRenderer textRenderer;
            private final ButtonWidget button;
            private final AChlorideFeature feature;
            private final Text onText = new TranslatableText("chloride.button.on");
            private final Text offText = new TranslatableText("chloride.button.off");

            public TextAbstractButtonWidget(int x, int y, int width, int height, Text text, AChlorideFeature feature)
            {
                super(x, y, width, height, LiteralText.EMPTY);
                this.feature = feature;
                this.text = text;
                this.textRenderer = MinecraftClient.getInstance().textRenderer;
                this.button = new ButtonWidget(x, y, 40, 20, GetButtonText(), button1 -> this.OnButtonPress());
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button)
            {
                return this.button.mouseClicked(mouseX, mouseY, button);
            }

            @Override
            public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
            {
                float textWidth = textRenderer.getWidth(text);
                float textX = (this.width - textWidth) / 2 - 20;
                this.textRenderer.drawWithShadow(matrices, text, textX, (float)y + 5, 16777215);
                this.button.x = (int)(textX + textWidth + 10);
                this.button.y = y;
                this.button.render(matrices, mouseX, mouseY, delta);
            }

            private void OnButtonPress()
            {
                feature.SetEnable(!feature.IsEnable());
                button.setMessage(GetButtonText());
            }

            private Text GetButtonText()
            {
                if (feature.IsEnable())
                    return onText;
                return offText;
            }
        }
    }
}
