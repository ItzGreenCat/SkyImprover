package me.greencat.skyimprover.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class Config extends MidnightConfig {
    @Comment(category = "render")
    public static Comment damageSplash;
    @Entry(category = "render")
    public static boolean damageSplashEnable = true;
    @Entry(category = "render")
    public static boolean damageSplashCompact = true;
    @Entry(category = "render",isSlider = true,min = 0.0F,max = 5.0F)
    public static float damageSplashOffset = 2.0F;
    @Entry(category = "render",isSlider = true,min = 0.0F,max = 10.0F)
    public static float damageSplashDuration = 3.0F;
    @Entry(category = "render",isSlider = true,min = 0.0F,max = 2.0F)
    public static float damageSplashAnimationSpeed = 0.3F;

    @Comment(category = "misc")
    public static Comment rainTimer;
    @Entry(category = "misc")
    public static boolean rainTimerEnable = true;
    @Entry(category = "misc",isSlider = true,min = 0.0F,max = 1.0F)
    public static float rainTimerGuiOffsetX = 0.3F;
    @Entry(category = "misc",isSlider = true,min = 0.0F,max = 1.0F)
    public static float rainTimerGuiOffsetY = 0.3F;

    @Comment(category = "dungeon")
    public static Comment m3FreezeHelper;
    @Entry(category = "dungeon")
    public static boolean m3FreezeHelperEnable = true;

    @Comment(category = "dungeon")
    public static Comment dungeonDeathMessage;
    @Entry(category = "dungeon")
    public static boolean dungeonDeathMessageEnable = true;
    @Entry(category = "dungeon")
    public static String dungeonDeathMessageContent = "BOOM!";
    @Comment(category = "dungeon")
    public static Comment kuudraHelper;
    @Entry(category = "dungeon")
    public static boolean kuudraHelperEnable = true;
}
