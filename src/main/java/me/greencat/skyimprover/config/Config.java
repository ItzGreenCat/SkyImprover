package me.greencat.skyimprover.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class Config extends MidnightConfig {
    @Entry(category = "render")
    public static boolean damageSplashEnable = true;
    @Entry(category = "render",isSlider = true,min = 0.0F,max = 10.0F)
    public static float damageSplashDuration = 3.0F;
    @Entry(category = "render",isSlider = true,min = 0.0F,max = 2.0F)
    public static float damageSplashAnimationSpeed = 0.3F;
}