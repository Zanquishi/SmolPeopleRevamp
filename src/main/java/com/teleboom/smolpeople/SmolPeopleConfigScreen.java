package com.teleboom.smolpeople;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class SmolPeopleConfigScreen extends Screen {
    private final Screen parent;
    private final SmolPeopleConfig config;
    
    private SliderWidget scaleSlider;
    private ButtonWidget toggleButton;
    
    public SmolPeopleConfigScreen(Screen parent) {
        super(new TranslatableText("gui.smolpeople.config.title"));
        this.parent = parent;
        this.config = SmolPeopleConfig.getInstance();
    }
    
    @Override
    protected void init() {
        super.init();
        
        // Title
        int centerX = this.width / 2;
        int startY = this.height / 4;
        
        // Scale slider
        this.scaleSlider = new SliderWidget(centerX - 100, startY, 200, 20, 
            new TranslatableText("gui.smolpeople.scale", String.format("%.2f", config.scaleFactor)), 
            (config.scaleFactor - 0.1) / 0.9) {
            
            @Override
            protected void updateMessage() {
                double value = 0.1 + (this.value * 0.9); // Range from 0.1 to 1.0
                config.scaleFactor = (float) value;
                this.setMessage(new TranslatableText("gui.smolpeople.scale", String.format("%.2f", value)));
            }
            
            @Override
            protected void applyValue() {
                // Value is applied in updateMessage
            }
        };
        this.addSelectableChild(this.scaleSlider);
        
        // Toggle button
        this.toggleButton = new ButtonWidget(centerX - 100, startY + 35, 200, 20,
            new TranslatableText("gui.smolpeople.enabled", config.enabled ? "ON" : "OFF"),
            button -> {
                config.enabled = !config.enabled;
                button.setMessage(new TranslatableText("gui.smolpeople.enabled", config.enabled ? "ON" : "OFF"));
            });
        this.addSelectableChild(this.toggleButton);
        
        // Done button
        this.addSelectableChild(new ButtonWidget(centerX - 100, startY + 70, 200, 20,
            new TranslatableText("gui.done"),
            button -> {
                config.save();
                this.client.setScreen(this.parent);
            }));
    }
    
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        
        // Draw title
        drawCenteredText(matrices, this.textRenderer, this.title, 
            this.width / 2, this.height / 4 - 40, 0xFFFFFF);
        
        // Draw description
        drawCenteredText(matrices, this.textRenderer, 
            new TranslatableText("gui.smolpeople.description"),
            this.width / 2, this.height / 4 - 20, 0xAAAAAA);
        
        super.render(matrices, mouseX, mouseY, delta);
    }
    
    @Override
    public void onClose() {
        config.save();
        super.onClose();
    }
}
