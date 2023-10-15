/* Copyright (c) 2023, JeriCraftPerks. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.perkgui.gui;

import com.chalwk.perkgui.Main;
import com.chalwk.perkgui.data.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static com.chalwk.perkgui.Main.config;
import static com.chalwk.perkgui.Main.formatMSG;

public class CustomGUI {

    private final List<GUIButton> buttons = new ArrayList<>();
    private Inventory inventory;

    public CustomGUI(String title, int rows) {
        if (rows > 6) {
            Main.getInstance().getLogger().warning("Too many rows!");
            return;
        }
        this.inventory = Bukkit.createInventory(null, rows * 9, title);
    }

    public void setItem(GUIButton button, int slot) {
        buttons.add(button);
        inventory.setItem(slot, button.getStack());
    }

    public void handleButton(ItemStack stack) {
        buttons.stream().filter(p -> p.getStack().isSimilar(stack)).findAny().ifPresent(button -> button.getAction().run());
    }

    public void show(Player player) {
        PlayerDataManager.getData(player).setOpenGUI(this);
        player.openInventory(inventory);
    }

    public void close(Player player) {
        player.closeInventory();
    }

    public static void fillEmptySlots(CustomGUI menu, int maxSlots) {
        ItemStack glass = menu.createItem("GRAY_STAINED_GLASS_PANE", " ", new ArrayList<>());
        GUIButton glassButton = new GUIButton(glass);
        for (int i = 0; i < maxSlots; i++) {
            if (!menu.getItem(i)) {
                menu.setItem(glassButton, i);
            }
        }
        glassButton.getStack().removeEnchantment(Enchantment.DURABILITY);
        glassButton.setAction(() -> {
            // do nothing (prevents removing)
        });
    }

    public void showCloseButton(Player sender, int slot) {
        ItemStack close = createItem("BARRIER", config.getString("GUI_CLOSE_BUTTON"), new ArrayList<>());
        GUIButton closeButton = new GUIButton(close);
        this.setItem(closeButton, slot);
        closeButton.setAction(sender::closeInventory);
    }

    public ItemStack createItem(String icon, String name, List<String> lore) {

        ItemStack item = new ItemStack(Material.valueOf(icon));
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(formatMSG(name));
        lore.replaceAll(Main::formatMSG);
        meta.setLore(lore);

        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);

        return item;
    }

    public boolean getItem(int i) {
        return inventory.getItem(i) != null;
    }
}
