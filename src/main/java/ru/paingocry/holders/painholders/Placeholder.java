package ru.paingocry.holders.painholders;


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

public class Placeholder implements Listener {
    private static ArrayList<Placeholder> placeholders = new ArrayList();
    private String placeholderName;
    private String zeroDisplay;
    private String normalDisplay;
    private HashMap<Player, Integer> playersToCheck = new HashMap();
    private FileConfiguration data;
    private File file;

    public String getToDisplay(Player p) {
        return this.playersToCheck.containsKey(p) ? this.normalDisplay.replace("%value%", ((Integer)this.playersToCheck.get(p)).toString()) : this.zeroDisplay;
    }

    public static Placeholder getPlaceholderByName(String name) {
        Iterator var1 = placeholders.iterator();

        Placeholder placeholder;
        do {
            if (!var1.hasNext()) {
                return null;
            }

            placeholder = (Placeholder)var1.next();
        } while(!placeholder.placeholderName.equals(name));

        return placeholder;
    }

    public void setValue(Integer value, String player) {
        if (value != 0) {
            int fromFile = value;
            this.data.set(player, fromFile);
            Player p = Bukkit.getPlayerExact(player);
            if (p != null) {
                this.playersToCheck.put(p, fromFile);
            }
        } else {
            this.data.set(player, (Object)null);
            Player p = Bukkit.getPlayerExact(player);
            if (p != null) {
                this.playersToCheck.remove(p);
            }
        }

        try {
            this.data.save(this.file);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    public void addValue(Integer value, String player) {
        int fromFile = this.data.getInt(player);
        fromFile += value;
        this.data.set(player, fromFile);

        try {
            this.data.save(this.file);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        Player p = Bukkit.getPlayerExact(player);
        if (p != null) {
            this.playersToCheck.put(p, fromFile);
        }

    }

    public void removeValue(Integer value, String player) {
        int fromFile = this.data.getInt(player);
        fromFile -= value;
        Player p;
        if (fromFile <= 0) {
            this.data.set(player, (Object)null);
            p = Bukkit.getPlayerExact(player);
            if (p != null) {
                this.playersToCheck.remove(p);
            }
        } else {
            this.data.set(player, fromFile);
            p = Bukkit.getPlayerExact(player);
            if (p != null) {
                this.playersToCheck.put(p, fromFile);
            }
        }

        try {
            this.data.save(this.file);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    public Placeholder(ConfigurationSection section) {
        this.placeholderName = section.getString("name");
        this.zeroDisplay = section.getString("zero-display");
        this.normalDisplay = section.getString("normal-display");
        this.file = new File(Main.plugin.getDataFolder(), "placeholderData/" + this.placeholderName + ".yml");

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
    public void onJoin(PlayerJoinEvent e) {
        int value = this.data.getInt(e.getPlayer().getName());
        if (value != 0) {
            this.playersToCheck.put(e.getPlayer(), value);
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        this.playersToCheck.remove(e.getPlayer());
    }
}
