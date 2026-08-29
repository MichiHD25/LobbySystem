package me.northcraft.lobbySystem;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class LobbyListener implements Listener {

    private final LobbySystem plugin;

    public LobbyListener(LobbySystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Join-Nachricht stummschalten
        event.setJoinMessage(null);

        // Spieler-Status zurücksetzen
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.getInventory().clear();

        // Teleport zum Spawn, falls gesetzt
        if (plugin.getSpawn() != null) {
            player.teleport(plugin.getSpawn());
        }

        // --- Tablist & Scoreboard setzen ---
        TablistManager.setTablist(player);
        ScoreboardManager.setScoreboard(player);

        // Navigator-Kompass erstellen
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta meta = compass.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6§lNavigator §7(Rechtsklick)");
            compass.setItemMeta(meta);
        }

        // Slot 4 ist die Mitte der Hotbar
        player.getInventory().setItem(4, compass);

        // --- Title-Einblendung ---
        player.sendTitle("§f§lMoin Moin!", "§bWillkommen im echten Norden.", 10, 70, 20);

        // --- Chat-Nachricht ---
        String border = "§7§m-----------------------------------------------------";
        player.sendMessage(border);
        player.sendMessage("§b§lNORTH§f§lCRAFT§4.§lME» §fMoin, §b" + player.getName() + "§f!");
        player.sendMessage("§fWillkommen im §e\"echten Norden\"§f auf unserem Netzwerk.");
        player.sendMessage("§fNutze den §bKompass§f, um zu den Minigames zu reisen.");
        player.sendMessage(border);

        // --- XP Soundeffekt (langsamer/tiefer bei Pitch 0.7f) ---
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

        // --- Feuerwerk beim Betreten ---
        spawnJoinFirework(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        // Leave-Nachricht stummschalten
        event.setQuitMessage(null);
    }

    private void spawnJoinFirework(Player player) {
        Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        fireworkMeta.addEffect(FireworkEffect.builder()
                .with(FireworkEffect.Type.BALL)
                .withColor(Color.AQUA, Color.ORANGE)
                .withFade(Color.WHITE)
                .flicker(true)
                .build());

        fireworkMeta.setPower(1);
        firework.setFireworkMeta(fireworkMeta);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.COMPASS && item.hasItemMeta()) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                event.setCancelled(true);
                player.openInventory(NavigatorGUI.getInventory());
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(NavigatorGUI.GUI_TITLE)) return;

        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack current = event.getCurrentItem();
        if (current == null || current.getType() == Material.AIR) return;

        switch (current.getType()) {
            case TNT -> sendToServer(player, "tntrun");
            case TNT_MINECART -> sendToServer(player, "tntcanon");
            case COD -> sendToServer(player, "fishing_slap"); // <--- Namensanpassung für den Proxy
            case BEACON -> {
                if (plugin.getSpawn() != null) {
                    player.teleport(plugin.getSpawn());
                    player.sendMessage("§aDu wurdest zum Spawn teleportiert.");
                } else {
                    player.sendMessage("§cDer Spawn-Punkt wurde noch nicht gesetzt! (/setspawn)");
                }
                player.closeInventory();
            }
            case WATER_BUCKET -> sendToServer(player, "jumpwater");
            case PUFFERFISH -> sendToServer(player, "fishslapffa");
            case BARRIER -> {
                player.sendMessage("§cDieser Server ist aktuell noch nicht verfügbar!");
                player.closeInventory();
            }
        }
    }

    private void sendToServer(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

    // --- Lobby-Schutz-System ---

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (!event.getPlayer().hasPermission("lobby.build")) event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().hasPermission("lobby.build")) event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!event.getPlayer().hasPermission("lobby.build")) event.setCancelled(true);
    }
}