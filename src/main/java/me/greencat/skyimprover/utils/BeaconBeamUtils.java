package me.greencat.skyimprover.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import me.greencat.skyimprover.mixin.BeaconBlockEntityRendererAccessor;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

public class BeaconBeamUtils {
    public static void renderBeaconBeam(WorldRenderContext context, BlockPos pos, Color color) {
        float[] colorComponents = new float[]{color.getRed() / 255.0F,color.getGreen() / 255.0F,color.getBlue() / 255.0F};
        if (FrustumUtils.isVisible(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, 319, pos.getZ() + 1)) {
            MatrixStack matrices = context.matrixStack();
            Vec3d camera = context.camera().getPos();

            matrices.push();
            matrices.translate(pos.getX() - camera.getX(), pos.getY() - camera.getY(), pos.getZ() - camera.getZ());

            Tessellator tessellator = RenderSystem.renderThreadTesselator();
            BufferBuilder buffer = tessellator.getBuffer();
            VertexConsumerProvider.Immediate consumer = VertexConsumerProvider.immediate(buffer);

            BeaconBlockEntityRendererAccessor.renderBeam(matrices, consumer, context.tickDelta(), context.world().getTime(), 0,319, colorComponents);

            consumer.draw();
            matrices.pop();
        }
    }

}
