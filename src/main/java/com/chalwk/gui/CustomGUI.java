/* Copyright (c) 2023, JeriCraftPerks. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.gui;

import com.chalwk.JeriCraftPerkGUI;
import com.chalwk.Misc;
import com.chalwk.data.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.chalwk.gui.Menu.showMenu;

public class CustomGUI {

    private static final FileConfiguration config = JeriCraftPerkGUI.getPluginConfig();
    private final List<GUIButton> buttons = new ArrayList<>();
    private Inventory inventory;

    public CustomGUI(String title, int rows) {
        if (rows > 6) {
            JeriCraftPerkGUI.getInstance().getLogger().warning("Too many rows!");
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

    public void fillEmptySlots(int maxSlots) {
        String emptySlots = config.getString("GUI_EMPTY_SLOTS");
        ItemStack item = this.createItem(emptySlots, "", new ArrayList<>(), false);
        GUIButton button = new GUIButton(item);
        for (int i = 0; i < maxSlots; i++) {
            if (!this.getItem(i)) {
                this.setItem(button, i);
            }
        }
        button.setAction(() -> {
        });
    }

    public void showCloseButton(Player player, int slot, boolean Enchant) {
        String closeIcon = config.getString("GUI_CLOSE_BUTTON");
        String closeIconText = config.getString("GUI_CLOSE_BUTTON_TEXT");
        ItemStack item = this.createItem(closeIcon, closeIconText, new ArrayList<>(), Enchant);
        GUIButton button = new GUIButton(item);
        this.setItem(button, slot);
        button.setAction(player::closeInventory);
    }

    public void showProfileButton(Player player, int slot, boolean mainMenu) {

        if (!mainMenu) {
            return;
        }

        int moneySpent = PlayerDataManager.getData(player).getMoneySpent();
        List<String> profileLore = new ArrayList<>();
        profileLore.add(Misc.formatMSG("&aClick to view your profile."));
        profileLore.add(Misc.formatMSG("&aYou have spent a total of &b$" + moneySpent + "&a."));

        PlayerDataManager.getData(player).setHead(profileLore, config.getString("GUI_PROFILE_BUTTON"));
        ItemStack head = PlayerDataManager.getData(player).getHead();
        GUIButton button = new GUIButton(head);
        this.setItem(button, slot);

        button.setAction(() -> {
            this.close(player);
            Misc.sound(player, "block.note_block.pling");
            showMenu(player, config.getString("PROFILE-MENU-TITLE"), 3, false);
        });
    }

    public ItemStack createItem(String icon, String name, List<String> lore, boolean Enchant) {

        ItemStack item = new ItemStack(Material.valueOf(icon));
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Misc.formatMSG(name));
        lore.replaceAll(Misc::formatMSG);
        meta.setLore(lore);

        if (Enchant) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(meta);

        return item;
    }

    public static void renderPerkButton(String name, String icon, List<String> lore, CustomGUI menu, int slot) {
        ItemStack item = menu.createItem(icon, name, lore, true);
        GUIButton button = new GUIButton(item);
        button.setAction(() -> {
        });
        menu.setItem(button, slot);
    }

    @NotNull
    public static Categories RenderCategories(Map<?, ?> opt, CustomGUI menu, int slot) {
        List<String> type = new ArrayList<>((Collection<? extends String>) opt.keySet());
        String category = type.get(0);
        Map<?, ?> data = (Map<?, ?>) opt.get(category);

        String name = (String) data.get("title");               // category title
        String icon = (String) data.get("icon");                // category icon
        List<String> lore = (List<String>) data.get("lore");    // category lore

        ItemStack item = menu.createItem(icon, name, lore, true);
        GUIButton button = new GUIButton(item);
        menu.setItem(button, slot);
        return new Categories(data, button);
    }

    public static class Categories {
        public final Map<?, ?> data;
        public final GUIButton button;
        public String title;

        public Categories(Map<?, ?> data, GUIButton button) {
            this.data = data;
            this.button = button;
            this.title = (String) data.get("title");
        }
    }

    public boolean getItem(int i) {
        return inventory.getItem(i) != null;
    }
}
