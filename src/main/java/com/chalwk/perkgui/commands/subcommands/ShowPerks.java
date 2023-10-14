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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ShowPerks extends SubCommand {

    private final FileConfiguration config = Main.getInstance().getConfig();

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

        CustomGUI GUI = new CustomGUI(formatMSG(config.getString("GUI_TITLE")), 6);

        // CLOSE BUTTON:
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(formatMSG(config.getString("GUI_CLOSE_BUTTON")));
        close.setItemMeta(closeMeta);

        GUIButton closeButton = new GUIButton(close);
        GUI.setItem(closeButton, 53);
        closeButton.setAction(sender::closeInventory);

        // PERK BUTTONS:
        int slot = 0;
        for (Map<?, ?> opt : config.getMapList("PERMISSIONS")) {
            if (slot > 52) { // coming in a future update: multiple pages
                break;
            }

            List<String> perk = new ArrayList<>((Collection<? extends String>) opt.keySet());
            String permissionNode = perk.get(0);
            if (sender.hasPermission(permissionNode)) {
                GUIButton perkButton = getGuiButton(sender, opt);
                GUI.setItem(perkButton, slot);
                slot++;
            }
        }
        if (slot > 0) {
            GUI.show(sender);
        } else {
            sender.sendMessage(formatMSG(config.getString("NO_PERKS")));
        }
    }

    @NotNull
    private GUIButton getGuiButton(Player sender, Map<?, ?> opt) {

        List<String> data = new ArrayList<>((Collection<? extends String>) opt.values());
        String perk = data.get(0);
        String price = data.get(1);
        ItemStack item = newPerkButton(perk, price);
        GUIButton perkButton = new GUIButton(item);
        perkButton.setAction(sender::closeInventory);

        return perkButton;
    }

    @NotNull
    private ItemStack newPerkButton(String perk, String price) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(formatMSG(perk));

        List<String> lore = new ArrayList<>();
        lore.add(formatMSG("$" + price));
        meta.setLore(lore);

        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        return item;
    }

    private String formatMSG(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
