package ru.paingocry.holders.painholders;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlaceholderText implements Listener {
    private static ArrayList<PlaceholderText> placeholders = new ArrayList();
    private String placeholderName;
    private String zeroDisplay;
    private String normalDisplay;
    private HashMap<Player, String> playersToCheck = new HashMap();
    private FileConfiguration data;
    private File file;

    public String getToDisplay(Player p) {
        return this.playersToCheck.containsKey(p) ? this.normalDisplay.replace("%value%", (CharSequence)this.playersToCheck.get(p)) : this.zeroDisplay;
    }

    public static PlaceholderText getPlaceholderByName(String name) {
        Iterator var1 = placeholders.iterator();

        PlaceholderText placeholder;
        do {
            if (!var1.hasNext()) {
                return null;
            }

            placeholder = (PlaceholderText)var1.next();
        } while(!placeholder.placeholderName.equals(name));

        return placeholder;
    }

    public void setValue(String value, String player) {
        Player p;
        if (!value.equalsIgnoreCase("null")) {
            value = value.replace("_", " ");
            this.data.set(player, value);
            p = Bukkit.getPlayerExact(player);
            if (p != null) {
                this.playersToCheck.put(p, value);
            }
        } else {
            this.data.set(player, (Object)null);
            p = Bukkit.getPlayerExact(player);
            if (p != null) {
                this.playersToCheck.remove(p);
            }
        }

        try {
            this.data.save(this.file);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public PlaceholderText(ConfigurationSection section) {
        this.placeholderName = section.getString("name");
        this.zeroDisplay = section.getString("zero-display");
        this.normalDisplay = section.getString("normal-display");
        this.file = new File(Main.plugin.getDataFolder().getAbsolutePath(), "placeholderData/" + this.placeholderName + ".yml");

        try {
            if (!this.file.exists()) {
                this.file.createNewFile();
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        this.data = YamlConfiguration.loadConfiguration(this.file);
        Bukkit.getPluginManager().registerEvents(this, Main.plugin);
        placeholders.add(this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        String value = this.data.getString(e.getPlayer().getName());
        if (value != null) {
            this.playersToCheck.put(e.getPlayer(), value);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        this.playersToCheck.remove(e.getPlayer());
    }
}
