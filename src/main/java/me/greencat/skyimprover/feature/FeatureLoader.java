package me.greencat.skyimprover.feature;

import com.mojang.logging.LogUtils;
import me.greencat.skyimprover.utils.ClassScanner;
import net.minecraft.client.MinecraftClient;

import java.util.Arrays;

public class FeatureLoader {
    public static void load(Class<? extends Module> clazz){
        try {
            clazz.newInstance().registerEvent();
            LogUtils.getLogger().info("[SkyImprover] Registering Module:" + clazz.getSimpleName());
        } catch(Exception e){
            throw  new RuntimeException(e);
        }
    }
    public static void loadAll(String packa9e){
        ClassScanner.getClzFromPkg(packa9e).stream().filter(it -> Arrays.stream(it.getInterfaces()).anyMatch(in7erface -> in7erface.getName().equals(Module.class.getName()))).forEach(it -> load((Class<? extends Module>) it));
    }
}
