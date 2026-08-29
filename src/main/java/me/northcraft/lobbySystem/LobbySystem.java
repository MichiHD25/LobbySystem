package me.northcraft.lobbySystem;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public final class LobbySystem extends JavaPlugin {

    @Override
    public void onEnable() {
        // Config-Datei initialisieren
        saveDefaultConfig();

        // BungeeCord/Velocity-Messaging-Channel registrieren
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        // Listener für Events & Interaktionen registrieren
        getServer().getPluginManager().registerEvents(new LobbyListener(this), this);

        // RealTime & RealWeather System für Kiel starten
        RealSyncManager syncManager = new RealSyncManager(this);
        syncManager.startSync();

        // Command zum Setzen des Spawns registrieren
        if (getCommand("setspawn") != null) {
            getCommand("setspawn").setExecutor((sender, command, label, args) -> {
                if (sender instanceof org.bukkit.entity.Player player) {
                    saveSpawn(player.getLocation());
                    player.sendMessage("§aSpawn-Punkt wurde erfolgreich gespeichert!");
                    return true;
                }
                return false;
            });
        }
    }

    @Override
    public void onDisable() {
        // Eventuelle aufräumende Logik beim Stoppen des Servers
    }

    // Hilfsmethode: Speichert die Koordinaten & Blickrichtung in der config.yml
    public void saveSpawn(Location loc) {
        getConfig().set("spawn.world", loc.getWorld().getName());
        getConfig().set("spawn.x", loc.getX());
        getConfig().set("spawn.y", loc.getY());
        getConfig().set("spawn.z", loc.getZ());
        getConfig().set("spawn.yaw", loc.getYaw());
        getConfig().set("spawn.pitch", loc.getPitch());
        saveConfig();
    }

    // Hilfsmethode: Liest die Spawn-Location aus der config.yml aus
    public Location getSpawn() {
        if (!getConfig().contains("spawn.world") || getConfig().getString("spawn.world") == null) {
            return null;
        }
        return new Location(
                getServer().getWorld(getConfig().getString("spawn.world")),
                getConfig().getDouble("spawn.x"),
                getConfig().getDouble("spawn.y"),
                getConfig().getDouble("spawn.z"),
                (float) getConfig().getDouble("spawn.yaw"),
                (float) getConfig().getDouble("spawn.pitch")
        );
    }
}