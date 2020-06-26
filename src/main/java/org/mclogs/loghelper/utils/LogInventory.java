package org.mclogs.loghelper.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.mclogs.loghelper.LogHelper;

import java.util.List;

public class LogInventory {
    private static LogHelper plugin;

    private static final int perPage = 45;

    public static int filecount = 0;

    public LogInventory(LogHelper main) {
        plugin = main;
    }

    public static void setup(Player p) {
        Inventory inv = plugin.openinv.get(p);
        if (inv != null) {
            inv.setItem(45, Items.getItem(Material.WHITE_STAINED_GLASS_PANE, "", " "));
            inv.setItem(46, Items.getItem(Material.WHITE_STAINED_GLASS_PANE, " ", " "));
            inv.setItem(47, Items.getItem(Material.WHITE_STAINED_GLASS_PANE, " ", " "));
            inv.setItem(48, Items.getItem(Material.WHITE_STAINED_GLASS_PANE, " ", " "));
            inv.setItem(49, Items.getItem(Material.WHITE_STAINED_GLASS_PANE, " ", " "));
            inv.setItem(50, Items.getItem(Material.WHITE_STAINED_GLASS_PANE, " ", " "));
            inv.setItem(51, Items.getItem(Material.WHITE_STAINED_GLASS_PANE, " ", " "));
            inv.setItem(52, Items.getItem(Material.WHITE_STAINED_GLASS_PANE, " ", " "));
            inv.setItem(53, Items.getArrowRight());
        }
    }

    public static void nextPage(Player p, List<LogFile> files) {
        if (filecount < files.size()) {
            Inventory inv = plugin.openinv.get(p);
            inv.clear();
            setup(p);
            inv.setItem(45, Items.getArrowLeft());
            insertItems(p, files);
        } else {
            plugin.openinv.get(p).setItem(53, Items.getItem(Material.WHITE_STAINED_GLASS_PANE, " ", " "));
        }
    }

    public static void previousPage(Player p, List<LogFile> files) {
        if (files.size() == filecount) {
            int rest = filecount % perPage;
            filecount -= rest;
            filecount -= perPage;
            Inventory inv = plugin.openinv.get(p);
            inv.clear();
            setup(p);
            inv.setItem(45, Items.getArrowLeft());
        } else {
            filecount -= perPage;
            filecount -= perPage;
            if (filecount < 0) {
                filecount = 0;
                return;
            }
            Inventory inv = plugin.openinv.get(p);
            inv.clear();
            setup(p);
            inv.setItem(45, Items.getArrowLeft());
        }
        insertItems(p, files);
    }

    public static boolean insertItems(Player p, List<LogFile> files) {
        Inventory inv = plugin.openinv.get(p);
        if (inv != null) {
            for (int i = 0; i < perPage; i++) {
                if (filecount > files.size() - 1)
                    return true;
                LogFile f = files.get(filecount);
                if (f != null)
                    if (f.getExt().equalsIgnoreCase("gz")) {
                        inv.setItem(i, Items.getItem(Material.CHEST, f.getName(), ""));
                        filecount++;
                    } else {
                        inv.setItem(i, Items.getItem(Material.PAPER, f.getName(), ""));
                        filecount++;
                    }
            }
            return true;
        }
        return false;
    }
}
