/* Copyright (c) 2023, JeriCraftPerks. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.perkgui.data;

import com.chalwk.perkgui.gui.CustomGUI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import java.util.Map;


public class PlayerData {

    private int moneySpent;
    private final Player player;
    private CustomGUI openGUI;

    public PlayerData(Player player) {
        this.moneySpent = 0;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public int getMoneySpent() {
        return moneySpent;
    }

    public CustomGUI getOpenGUI() {
        return openGUI;
    }

    public void setOpenGUI(CustomGUI openGUI) {
        this.openGUI = openGUI;
    }

    public void setMoneySpent(int i) {
        this.moneySpent = i;
    }

    public void updateMoneySpent(Player sender, Map<?, ?> data) {
        Map<?, ?> perks = (Map<?, ?>) data.get("perks");
        for (Map.Entry<?, ?> entry : perks.entrySet()) {
            String node = (String) entry.getKey();
            if (sender.hasPermission(node)) {
                Map<?, ?> perkData = (Map<?, ?>) entry.getValue();
                int price = (int) perkData.get("price");
                this.moneySpent = this.moneySpent + price;
            }
        }
    }
}
