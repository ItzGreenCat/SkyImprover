package me.greencat.skyimprover.feature.kuudraHelper;

import me.greencat.skyimprover.config.Config;
import me.greencat.skyimprover.event.RenderLivingEntityPreEvent;
import me.greencat.skyimprover.feature.Module;
import me.greencat.skyimprover.utils.BeaconBeamUtils;
import me.greencat.skyimprover.utils.LocationUtils;
import me.greencat.skyimprover.utils.TextRenderUtils;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.awt.*;

public class KuudraHelper implements Module {
    public static long lastRefresh = 0L;
    @Override
    public void registerEvent() {
        //SUPPLIES
        //BRING SUPPLY CHEST HERE
        //SUPPLIES RECEIVED
        //SUPPLY PILE
        //PROGRESS:
        //FUEL CELL
        WorldRenderEvents.LAST.register(KuudraHelper::onRender);
    }

    private static void onRender(WorldRenderContext worldRenderContext) {
        if(System.currentTimeMillis() - lastRefresh >= 5000){
            lastRefresh = System.currentTimeMillis();
            LocationUtils.update();
        }
        if(!LocationUtils.isInKuudra){
            return;
        }
        if(!Config.kuudraHelperEnable){
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = client.world;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if(world == null || player == null){
            return;
        }
        for(Entity entity : world.getEntities()){
            if(!(entity instanceof ArmorStandEntity armorStand)){
                continue;
            }
            if(!armorStand.hasCustomName()){
                continue;
            }
            if(armorStand.getCustomName() != null) {
                String name = armorStand.getCustomName().getString();
                if(name.contains("SUPPLIES")){
                    BeaconBeamUtils.renderBeaconBeam(worldRenderContext,armorStand.getBlockPos(),Color.RED);
                }
                if(name.contains("BRING SUPPLY CHEST HERE")){
                    BeaconBeamUtils.renderBeaconBeam(worldRenderContext,armorStand.getBlockPos(),Color.WHITE);
                }
                if(name.contains("SUPPLIES RECEIVED")){
                    BeaconBeamUtils.renderBeaconBeam(worldRenderContext,armorStand.getBlockPos(),Color.GREEN);
                }
                if(name.contains("PROGRESS:")){
                    float scaling = (float)Math.max(2,Math.log(armorStand.getPos().distanceTo(MinecraftClient.getInstance().player.getPos()) * 5));
                    BeaconBeamUtils.renderBeaconBeam(worldRenderContext,armorStand.getBlockPos(),Color.CYAN);
                    TextRenderUtils.renderText(worldRenderContext, Text.literal(name.replace("PROGRESS:","")).formatted(Formatting.AQUA),armorStand.getPos(),scaling,true);
                }
                if(name.contains("FUEL CELL")){
                    BeaconBeamUtils.renderBeaconBeam(worldRenderContext,armorStand.getBlockPos(),Color.YELLOW);
                }
            }
        }
    }

}
