/* Copyright (c) 2023, JeriCraft-Perks-GUI. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.perkgui.gui;

import com.chalwk.perkgui.data.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.chalwk.perkgui.Main.*;
import static com.chalwk.perkgui.gui.CustomGUI.RenderCategories;
import static com.chalwk.perkgui.gui.CustomGUI.renderPerkButton;

public class MainMenu {

    public static void showMenu(Player player, String title, int rows, boolean mainMenu) {

        int slot = 0;
        int slots = (rows * 9) - 1;

        CustomGUI menu = new CustomGUI(formatMSG(title), rows);
        menu.showCloseButton(player, slots, false); // slots = (rows * 9) - 1
        menu.fillEmptySlots(slots); // slots = (rows * 9) - 1
        if (!mainMenu) {
            showBackButton(player, menu, 18, false);
        }

        List<Map<?, ?>> categories = config.getMapList("CATEGORIES");
        for (Map<?, ?> opt : categories) {
            if (slot > 8) {
                break;
            }
            CustomGUI.Categories result = RenderCategories(opt, menu, slot);
            result.button.setAction(() -> {
                menu.close(player);
                sound(player, "entity.villager.yes");
                showPerks(player, result.data, result.title, mainMenu);
            });
            slot = slot + 2;
            PlayerDataManager.getData(player).updateMoneySpent(player, result.data);
        }
        menu.showProfileButton(player, 13, mainMenu);
        menu.show(player);
    }

    private static void showPerks(Player sender, Map<?, ?> data, String title, boolean mainMenu) {

        int slot = -1;
        CustomGUI menu = new CustomGUI(formatMSG(title), 6);
        Map<?, ?> perks = (Map<?, ?>) data.get("perks");

        for (Map.Entry<?, ?> entry : perks.entrySet()) {

            Map<?, ?> perk = (Map<?, ?>) entry.getValue();
            String permission = (String) entry.getKey();
            String name = (String) perk.get("name");
            String icon = (String) perk.get("icon");
            List<String> lore = (List<String>) perk.get("lore");

            if (mainMenu) {
                slot++;
                renderPerkButton(name, icon, lore, menu, slot);
            } else if (sender.hasPermission(permission)) {
                slot++;
                renderPerkButton(name, icon, lore, menu, slot);
            }
        }

        showBackButton(sender, menu, 45, mainMenu);
        menu.showCloseButton(sender, 53, false);
        menu.fillEmptySlots(53);
        menu.show(sender);
    }

    private static void showBackButton(Player player, CustomGUI menu, int slot, boolean mainMenu) {

        String backIcon = config.getString("GUI_BACK_BUTTON");
        String backIconText = config.getString("GUI_BACK_BUTTON_TEXT");
        ItemStack item = menu.createItem(backIcon, backIconText, new ArrayList<>(), false);

        GUIButton button = new GUIButton(item);
        menu.setItem(button, slot);
        button.setAction(() -> {
            menu.close(player);
            showMenu(player, config.getString("MAIN-MENU-TITLE"), 3, mainMenu);
        });
    }
}
