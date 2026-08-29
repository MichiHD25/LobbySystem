package me.northcraft.lobbySystem;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RealSyncManager {

    private final LobbySystem plugin;

    public RealSyncManager(LobbySystem plugin) {
        this.plugin = plugin;
    }

    public void startSync() {
        World world = Bukkit.getWorlds().get(0);
        if (world != null) {
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        }

        // Live-Uhrzeit (jede Sekunde synchronisieren)
        Bukkit.getScheduler().runTaskTimer(plugin, this::syncTime, 0L, 20L);

        // Live-Wetter für Kiel (alle 10 Minuten abfragen)
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::fetchKielWeather, 0L, 20L * 60 * 10);
    }

    private void syncTime() {
        LocalTime now = LocalTime.now(ZoneId.of("Europe/Berlin"));
        int hour = now.getHour();
        int minute = now.getMinute();

        // 06:00 Uhr entspricht 0 Ticks in Minecraft (Sonnenaufgang)
        long ticks = ((hour - 6 + 24) % 24) * 1000L + (minute * 1000L / 60L);

        for (World world : Bukkit.getWorlds()) {
            world.setTime(ticks);
        }
    }

    private void fetchKielWeather() {
        try {
            // Kiel-Koordinaten via Open-Meteo API
            URL url = new URL("https://api.open-meteo.com/v1/forecast?latitude=54.3213&longitude=10.1348&current_weather=true");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                int weatherCode = parseWeatherCode(response.toString());
                boolean isRaining = isRainCode(weatherCode);

                // Wetteränderung im Haupt-Thread ausführen
                Bukkit.getScheduler().runTask(plugin, () -> {
                    for (World world : Bukkit.getWorlds()) {
                        world.setStorm(isRaining);
                        world.setThundering(weatherCode >= 95);
                    }
                });
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Kieler Wetter konnte nicht abgerufen werden: " + e.getMessage());
        }
    }

    private int parseWeatherCode(String json) {
        // Sucht explizit nach "weathercode": gefolgt von rein numerischen Ziffern
        Pattern pattern = Pattern.compile("\"weathercode\"\\s*:\\s*([0-9]+)");
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return -1;
    }

    private boolean isRainCode(int code) {
        // WMO-Codes für Nieselregen, Regen, Schauer & Gewitter
        return (code >= 51 && code <= 67) || (code >= 80 && code <= 82) || (code >= 95);
    }
}