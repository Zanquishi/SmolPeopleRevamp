package com.teleboom.smolpeople;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class SmolPeopleClient implements ClientModInitializer {
    public static final String MOD_ID = "smolpeople";
    
    public static KeyBinding configKeyBinding;
    
    @Override
    public void onInitializeClient() {
        // Register keybinding
        configKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.smolpeople.config",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "category.smolpeople"
        ));
        
        // Register tick event for keybinding
        net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (configKeyBinding.wasPressed()) {
                client.setScreen(new SmolPeopleConfigScreen(client.currentScreen));
            }
        });
        
        System.out.println("SmolPeople mod initialized!");
    }
}
