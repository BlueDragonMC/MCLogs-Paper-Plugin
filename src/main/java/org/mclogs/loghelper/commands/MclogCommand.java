package org.mclogs.loghelper.commands;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.mclogs.loghelper.LogHelper;
import org.mclogs.loghelper.utils.HTTPUtil;
import org.mclogs.loghelper.utils.LogFile;
import org.mclogs.loghelper.utils.LogInventory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MclogCommand implements CommandExecutor {
    private final LogHelper plugin;

    public static List<LogFile> logs = new ArrayList<>();

    public MclogCommand(LogHelper main) {
        this.plugin = main;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            readFiles();
            if (args.length == 0) {
                if (p.hasPermission("mclogs.upload")) {
                    analyzeLatestLog(p);
                } else {
                    p.sendMessage(this.plugin.errorprefix + this.plugin.config.getString("messages.nopermission"));
                }
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    if (p.hasPermission("mclogs.list") || p.isOp()) {
                        showLogList(p);
                    } else {
                        p.sendMessage(this.plugin.errorprefix + this.plugin.config.getString("messages.nopermission"));
                    }
                } else {
                    p.sendMessage(this.plugin.errorprefix + this.plugin.config.getString("messages.wrongcommand"));
                }
            } else {
                p.sendMessage(this.plugin.errorprefix + this.plugin.config.getString("messages.wrongcommand"));
            }
        } else if (sender instanceof ConsoleCommandSender &&
                args.length == 0) {
            analyzeConsoleLatestLog((ConsoleCommandSender) sender);
        }
        return true;
    }

    private void analyzeLatestLog(Player p) {
        File latest = new File("./" + this.plugin.config.getString("mclogs.logdirectory") + "/" + this.plugin.config.getString("mclogs.logname") + ".log");

        String urlResp = "";
        String errorResp = "";
        if (latest.exists()) {
            String response = HTTPUtil.analyzeFile(latest);
            if (response != null) {
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
                    TextComponent message = new TextComponent(this.plugin.prefix + this.plugin.config.getString("messages.loguploaded") + urlResp);
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, urlResp));
                    p.spigot().sendMessage(message);
                } else {
                    p.sendMessage(this.plugin.errorprefix + this.plugin.config.getString("messages.logerror") + errorResp);
                }
            }
        } else {
            p.sendMessage(this.plugin.errorprefix + this.plugin.config.getString("messages.fileerror").replaceAll("%FILE", this.plugin.config.getString("mclogs.logname") + ".log"));
        }
    }

    private void analyzeConsoleLatestLog(ConsoleCommandSender sender) {
        File latest = new File("./" + this.plugin.config.getString("mclogs.logdirectory") + "/" + this.plugin.config.getString("mclogs.logname") + ".log");
        String urlResp = "";
        String errorResp = "";
        if (latest.exists()) {
            String response = HTTPUtil.analyzeFile(latest);
            if (response != null) {
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
                    TextComponent message = new TextComponent(this.plugin.prefix + this.plugin.config.getString("messages.loguploaded") + urlResp);
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, urlResp));
                    sender.spigot().sendMessage(message);
                } else {
                    sender.sendMessage(this.plugin.errorprefix + this.plugin.config.getString("messages.logerror") + errorResp);
                }
            }
        } else {
            sender.sendMessage(this.plugin.errorprefix + this.plugin.config.getString("messages.fileerror").replaceAll("%FILE", this.plugin.config.getString("mclogs.logname") + ".log"));
        }
    }

    private void showLogList(Player p) {
        Inventory inv = Bukkit.createInventory(null, 54, "mclo.gs");
        this.plugin.openinv.put(p, inv);
        LogInventory.setup(p);
        boolean state = LogInventory.insertItems(p, logs);
        if (state)
            p.openInventory(this.plugin.openinv.get(p));
    }

    public void readFiles() {
        logs.clear();
        File folder = new File("./" + this.plugin.config.getString("mclogs.logdirectory"));
        File[] files = folder.listFiles();
        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        byte b;
        int i;
        File[] arrayOfFile1;
        for (i = (arrayOfFile1 = files).length, b = 0; b < i; ) {
            File f = arrayOfFile1[b];
            String fname = f.getName();
            String fext = fname.substring(fname.lastIndexOf(".") + 1);
            String fdate = StringUtils.substringBefore(fname, ".");
            String fpath = f.getPath();
            LogFile lf = new LogFile(fdate, fname, fext, fpath, f);
            logs.add(lf);
            b++;
        }
    }
}
