/* Copyright (c) 2023, JeriCraftPerks. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.jericraftperks.data;

import com.chalwk.jericraftperks.gui.CustomGUI;
import org.bukkit.entity.Player;

public class PlayerData {

    private final Player player;
    private CustomGUI openGUI;

    public PlayerData(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public CustomGUI getOpenGUI() {
        return openGUI;
    }

    public void setOpenGUI(CustomGUI openGUI) {
        this.openGUI = openGUI;
    }
}
