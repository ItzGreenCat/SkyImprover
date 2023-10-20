package me.greencat.skyimprover;

import eu.midnightdust.lib.config.MidnightConfig;
import me.greencat.skyimprover.config.Config;
import me.greencat.skyimprover.feature.FeatureLoader;
import me.greencat.skyimprover.feature.damageSplash.DamageSplash;
import me.greencat.skyimprover.feature.rainTimer.RainTimer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;


public class SkyImprover implements ClientModInitializer {
    public static final String MODID = "skyimprover";
    @Override
    public void onInitializeClient() {
        MidnightConfig.init(MODID, Config.class);
        FeatureLoader.load(DamageSplash.class);
        FeatureLoader.load(RainTimer.class);
    }
}
