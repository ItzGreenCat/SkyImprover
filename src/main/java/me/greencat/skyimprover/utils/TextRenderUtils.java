package me.greencat.skyimprover.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class TextRenderUtils {
    public static final int backgroundColor = new Color(0,0,0,100).getRGB();
    public static void renderText(WorldRenderContext context, Text text, Vec3d pos, boolean seeThrough) {
        renderText(context, text, pos, 1, seeThrough);
    }

    public static void renderText(WorldRenderContext context, Text text, Vec3d pos, float scale, boolean seeThrough) {
        renderText(context, text, pos, scale, 0, seeThrough);
    }

    public static void renderText(WorldRenderContext context, Text text, Vec3d pos, float scale, float yOffset, boolean seeThrough) {
        renderText(context, text.asOrderedText(), pos, scale, yOffset, seeThrough);
    }
    public static void renderText(WorldRenderContext context, OrderedText text, Vec3d pos, float scale, float yOffset, boolean seeThrough) {
        MatrixStack matrices = context.matrixStack();
        Vec3d camera = context.camera().getPos();
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        scale *= 0.025f;

        matrices.push();
        matrices.translate(pos.getX() - camera.getX(), pos.getY() - camera.getY(), pos.getZ() - camera.getZ());
        matrices.peek().getPositionMatrix().mul(RenderSystem.getModelViewMatrix());
        matrices.multiply(context.camera().getRotation());
        matrices.scale(-scale, -scale, scale);

        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
        float xOffset = -textRenderer.getWidth(text) / 2f;

        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder buffer = tessellator.getBuffer();
        VertexConsumerProvider.Immediate consumers = VertexConsumerProvider.immediate(buffer);

        RenderSystem.depthFunc(seeThrough ? GL11.GL_ALWAYS : GL11.GL_LEQUAL);

        textRenderer.draw(text, xOffset, yOffset, 0xFFFFFFFF, false, positionMatrix, consumers, TextRenderer.TextLayerType.SEE_THROUGH, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE);
        consumers.draw();

        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        matrices.pop();
    }
    public static void renderHUDText(DrawContext context,int x,int y,Text... text){
        int height = text.length * 10;
        int width = 0;
        for(Text t : text){
            int length = MinecraftClient.getInstance().textRenderer.getWidth(t.asOrderedText());
            if(length > width){
                width = length;
            }
        }
        context.fill(x - 3,y - 3,x + width + 3,y + height + 3,backgroundColor);
        for(int i = 0;i < text.length;i++){
            context.drawText(MinecraftClient.getInstance().textRenderer,text[i],x,y + i * 10,Color.WHITE.getRGB(),false);
        }
    }
}
