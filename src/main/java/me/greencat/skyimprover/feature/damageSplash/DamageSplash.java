package me.greencat.skyimprover.feature.damageSplash;

import me.greencat.skyimprover.config.Config;
import me.greencat.skyimprover.event.RenderLivingEntityPreEvent;
import me.greencat.skyimprover.feature.Module;
import me.greencat.skyimprover.utils.TextRenderUtils;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DamageSplash implements Module {
    private static final Pattern pattern = Pattern.compile("[✧✯]?(\\d{1,3}(?:,\\d{3})*[⚔+✧❤♞☄✷ﬗ✯]*)");
    private static final Deque<RenderInformation> damages = new LinkedList<>();
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private static final Random random = new Random();
    @Override
    public void registerEvent() {
        RenderLivingEntityPreEvent.EVENT.register(DamageSplash::onRenderEntity);
        WorldRenderEvents.LAST.register(DamageSplash::onRenderWorld);
    }
    public static boolean onRenderEntity(LivingEntity entity){
        if(!Config.damageSplashEnable){
            return true;
        }
        if(entity instanceof ArmorStandEntity && entity.hasCustomName()){
            String customName = entity.getCustomName().getString();
            Matcher matcher = pattern.matcher(customName == null ? "" : customName);
            if(matcher.matches() && customName != null){
                String damage =  customName.replaceAll( "[^\\d]", "");
                if(Config.damageSplashCompact){
                    try {
                        int damageInteger = Integer.parseInt(damage);
                        if (damageInteger >= 1000 && damageInteger < 1000000) {
                            double damageDouble = damageInteger / 1000.0D;
                            damage = decimalFormat.format(damageDouble) + "K";
                        } else if (damageInteger >= 1000000 && damageInteger < 1000000000) {
                            double damageDouble = damageInteger / 1000000.0D;
                            damage = decimalFormat.format(damageDouble) + "M";
                        } else if (damageInteger >= 1000000000) {
                            double damageDouble = damageInteger / 1000000000.0D;
                            damage = decimalFormat.format(damageDouble) + "B";
                        }
                    } catch(Exception ignored){}
                }
                damages.add(new RenderInformation(entity.getX() + random.nextDouble(Config.damageSplashOffset * 2.0D) - Config.damageSplashOffset, entity.getY() + random.nextDouble(Config.damageSplashOffset * 2.0D) - Config.damageSplashOffset, entity.getZ() + random.nextDouble(Config.damageSplashOffset * 2.0D) - Config.damageSplashOffset,damage,new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255))));
                if (MinecraftClient.getInstance().world != null) {
                    MinecraftClient.getInstance().world.removeEntity(entity.getId(), Entity.RemovalReason.UNLOADED_WITH_PLAYER);
                }
                return false;
            }
        }
        return true;
    }
    public static void onRenderWorld(WorldRenderContext wrc){
        List<RenderInformation> removeList = new ArrayList<>();
        for(RenderInformation info : damages){
            Vec3d pos = new Vec3d(info.x, info.y,info.z);
            float scaling = (float)Math.max(2,Math.log(pos.distanceTo(MinecraftClient.getInstance().player.getPos()) * 3));
            TextRenderUtils.renderText(wrc,Text.literal(info.message).fillStyle(Style.EMPTY.withColor(info.color.getRGB())),pos,System.currentTimeMillis() - info.startTime <= Config.damageSplashAnimationSpeed * 1000 ? (System.currentTimeMillis() - info.startTime) / (Config.damageSplashAnimationSpeed * 1000) * scaling:scaling,true);
            if(System.currentTimeMillis() - info.startTime >= Config.damageSplashDuration * 1000){
                removeList.add(info);
            }
        }
        if(!removeList.isEmpty()) {
            damages.removeAll(removeList);
        }
    }
    static class RenderInformation{
        double x;
        double y;
        double z;
        String message;
        Color color;
        long startTime;
        public RenderInformation(double x,double y,double z,String message,Color color){
            this.x = x;
            this.y = y;
            this.z = z;
            this.message = message;
            this.startTime = System.currentTimeMillis();
            this.color = color;
        }
    }
}
