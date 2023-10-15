/* Copyright (c) 2023, JeriCraftPerks. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.perkgui.commands.subcommands;

import com.chalwk.perkgui.Main;
import com.chalwk.perkgui.commands.SubCommand;
import com.chalwk.perkgui.data.PlayerData;
import com.chalwk.perkgui.data.PlayerDataManager;
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
        CustomGUI menu = new CustomGUI(formatMSG(config.getString("MAIN-MENU-TITLE")), 3);
        showCloseButton(sender, menu, 22);

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

            ItemStack item = createItem(icon, title, lore);
            ItemMeta meta = item.getItemMeta();

            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);

            GUIButton button = new GUIButton(item);
            menu.setItem(button, slot);
            button.setAction(() -> {
                menu.close(sender);
                showPerks(sender, data, title);
            });

            slot = slot + 2;
        }
        menu.show(sender);
    }

    private void showPerks(Player sender, Map<?, ?> data, String title) {

        PlayerData gui = PlayerDataManager.getData(sender);
        if (gui != null && gui.getOpenGUI() != null) {
            gui.setOpenGUI(null);
        }

        CustomGUI menu = new CustomGUI(formatMSG(title), 6);

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
            menu.show(sender);
        }
    }

    private void showPerkButtons(Player sender, String name, String icon, List<String> lore, CustomGUI gui, int slot) {

        ItemStack item = createItem(icon, name, lore);

        GUIButton button = new GUIButton(item);
        button.setAction(() -> {
        });
        gui.setItem(button, slot);

        ItemStack backButton = createItem("RED_STAINED_GLASS_PANE", config.getString("GUI_PREVIOUS_PAGE"), new ArrayList<>());

        GUIButton back = new GUIButton(backButton);
        gui.setItem(back, 45);
        back.setAction(() -> {
        });

        showCloseButton(sender, gui, 53);
    }

    private ItemStack createItem(String icon, String name, List<String> lore) {

        ItemStack item = new ItemStack(Material.valueOf(icon));
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(formatMSG(name));
        lore.replaceAll(this::formatMSG);
        meta.setLore(lore);

        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);

        return item;
    }

    private void showCloseButton(Player sender, CustomGUI gui, int slot) {
        ItemStack close = createItem("BARRIER", config.getString("GUI_CLOSE_BUTTON"), new ArrayList<>());
        GUIButton closeButton = new GUIButton(close);
        gui.setItem(closeButton, slot);
        closeButton.setAction(sender::closeInventory);
    }

    private String formatMSG(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
