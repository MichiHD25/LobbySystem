package me.northcraft.lobbySystem;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public class TablistManager {

    public static void setTablist(Player player) {
        var header = LegacyComponentSerializer.legacySection().deserialize("\n§b§lNORTH§f§lCRAFT§4.§lME\n");
        var footer = LegacyComponentSerializer.legacySection().deserialize("\n§bWillkommen in Föhr!\n");

        player.sendPlayerListHeaderAndFooter(header, footer);
    }
}