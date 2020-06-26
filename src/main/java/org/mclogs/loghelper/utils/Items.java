package org.mclogs.loghelper.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Items {
    public static ItemStack getItem(Material mat, String DisplayName, String loreString) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(DisplayName);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(loreString);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getArrowRight() {
        ItemStack stack = new ItemStack(Material.ARROW);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName("Next");
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack getArrowLeft() {
        ItemStack stack = new ItemStack(Material.ARROW);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName("Previous");
        stack.setItemMeta(meta);
        return stack;
    }
}
