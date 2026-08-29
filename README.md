# ⚓ Northcraft LobbySystem

Das offizielle Spigot/Paper-Lobbyplugin für das **Northcraft.me** Minecraft-Netzwerk[cite: 1]. Es vereint maritimes Nordsee-Flair mit echten Kieler Live-Wetterdaten, maßgeschneiderter Tablist, Scoreboard, Server-Navigator und einem umfassenden Lobbyschutz[cite: 1, 2].

---

## 🌊 Features

* **🌦️ Live RealTime & RealWeather (Kiel)**
  * **Echtzeit-Uhrzeit:** Synchronisiert die Minecraft-Weltzeit im Sekundentakt mit der Zeitzone `Europe/Berlin`.
  * **Kieler Live-Wetter:** Fragt alle 10 Minuten automatisiert die Open-Meteo API für Kiel (`54.3213`, `10.1348`) ab und spiegelt Regen, Stürme und Gewitter direkt in der Lobby-Welt wider.
* **📊 Custom Tablist & Scoreboard**
  * **Tablist:** Dynamische Kopf- und Fußzeilen via Paper Adventure API (`LegacyComponentSerializer`).
  * **Sidebar-Scoreboard:** Anzeige von Spielername, Rang, Coins, aktuellem Server (`Lobby-1`) und Server-Domain[cite: 2].
* **🧩 Navigator & BungeeCord-Routing**
  * **Hotbar-Kompass:** Öffnet per Rechtsklick ein interaktives Navigation-GUI (`NavigatorGUI`)[cite: 2].
  * **BungeeCord Messaging:** Nahtlose Weiterleitung auf Unterserver wie `fishing_slap`, `tntrun`, `tntcanon`, `jumpwater` und `fishslapffa`[cite: 2].
* **🛡️ Lobby-Schutz-System & Join-Handling**
  * **Status-Reset:** Setzt Abenteuer-Modus, Leben, Hunger und Inventar beim Betreten zurück[cite: 2].
  * **Effekte:** Begrüßungstitel, maritime Chat-Nachricht, XP-Sound und automatisches Join-Feuerwerk[cite: 2].
  * **Schutz:** Deaktiviert Block-Abbau/Aufbau, Hungerverlust, Schaden und Item-Drop (Bypass via Permission `lobby.build`)[cite: 2].
  * **Muted Join/Quit:** Unterdrückt die Standard-Minecraft-Nachrichten beim Betreten und Verlassen des Servers.

---

## ⚙️ Anforderungen & Kompatibilität

* **Server-Software:** Paper / Purpur 1.20+[cite: 2]
* **Java-Version:** Java 17 (LTS) oder höher[cite: 8]
* **Netzwerk:** BungeeCord oder Velocity Proxy mit aktiviertem Plugin-Messaging[cite: 2]

---

## 🛠️ Befehle & Rechte

| Befehl / Permission | Beschreibung | Standard-Zugriff |
|---|---|---|
| `/setspawn` | Speichert die aktuelle Position & Blickrichtung als Lobby-Spawn in der `config.yml`[cite: 2]. | `lobby.admin`[cite: 2] |
| `lobby.build` | Bypass für den Lobbyschutz (Bauen, Blöcke abbauen, Items droppen)[cite: 2]. | Operatoren |

---

## 📁 Projektstruktur

```text
src/main/java/me/northcraft/lobbySystem/
├── LobbySystem.java         # Hauptklasse (Plugin-Lifecycle, Spawn-Speicherung)[cite: 2]
├── LobbyListener.java       # Event-Handling (Join/Quit, Schutz, Interaktionen)[cite: 2]
├── TablistManager.java      # Paper-Adventure Tablist Header/Footer[cite: 2]
├── ScoreboardManager.java   # Scoreboard Sidebar-Anzeige[cite: 2]
├── RealSyncManager.java     # Live-Uhrzeit & Open-Meteo Wetterabruf für Kiel
└── NavigatorGUI.java        # Inventar-GUI für die Serverauswahl[cite: 2]
