/* Copyright (c) 2023, JeriCraftPerks. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.data;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerDataManager {

    private static final Map<Player, PlayerData> dataMap = new HashMap<>();

    public static void setData(Player player) {
        dataMap.put(player, new PlayerData(player));
    }

    public static PlayerData getData(Player player) {
        return dataMap.getOrDefault(player, null);
    }
}
