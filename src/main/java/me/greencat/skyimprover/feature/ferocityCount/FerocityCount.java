package me.greencat.skyimprover.feature.ferocityCount;

import me.greencat.skyimprover.config.Config;
import me.greencat.skyimprover.event.SoundEvent;
import me.greencat.skyimprover.feature.Module;
import me.greencat.skyimprover.utils.TextRenderUtils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.*;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.text.DecimalFormat;

public class FerocityCount implements Module{
    public static int count = 0;
    public static long last = 0L;
    public static final DecimalFormat format = new DecimalFormat("0.0");
    @Override
    public void registerEvent() {
        SoundEvent.EVENT.register(FerocityCount::onSoundPlayed);
        HudRenderCallback.EVENT.register(FerocityCount::onOverlayRendering);
        ClientTickEvents.END_CLIENT_TICK.register(FerocityCount::onTick);
    }

    private static void onTick(MinecraftClient minecraftClient) {
        if(!Config.ferocityCountEnable){
            return;
        }
        if(System.currentTimeMillis() - last >= 5000){
            count = 0;
        }
    }

    private static void onOverlayRendering(DrawContext context, float v) {
        if(!Config.ferocityCountEnable){
            return;
        }
        MutableText prefix = Text.literal(Formatting.RED + "⫽ Ferocity: ");
        MutableText number = Text.literal(Formatting.WHITE + String.valueOf(count));
        MutableText time = Text.literal(Formatting.WHITE + " Hit - " + (System.currentTimeMillis() - last > 5000 ? "5.0s" : format.format(5.0F - (System.currentTimeMillis() - last) / 1000.0F) + "s"));
        Text displayText = prefix.append(number).append(time);
        TextRenderUtils.renderHUDText(context,(int)(context.getScaledWindowWidth() * Config.ferocityCountGuiOffsetX), (int)(context.getScaledWindowHeight() * Config.ferocityCountGuiOffsetY),displayText);
    }


    public static void onSoundPlayed(SoundInstance sound) {
        if(!Config.ferocityCountEnable){
            return;
        }
        String soundName = sound.getSound().getIdentifier().toString();
        if(!soundName.equals("minecraft:mob/zombie/woodbreak")){
            return;
        }
        last = System.currentTimeMillis();
        count++;
    }
}
