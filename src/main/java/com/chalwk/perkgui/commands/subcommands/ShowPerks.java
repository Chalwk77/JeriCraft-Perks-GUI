/* Copyright (c) 2023, JeriCraftPerks. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.perkgui.commands.subcommands;

import com.chalwk.perkgui.Main;
import com.chalwk.perkgui.commands.SubCommand;
import com.chalwk.perkgui.gui.CustomGUI;
import com.chalwk.perkgui.gui.GUIButton;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ShowPerks extends SubCommand {

    private final FileConfiguration config = Main.getPluginConfig();

    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getDescription() {
        return "Shows you the perks you have purchased.";
    }

    @Override
    public String getSyntax() {
        return "/perks " + getName();
    }

    @Override
    public String getPermission() {
        return "perks.show";
    }

    @Override
    public void perform(Player sender, String[] args) {

        CustomGUI mainMenu = new CustomGUI(formatMSG(config.getString("MAIN-MENU-TITLE")), 3);
        showCategories(sender, mainMenu, 3);

//        CustomGUI GUI = new CustomGUI(formatMSG(config.getString("GUI_TITLE")), 6);
//        showNavigationButtons(sender, GUI);
//
//        int slot = 0;
//        for (Map<?, ?> opt : config.getMapList("PERMISSIONS")) {
//            if (slot > 52) { // coming in a future update: multiple pages
//                break;
//            }
//            List<String> perk = new ArrayList<>((Collection<? extends String>) opt.keySet());
//            String permissionNode = perk.get(0);
//            if (sender.hasPermission(permissionNode)) {
//                Map<?, ?> data = (Map<?, ?>) opt.get(permissionNode);
//                String name = (String) data.get("name");
//                List<String> lore = (List<String>) data.get("lore");
//                GUIButton perkButton = getGuiButton(sender, name, lore);
//                GUI.setItem(perkButton, slot);
//                slot++;
//            }
//        }
//        if (slot > 0) {
//            GUI.show(sender);
//        } else {
//            sender.sendMessage(formatMSG(config.getString("NO_PERKS")));
//        }
    }

    private void showCategories(Player sender, CustomGUI gui, int rows) {

        showCloseButton(sender, gui, 22);

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

            ItemStack item = new ItemStack(Material.valueOf(icon));
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(formatMSG(title));
            lore.replaceAll(this::formatMSG);
            meta.setLore(lore);

            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);

            GUIButton button = new GUIButton(item);
            gui.setItem(button, slot);
            button.setAction(() -> {
                gui.close(sender);
                showPerks(sender, data, title, category);
            });

            slot = slot + 2;
        }
        gui.show(sender);
    }

    private void showPerks(Player sender, Map<?, ?> data, String title, String category) {
        //CustomGUI menu = new CustomGUI(formatMSG(title), 53);
        System.out.println("SHOWING PERKS FOR " + category);
        List<String> perks = (List<String>) data.get("perks");


    }

    private void showCloseButton(Player sender, CustomGUI gui, int slot) {
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(formatMSG(config.getString("GUI_CLOSE_BUTTON")));
        close.setItemMeta(closeMeta);
        GUIButton closeButton = new GUIButton(close);
        gui.setItem(closeButton, slot);
        closeButton.setAction(sender::closeInventory);
    }

    private String formatMSG(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
