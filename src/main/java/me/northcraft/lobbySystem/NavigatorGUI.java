package me.northcraft.lobbySystem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class NavigatorGUI {

    public static final String GUI_TITLE = "§f» §b§lNavigator";

    public static Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(null, 27, GUI_TITLE);

        // Reihe 1
        inv.setItem(2, createItem(Material.TNT, "§e§lTNT Run", List.of("§7Renne, bevor der Boden unter dir zerfällt!")));
        inv.setItem(6, createItem(Material.TNT_MINECART, "§6§lTNTCanon", List.of("§7Spreng dich mit Kanonen ans Ziel!")));

        // Reihe 2
        inv.setItem(11, createItem(Material.COD, "§a§lFishslap", List.of("§7Schlage deine Gegner ins Wasser!")));
        inv.setItem(13, createItem(Material.BEACON, "§f§lSpawn", List.of("§7Teleportiere dich zurück zum Spawn.")));
        inv.setItem(15, createItem(Material.WATER_BUCKET, "§4§lJumpwater", List.of("§7Meistere den Sprung ins Wasser!")));

        // Reihe 3
        inv.setItem(20, createItem(Material.PUFFERFISH, "§2§lFishslapFFA", List.of("§7Jeder gegen jeden im Fishslap-Modus!")));
        inv.setItem(24, createItem(Material.BARRIER, "§6§lcoming soon...", List.of("§7Dieser Spielmodus ist noch in Arbeit.")));

        return inv;
    }

    private static ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }
}