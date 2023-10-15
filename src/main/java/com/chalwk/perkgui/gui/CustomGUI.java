/* Copyright (c) 2023, JeriCraftPerks. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.perkgui.gui;

import com.chalwk.perkgui.Main;
import com.chalwk.perkgui.data.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CustomGUI {

    private final List<GUIButton> buttons = new ArrayList<>();
    private Inventory inventory;

    public CustomGUI(String title, int rows) {
        if (rows > 6) {
            Main.getInstance().getLogger().warning("Too many rows!");
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
}
