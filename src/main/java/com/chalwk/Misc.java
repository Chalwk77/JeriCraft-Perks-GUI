/* Copyright (c) 2023, JeriCraftPerks. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Misc {
    public static void send(Player sender, String msg) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public static void sound(Player player, String sound) {
        player.playSound(player.getLocation(), sound, 1,1);
    }

    public static String formatMSG(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
