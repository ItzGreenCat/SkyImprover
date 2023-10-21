package me.greencat.skyimprover.utils;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.scoreboard.*;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Collections;
import java.util.List;

public class LocationUtils {
    public static boolean isOnSkyblock = false;
    public static boolean isInDungeons = false;
    public static boolean isInKuudra = false;
    public static final ObjectArrayList<String> STRING_SCOREBOARD = new ObjectArrayList<>();

    public static void update(){
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        updateScoreboard(minecraftClient);
        updatePlayerPresenceFromScoreboard(minecraftClient);
    }
    public static void updatePlayerPresenceFromScoreboard(MinecraftClient client) {
        List<String> sidebar = STRING_SCOREBOARD;

        FabricLoader fabricLoader = FabricLoader.getInstance();
        if (client.world == null || client.isInSingleplayer() || sidebar.isEmpty()) {
            isOnSkyblock = false;
            isInDungeons = false;
            isInKuudra = false;
        }

        if (sidebar.isEmpty() && !fabricLoader.isDevelopmentEnvironment()) return;
        String string = sidebar.toString();
            if (sidebar.get(0).contains("SKYBLOCK") || sidebar.get(0).contains("SKIBLOCK")) {
                if (!isOnSkyblock) {
                    isOnSkyblock = true;
                }
            } else {
                isOnSkyblock = false;
            }
            isInDungeons = isOnSkyblock && string.contains("The Catacombs");
            isInKuudra = isOnSkyblock && string.contains("Kuudra");
    }
    private static void updateScoreboard(MinecraftClient client) {
        try {
            STRING_SCOREBOARD.clear();

            ClientPlayerEntity player = client.player;
            if (player == null) return;

            Scoreboard scoreboard = player.getScoreboard();
            ScoreboardObjective objective = scoreboard.getObjectiveForSlot(1);
            ObjectArrayList<Text> textLines = new ObjectArrayList<>();
            ObjectArrayList<String> stringLines = new ObjectArrayList<>();

            for (ScoreboardPlayerScore score : scoreboard.getAllPlayerScores(objective)) {
                Team team = scoreboard.getPlayerTeam(score.getPlayerName());

                if (team != null) {
                    Text textLine = Text.empty().append(team.getPrefix().copy()).append(team.getSuffix().copy());
                    String strLine = team.getPrefix().getString() + team.getSuffix().getString();

                    if (!strLine.trim().isEmpty()) {
                        String formatted = Formatting.strip(strLine);

                        textLines.add(textLine);
                        stringLines.add(formatted);
                    }
                }
            }

            if (objective != null) {
                stringLines.add(objective.getDisplayName().getString());
                textLines.add(Text.empty().append(objective.getDisplayName().copy()));

                Collections.reverse(stringLines);
                Collections.reverse(textLines);
            }
            STRING_SCOREBOARD.addAll(stringLines);
        } catch (NullPointerException ignored) {

        }
    }
}
