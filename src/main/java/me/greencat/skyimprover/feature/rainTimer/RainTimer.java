package me.greencat.skyimprover.feature.rainTimer;

import me.greencat.skyimprover.config.Config;
import me.greencat.skyimprover.feature.Module;
import me.greencat.skyimprover.utils.TextRenderUtils;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RainTimer implements Module {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    private static long lastRefresh = 0L;

    private static final int cooldown = 2400;
    private static final int duration = 1200;
    private static final int thunderstormInterval = 3;

    private static String displayTimeLeft = secsToTime(0);
    private static String displayNextRain = secsToTime(0);

    private static boolean netxIsStormCache;
    private static boolean thunderStormNowCache;
    private static boolean rainNowCache;

    @Override
    public void registerEvent() {
        HudRenderCallback.EVENT.register(RainTimer::onOverlayRendering);
    }
    public static void onOverlayRendering(DrawContext context,float tick){
        if(!Config.rainTimerEnable){
            return;
        }
        Text displayText = null;
        if(netxIsStormCache){
            displayText = Text.translatable("rainTimer.hud.NextThunder").append(Text.literal(": " + displayNextRain + "⚡")).fillStyle(Style.EMPTY.withColor(Formatting.GOLD));
        } else if(thunderStormNowCache){
            displayText = Text.translatable("rainTimer.hud.rainLeft").append(Text.literal(": " + displayTimeLeft + "⚡")).fillStyle(Style.EMPTY.withColor(Formatting.GOLD));
        } else if(rainNowCache){
            displayText = Text.translatable("rainTimer.hud.rainLeft").append(Text.literal(": " + displayTimeLeft)).fillStyle(Style.EMPTY.withColor(Formatting.AQUA));
        } else {
            displayText = Text.translatable("rainTimer.hud.NextRain").append(Text.literal(": " + displayNextRain));
        }
        TextRenderUtils.renderHUDText(context,(int)(context.getScaledWindowWidth() * Config.rainTimerGuiOffsetX), (int)(context.getScaledWindowHeight() * Config.rainTimerGuiOffsetY),displayText);
        if(System.currentTimeMillis() - lastRefresh <= 1000L){
            return;
        }
        lastRefresh = System.currentTimeMillis();

        long timestamp = (long) Math.floor(System.currentTimeMillis() / 1000.0D);
        long skyblockAge = (timestamp - 1560275700);

        long thunderstorm = skyblockAge % ((cooldown + duration) * thunderstormInterval);
        long rain = skyblockAge % (cooldown + duration);

        boolean rainNow = false;
        if (cooldown <= rain) {
            rainNow = true;
            long timeLeft = (cooldown + duration) - rain;
            displayTimeLeft = secsToTime(timeLeft);
            displayNextRain = secsToTime(timeLeft + cooldown);
        } else {
            rainNow = false;
            displayNextRain = secsToTime(cooldown - rain);
        }
        boolean thunderStormNow = false;
        boolean netxIsStorm = false;
        if ((cooldown <= thunderstorm) && (thunderstorm < (cooldown + duration))) {
            thunderStormNow = true;
            long timeLeft = (cooldown + duration) - rain;
            displayTimeLeft = secsToTime(timeLeft);
        } else {
            thunderStormNow = false;
            long nextThunderstorm = 0;
            if (thunderstorm < cooldown) {
                nextThunderstorm = cooldown - thunderstorm;
            } else if ((cooldown + duration) <= thunderstorm) {
                nextThunderstorm = ((cooldown + duration) * thunderstormInterval) - thunderstorm + cooldown;
            }
            if(nextThunderstorm == cooldown - rain){
                netxIsStorm = true;
                displayNextRain = secsToTime(nextThunderstorm);
            } else {
                netxIsStorm = false;
            }
        }
        thunderStormNowCache = thunderStormNow;
        rainNowCache = rainNow;
        netxIsStormCache = netxIsStorm;
    }
    private static String secsToTime(long seconds) {
        return sdf.format(new Date(seconds * 1000)).substring(14, 19);
    }
}
