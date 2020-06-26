package org.mclogs.loghelper.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.UUID;

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
        String headName = "Next";
        ItemStack stack = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) 3);
        stack = Bukkit.getUnsafe().modifyItemStack(stack, "{display:{Name:\"" + headName + "\"},SkullOwner:{Id:" + UUID.randomUUID().toString() + ",Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzE1NDQ1ZGExNmZhYjY3ZmNkODI3ZjcxYmFlOWMxZDJmOTBjNzNlYjJjMWJkMWVmOGQ4Mzk2Y2Q4ZTgifX19\"}]}}}");
        return stack;
    }

    public static ItemStack getArrowLeft() {
        String headName = "Previous";
        ItemStack stack = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) 3);
        stack = Bukkit.getUnsafe().modifyItemStack(stack, "{display:{Name:\"" + headName + "\"},SkullOwner:{Id:" + UUID.randomUUID().toString() + ",Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWVkNzg4MjI1NzYzMTdiMDQ4ZWVhOTIyMjdjZDg1ZjdhZmNjNDQxNDhkY2I4MzI3MzNiYWNjYjhlYjU2ZmExIn19fQ==\"}]}}}");
        return stack;
    }
}
