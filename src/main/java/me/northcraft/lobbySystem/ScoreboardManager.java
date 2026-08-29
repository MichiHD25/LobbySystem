package me.northcraft.lobbySystem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardManager {

    public static void setScoreboard(Player player) {
        org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return;

        Scoreboard board = manager.getNewScoreboard();

        // Titel: NORTHCRAFT in Cyan/Bold, .ME in Dunkelrot
        Objective obj = board.registerNewObjective("northcraft", Criteria.DUMMY, "§b§lNORTH§f§lCRAFT§4.§lME");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Zeilenstruktur basierend auf den Score-Indizes aus dem Screenshot
        setScore(obj, "§1", 15);
        setScore(obj, "§fSpieler: §b" + player.getName(), 14);
        setScore(obj, "§2", 13);
        setScore(obj, "§fRang: §9User", 12);
        setScore(obj, "§3", 11);
        setScore(obj, "§fMünzen: §e0", 10);
        setScore(obj, "§4", 9);
        setScore(obj, "§fServer: §bLobby-1", 8);
        setScore(obj, "§5", 7);
        setScore(obj, "§fnorthcraft.me", 6);

        player.setScoreboard(board);
    }

    private static void setScore(Objective obj, String text, int scoreValue) {
        obj.getScore(text).setScore(scoreValue);
    }
}
