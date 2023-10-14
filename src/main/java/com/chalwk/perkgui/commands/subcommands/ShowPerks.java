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

        CustomGUI GUI = new CustomGUI(ChatColor.BLUE + "YOUR PERKS:", 6);

        // CLOSE BUTTON:
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(ChatColor.RED + "Close!");
        close.setItemMeta(closeMeta);

        GUIButton closeButton = new GUIButton(close);
        GUI.setItem(closeButton, 53);
        closeButton.setAction(sender::closeInventory);

        // PERK BUTTONS:
        int slot = 0;
        for (Map<?, ?> opt : config.getMapList("PERMISSIONS")) {
            if (slot > 52) {
                break;
            }
            List<String> perk = new ArrayList<>((Collection<? extends String>) opt.keySet());
            for (String node : perk) {
                if (sender.hasPermission(node)) {
                    GUIButton perkButton = getGuiButton(sender, opt);
                    GUI.setItem(perkButton, slot);
                    slot++;
                }
            }
        }
        if (slot > 0) {
            GUI.show(sender);
        } else {
            sender.sendMessage(ChatColor.RED + "You have no perks!");
        }
    }

    @NotNull
    private GUIButton getGuiButton(Player sender, Map<?, ?> opt) {

        List<String> perks = new ArrayList<>((Collection<? extends String>) opt.values());
        String perk = perks.get(0);
        String price = perks.get(1);

        ItemStack item = newPerkButton(perk, price);
        GUIButton perkButton = new GUIButton(item);
        perkButton.setAction(sender::closeInventory);

        return perkButton;
    }

    @NotNull
    private ItemStack newPerkButton(String perk, String price) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', perk));

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "$" + price));
        meta.setLore(lore);

        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);

        item.setItemMeta(meta);
        return item;
    }
}
