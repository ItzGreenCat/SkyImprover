package me.greencat.skyimprover;

import eu.midnightdust.lib.config.MidnightConfig;
import me.greencat.skyimprover.config.Config;
import me.greencat.skyimprover.feature.FeatureLoader;
import me.greencat.skyimprover.feature.damageSplash.DamageSplash;
import me.greencat.skyimprover.feature.dungeonDeathMessage.DungeonDeathMessage;
import me.greencat.skyimprover.feature.kuudraHelper.KuudraHelper;
import me.greencat.skyimprover.feature.m3Freeze.M3FreezeHelper;
import me.greencat.skyimprover.feature.rainTimer.RainTimer;
import me.greencat.skyimprover.utils.LocationUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;


public class SkyImprover implements ClientModInitializer {
    public static final String MODID = "skyimprover";
    @Override
    public void onInitializeClient() {
        MidnightConfig.init(MODID, Config.class);
        FeatureLoader.loadAll("me.greencat.skyimprover.feature");
    }
}
