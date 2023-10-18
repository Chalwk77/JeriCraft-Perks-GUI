/* Copyright (c) 2023, JeriCraftPerks. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.listener;

import com.chalwk.data.PlayerData;
import com.chalwk.data.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GUIListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        PlayerData data = PlayerDataManager.getData((Player) e.getWhoClicked());
        if (data != null && data.getOpenGUI() != null && e.getClickedInventory() != null) {
            data.getOpenGUI().handleButton(e.getCurrentItem());
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        PlayerData data = PlayerDataManager.getData((Player) e.getPlayer());
        if (data != null && data.getOpenGUI() != null) {
            data.setOpenGUI(null);
        }
    }
}
