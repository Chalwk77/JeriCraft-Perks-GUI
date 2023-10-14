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

        CustomGUI GUI = new CustomGUI(formatMSG(config.getString("GUI_TITLE")), 6);
        showNavigationButtons(sender, GUI);

        int slot = 0;
        for (Map<?, ?> opt : config.getMapList("PERMISSIONS")) {
            if (slot > 52) { // coming in a future update: multiple pages
                break;
            }
            List<String> perk = new ArrayList<>((Collection<? extends String>) opt.keySet());
            String permissionNode = perk.get(0);
            if (sender.hasPermission(permissionNode)) {
                Map<?, ?> data = (Map<?, ?>) opt.get(permissionNode);
                String name = (String) data.get("name");
                List<String> lore = (List<String>) data.get("lore");
                GUIButton perkButton = getGuiButton(sender, name, lore);
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

    private void showNavigationButtons(Player sender, CustomGUI GUI) {

        // LORE:
        List<String> lore = new ArrayList<>();
        lore.add(formatMSG("&cMultiple pages coming soon!"));

        // Previous page:
        ItemStack previous = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta previousMeta = previous.getItemMeta();
        previousMeta.setDisplayName(formatMSG(config.getString("GUI_PREVIOUS_PAGE")));
        previousMeta.setLore(lore);
        previous.setItemMeta(previousMeta);

        GUIButton previousButton = new GUIButton(previous);
        GUI.setItem(previousButton, 45);
        previousButton.setAction(sender::closeInventory); // TEMP

        // CLOSE BUTTON:
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(formatMSG(config.getString("GUI_CLOSE_BUTTON")));
        close.setItemMeta(closeMeta);

        GUIButton closeButton = new GUIButton(close);
        GUI.setItem(closeButton, 49);
        closeButton.setAction(sender::closeInventory);

        // Next page:
        ItemStack next = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta nextMeta = next.getItemMeta();
        nextMeta.setDisplayName(formatMSG(config.getString("GUI_NEXT_PAGE")));
        nextMeta.setLore(lore);
        next.setItemMeta(nextMeta);

        GUIButton nextButton = new GUIButton(next);
        GUI.setItem(nextButton, 53);
        nextButton.setAction(sender::closeInventory); // TEMP
    }

    @NotNull
    private GUIButton getGuiButton(Player sender, String name, List<String> lore) {

        ItemStack item = newPerkButton(name, lore);
        GUIButton perkButton = new GUIButton(item);
        perkButton.setAction(sender::closeInventory);

        return perkButton;
    }

    @NotNull
    private ItemStack newPerkButton(String name, List<String> lore) {

        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(formatMSG(name));

        lore.replaceAll(this::formatMSG);
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
