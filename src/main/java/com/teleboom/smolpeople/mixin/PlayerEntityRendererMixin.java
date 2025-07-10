package com.teleboom.smolpeople.mixin;

import com.teleboom.smolpeople.SmolPeopleConfig;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    private boolean shouldScale = false;
    
    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderStart(AbstractClientPlayerEntity player, float yaw, float tickDelta, 
                              MatrixStack matrices, VertexConsumerProvider vertexConsumers, 
                              int light, CallbackInfo ci) {
        // Only scale if we're NOT in first-person view and mod is enabled
        if (!isFirstPersonView() && SmolPeopleConfig.getInstance().enabled) {
            float scaleFactor = SmolPeopleConfig.getInstance().getScaleFactor();
            if (scaleFactor != 1.0f) {
                shouldScale = true;
                matrices.push();
                matrices.scale(scaleFactor, scaleFactor, scaleFactor);
                
                // Adjust Y position to keep feet on ground
                matrices.translate(0, 0, 0);
            }
        }
    }
    
    @Inject(method = "render", at = @At("TAIL"))
    private void onRenderEnd(AbstractClientPlayerEntity player, float yaw, float tickDelta, 
                            MatrixStack matrices, VertexConsumerProvider vertexConsumers, 
                            int light, CallbackInfo ci) {
        if (shouldScale) {
            matrices.pop();
            shouldScale = false;
        }
    }
    
    private boolean isFirstPersonView() {
        net.minecraft.client.MinecraftClient client = net.minecraft.client.MinecraftClient.getInstance();
        return client.options.perspective == 0; // 0 = first person, 1 = third person back, 2 = third person front
    }
}
