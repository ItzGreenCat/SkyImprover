package me.greencat.skyimprover.feature.m3Freeze;

import com.mojang.authlib.GameProfile;
import me.greencat.skyimprover.config.Config;
import me.greencat.skyimprover.feature.Module;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public class M3FreezeHelper implements Module {
    private static long lastReceiveTargetMessage = 0L;
    private static boolean isSend = true;
    @Override
    public void registerEvent() {
        ClientReceiveMessageEvents.ALLOW_GAME.register(M3FreezeHelper::onChat);
        ClientTickEvents.END_CLIENT_TICK.register(M3FreezeHelper::onTick);
    }

    private static void onTick(MinecraftClient client) {
        if(System.currentTimeMillis() - lastReceiveTargetMessage >= 5250 && !isSend){
            isSend = true;
            MinecraftClient.getInstance().inGameHud.setTitle(Text.literal(Formatting.RED + "Freeze!"));
        }
    }

    public static boolean onChat(Text text, boolean overlay){
        if(!Config.m3FreezeHelperEnable){
            return true;
        }
        String message = text.getString();
        if(message.contains("You found my Guardians' one weakness?")){
            lastReceiveTargetMessage = System.currentTimeMillis();
            isSend = false;
        }
        return true;
    }

}
