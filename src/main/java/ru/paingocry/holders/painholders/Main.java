package ru.paingocry.holders.painholders;


import java.io.File;
import java.util.Iterator;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main plugin;

    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();
        File dataFolder = new File(this.getDataFolder(), "placeholderData");
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        Iterator var2 = this.getConfig().getConfigurationSection("placeholders").getKeys(false).iterator();

        String key;
        while(var2.hasNext()) {
            key = (String)var2.next();
            new Placeholder(this.getConfig().getConfigurationSection("placeholders." + key));
        }

        var2 = this.getConfig().getConfigurationSection("placeholders-text").getKeys(false).iterator();

        while(var2.hasNext()) {
            key = (String)var2.next();
            new PlaceholderText(this.getConfig().getConfigurationSection("placeholders-text." + key));
        }

        new Commands();
        (new PlaceholderRegister(this)).register();
    }
}