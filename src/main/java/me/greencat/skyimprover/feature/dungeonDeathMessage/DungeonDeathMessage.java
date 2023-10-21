package me.greencat.skyimprover.feature.dungeonDeathMessage;

import me.greencat.skyimprover.config.Config;
import me.greencat.skyimprover.feature.Module;
import me.greencat.skyimprover.utils.LocationUtils;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class DungeonDeathMessage implements Module {
    @Override
    public void registerEvent() {
        ClientReceiveMessageEvents.ALLOW_GAME.register(DungeonDeathMessage::onChat);
    }

    private static boolean onChat(Text text, boolean overlay) {
        if(!Config.dungeonDeathMessageEnable){
            return true;
        }
        LocationUtils.update();
        if(!LocationUtils.isInDungeons){
            return true;
        }
        String message = text.getString();
        if(message.contains("☠") && !message.contains("Crit Damage")){
            if (MinecraftClient.getInstance().player != null) {
                MinecraftClient.getInstance().player.networkHandler.sendChatMessage(Config.dungeonDeathMessageContent);
            }
        }
        return true;
    }
}
