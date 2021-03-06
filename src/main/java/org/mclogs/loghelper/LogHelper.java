package org.mclogs.loghelper;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.mclogs.loghelper.commands.MclogCommand;
import org.mclogs.loghelper.listeners.InventoryListener;

import java.io.File;
import java.util.HashMap;

public class LogHelper extends JavaPlugin {

    public FileConfiguration config;
    final File temp = new File("./temp");

    public final String prefix = ChatColor.BLUE + "[mclo.gs] " + ChatColor.GREEN;
    public final String errorprefix = ChatColor.BLUE + "[mclo.gs] " + ChatColor.RED;

    public final HashMap<Player, Inventory> openinv = new HashMap<>();

    @Override
    public void onEnable() {
        if (!this.temp.exists())
            //noinspection ResultOfMethodCallIgnored
            this.temp.mkdirs();

        saveDefaultConfig();
        getCommand("mclogs").setExecutor(new MclogCommand(this));
        Bukkit.getPluginManager().registerEvents(new InventoryListener(this), this);
        this.config = getConfig();
    }
}
