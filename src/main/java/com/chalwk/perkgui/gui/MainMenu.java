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
import static com.chalwk.perkgui.gui.PlayerProfile.showPlayerProfile;

public class MainMenu {

    public static void showMainMenu(Player player) {

        playerSpend.put(player.getUniqueId(), 0);

        CustomGUI menu = new CustomGUI(formatMSG(config.getString("MAIN-MENU-TITLE")), 3);
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
                showAllPerks(player, data, title);
                player.playSound(player.getLocation(), "entity.villager.yes", 1, 1);
            });

            slot = slot + 2;
            updateTotalSpent(player, data);
        }

        fillEmptySlots(menu, 27);
        showProfileButton(player, menu);
        menu.show(player);
    }

    private static void updateTotalSpent(Player sender, Map<?, ?> data) {
        Map<?, ?> perks = (Map<?, ?>) data.get("perks");
        for (Map.Entry<?, ?> entry : perks.entrySet()) {
            String node = (String) entry.getKey();
            if (sender.hasPermission(node)) {
                Map<?, ?> perkData = (Map<?, ?>) entry.getValue();
                int price = (int) perkData.get("price");
                int totalSpent = playerSpend.get(sender.getUniqueId());
                playerSpend.put(sender.getUniqueId(), totalSpent + price);
            }
        }
    }

    private static void showProfileButton(Player sender, CustomGUI menu) {
        List<String> profileLore = new ArrayList<>();
        profileLore.add(formatMSG("&aClick to view your profile."));
        profileLore.add(formatMSG("&aYou have spent a total of &b$" + playerSpend.get(sender.getUniqueId()) + "&a."));
        ItemStack profileButton = menu.createItem("PLAYER_HEAD", config.getString("GUI_PROFILE_BUTTON"), profileLore);
        GUIButton profile = new GUIButton(profileButton);
        menu.setItem(profile, 13);
        profile.setAction(() -> {
            menu.close(sender);
            showPlayerProfile(sender);
            sender.playSound(sender.getLocation(), "block.note_block.pling", 1, 1);
        });
    }

    private static void showAllPerks(Player sender, Map<?, ?> data, String title) {
        CustomGUI menu = new CustomGUI(formatMSG(title), 6);
        int slot = -1;
        Map<?, ?> perks = (Map<?, ?>) data.get("perks");
        for (Map.Entry<?, ?> entry : perks.entrySet()) {
            slot++;
            Map<?, ?> perkData = (Map<?, ?>) entry.getValue();
            String name = (String) perkData.get("name");

            String node = (String) entry.getKey();
            if (sender.hasPermission(node)) {
                name += " &a&l[&c&lPurchased&a&l]";
            }

            String icon = (String) perkData.get("icon");
            List<String> lore = (List<String>) perkData.get("lore");
            showPerkButtons(sender, name, icon, lore, menu, slot);
        }
        if (slot >= 0) {
            menu.show(sender);
            fillEmptySlots(menu, 53);
        } else {
            send(sender, config.getString("NO_PERKS").replace("{category}", title));
            sender.playSound(sender.getLocation(), "entity.villager.no", 1, 1);
            showMainMenu(sender); // go back to main menu
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
            showMainMenu(sender);
        });

        gui.showCloseButton(sender, 53);
    }
}
