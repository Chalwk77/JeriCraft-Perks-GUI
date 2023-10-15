/* Copyright (c) 2023, JeriCraft-Perks-GUI. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.perkgui.gui;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.chalwk.perkgui.Main.*;
import static com.chalwk.perkgui.gui.CustomGUI.fillEmptySlots;
import static com.chalwk.perkgui.gui.MainMenu.showMainMenu;

public class PlayerProfile {

    public static void showPlayerProfile(Player player) {

        CustomGUI menu = new CustomGUI(formatMSG(config.getString("PROFILE-MENU-TITLE")), 3);
        menu.showCloseButton(player, 26);

        int slot = 0;
        for (Map<?, ?> opt : config.getMapList("CATEGORIES")) {

            if (slot > 8) {
                break;
            }

            List<String> type = new ArrayList<>((Collection<? extends String>) opt.keySet());
            String category = type.get(0);
            Map<?, ?> data = (Map<?, ?>) opt.get(category);

            String title = (String) data.get("title");              // category title
            String icon = (String) data.get("icon");                // category icon
            List<String> lore = (List<String>) data.get("lore");    // category lore

            ItemStack item = menu.createItem(icon, title, lore);
            ItemMeta meta = item.getItemMeta();

            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);

            GUIButton button = new GUIButton(item);
            menu.setItem(button, slot);
            button.setAction(() -> {
                menu.close(player);
                showPersonalPerks(player, data, title);
                player.playSound(player.getLocation(), "entity.villager.yes", 1, 1);
            });

            ItemStack backButton = menu.createItem("RED_STAINED_GLASS_PANE", config.getString("GUI_PREVIOUS_PAGE"), new ArrayList<>());
            GUIButton back = new GUIButton(backButton);
            menu.setItem(back, 18);
            back.setAction(() -> {
                menu.close(player);
                showMainMenu(player);
            });

            slot = slot + 2;
        }

        fillEmptySlots(menu, 27);
        menu.show(player);
    }

    private static void showPersonalPerks(Player sender, Map<?, ?> data, String title) {
        CustomGUI menu = new CustomGUI(formatMSG("&b&lYour " + title), 6);
        int slot = -1;
        Map<?, ?> perks = (Map<?, ?>) data.get("perks");
        for (Map.Entry<?, ?> entry : perks.entrySet()) {
            String node = (String) entry.getKey();
            if (sender.hasPermission(node)) {
                slot++;
                Map<?, ?> perkData = (Map<?, ?>) entry.getValue();
                String name = (String) perkData.get("name");
                String icon = (String) perkData.get("icon");
                List<String> lore = (List<String>) perkData.get("lore");
                showPerkButtons(sender, name, icon, lore, menu, slot);
            }
        }
        if (slot >= 0) {
            fillEmptySlots(menu, 53);
            menu.show(sender);
        } else {
            send(sender, config.getString("NO_PERKS").replace("{category}", title));
            sender.playSound(sender.getLocation(), "entity.villager.no", 1, 1);
            menu.close(sender);
            showPlayerProfile(sender); // go back to main menu
        }
    }

    private static void showPerkButtons(Player sender, String name, String icon, List<String> lore, CustomGUI gui, int slot) {

        ItemStack item = gui.createItem(icon, name, lore);

        GUIButton button = new GUIButton(item);
        button.setAction(() -> {
        });
        gui.setItem(button, slot);

        ItemStack backButton = gui.createItem("RED_STAINED_GLASS_PANE", config.getString("GUI_PREVIOUS_PAGE"), new ArrayList<>());
        GUIButton back = new GUIButton(backButton);
        gui.setItem(back, 45);
        back.setAction(() -> {
            gui.close(sender);
            showPlayerProfile(sender);
        });

        gui.showCloseButton(sender, 53);
    }
}
