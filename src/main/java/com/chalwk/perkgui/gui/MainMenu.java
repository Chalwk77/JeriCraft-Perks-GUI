/* Copyright (c) 2023, JeriCraft-Perks-GUI. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.perkgui.gui;

import com.chalwk.perkgui.Misc;
import com.chalwk.perkgui.data.PlayerDataManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.chalwk.perkgui.Main.getPluginConfig;
import static com.chalwk.perkgui.gui.CustomGUI.RenderCategories;
import static com.chalwk.perkgui.gui.CustomGUI.renderPerkButton;

public class MainMenu {

    private static final FileConfiguration config = getPluginConfig();

    public static void showMenu(Player player, String title, int rows, boolean mainMenu) {

        PlayerDataManager.getData(player).setMoneySpent(0);

        int slot = 0;
        int slots = (rows * 9) - 1;

        CustomGUI menu = new CustomGUI(Misc.formatMSG(title), rows);
        menu.showCloseButton(player, slots, false); // slots = (rows * 9) - 1
        menu.fillEmptySlots(slots); // slots = (rows * 9) - 1
        if (!mainMenu) {
            showBackButton(player, menu, 18, true);
        }

        List<Map<?, ?>> categories = config.getMapList("CATEGORIES");
        for (Map<?, ?> opt : categories) {
            if (slot > 8) {
                break;
            }
            CustomGUI.Categories result = RenderCategories(opt, menu, slot);
            result.button.setAction(() -> {
                menu.close(player);
                Misc.sound(player, "entity.villager.yes");
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
        CustomGUI menu = new CustomGUI(Misc.formatMSG(title), 6);
        Map<?, ?> perks = (Map<?, ?>) data.get("perks");

        for (Map.Entry<?, ?> entry : perks.entrySet()) {

            Map<?, ?> perk = (Map<?, ?>) entry.getValue();
            String permission = (String) entry.getKey();
            String name = (String) perk.get("name");
            String icon = (String) perk.get("icon");
            List<String> lore = (List<String>) perk.get("lore");

            if (mainMenu || sender.hasPermission(permission)) {
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
            if (mainMenu) {
                showMenu(player, config.getString("MAIN-MENU-TITLE"), 3, true);
            } else {
                showMenu(player, config.getString("PROFILE-MENU-TITLE"), 3, false);
            }
        });
    }
}
