package org.mclogs.loghelper.listeners;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.io.FileUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mclogs.loghelper.LogHelper;
import org.mclogs.loghelper.commands.MclogCommand;
import org.mclogs.loghelper.utils.HTTPUtil;
import org.mclogs.loghelper.utils.LogFile;
import org.mclogs.loghelper.utils.LogInventory;
import org.mclogs.loghelper.utils.UnpackUtil;

import java.io.File;
import java.io.IOException;

public class InventoryListener implements Listener {
    private final LogHelper plugin;

    public InventoryListener(LogHelper main) {
        this.plugin = main;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        if (e.getView().getTitle().equalsIgnoreCase("mclo.gs") && this.plugin.openinv.get(p) != null && this.plugin.openinv.get(p).getHolder() == inv.getHolder() && item != null && item.getItemMeta() != null) {
            e.setCancelled(true);
            boolean hasDisplayName = item.getItemMeta().hasDisplayName();
            if (p.hasPermission("mclogs.upload")) {
                if (item.hasItemMeta() && hasDisplayName && item.getItemMeta().getDisplayName().equalsIgnoreCase("Next")) {
                    LogInventory.nextPage(p, MclogCommand.logs);
                } else if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase("Previous")) {
                    LogInventory.previousPage(p, MclogCommand.logs);
                } else if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getType() == Material.PAPER) {
                    analyzeLog(p, item);
                } else if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getType() == Material.CHEST) {
                    analyzePackedLog(p, item);
                }
            } else if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase("Next")) {
                LogInventory.nextPage(p, MclogCommand.logs);
            } else if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase("Previous")) {
                LogInventory.previousPage(p, MclogCommand.logs);
            } else if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getType() == Material.PAPER) {
                p.closeInventory();
                p.sendMessage(this.plugin.errorprefix + this.plugin.config.getString("messages.nopermission"));
            } else if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getType() == Material.CHEST) {
                p.closeInventory();
                p.sendMessage(this.plugin.errorprefix + this.plugin.config.getString("messages.nopermission"));
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (e.getView().getTitle().equalsIgnoreCase("mclo.gs")) {
            LogInventory.filecount = 0;
            this.plugin.openinv.remove(p);
        }
    }

    private void analyzeLog(Player p, ItemStack item) {
        if(item != null && item.getItemMeta() != null) {
            String name = item.getItemMeta().getDisplayName();
            for (LogFile f : MclogCommand.logs) {
                if (f.getName().equalsIgnoreCase(name)) {
                    File log = new File(f.getPath());
                    String urlResp = "";
                    String errorResp = "";
                    if (log.exists()) {
                        String response = HTTPUtil.analyzeFile(log);
                        if (response != null) {
                            processResponse(response, urlResp, errorResp, p, this.plugin);
                            p.closeInventory();
                        }
                        continue;
                    }
                    p.sendMessage(this.plugin.errorprefix + this.plugin.config.getString("messages.fileerror").replaceAll("%FILE", this.plugin.config.getString("mclogs.logname") + ".log"));
                }
            }
        } else {
            p.sendMessage(this.plugin.errorprefix + this.plugin.config.getString("messages.logerror") + "Inventory data error");
        }
    }

    private void analyzePackedLog(Player p, ItemStack item) {
        String name = item.getItemMeta().getDisplayName();
        for (LogFile f : MclogCommand.logs) {
            if (f.getName().equalsIgnoreCase(name)) {
                String path = f.getPath();
                UnpackUtil.gunzipIt(path, "./temp/" + name + ".txt");
                File log = new File("./temp/" + name + ".txt");
                String urlResp = "";
                String errorResp = "";
                if (log.exists()) {
                    String response = HTTPUtil.analyzeFile(log);
                    if (response != null) {
                        processResponse(response, urlResp, errorResp, p, this.plugin);
                        p.closeInventory();
                    }
                    File temp = new File("./temp");
                    try {
                        FileUtils.cleanDirectory(temp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                p.sendMessage(this.plugin.errorprefix + this.plugin.config.getString("messages.fileerror").replaceAll("%FILE", this.plugin.config.getString("mclogs.logname") + ".log"));
            }
        }
    }

    public static void processResponse(String response, String urlResp, String errorResp, CommandSender p, LogHelper plugin) {
        boolean state;
        JsonElement jelement = (new JsonParser()).parse(response);
        JsonObject jobject = jelement.getAsJsonObject();
        String success = jobject.get("success").toString();
        if (success.equalsIgnoreCase("true")) {
            urlResp = jobject.get("url").toString();
            urlResp = urlResp.substring(1, urlResp.length() - 1);
            state = true;
        } else {
            errorResp = jobject.get("error").toString();
            errorResp = errorResp.substring(1, errorResp.length() - 1);
            state = false;
        }
        if (state) {
            TextComponent message = new TextComponent(plugin.prefix + plugin.config.getString("messages.loguploaded") + urlResp);
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, urlResp));
            p.spigot().sendMessage(message);
        } else {
            p.sendMessage(plugin.errorprefix + plugin.config.getString("messages.logerror") + errorResp);
        }
    }
}
