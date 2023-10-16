/* Copyright (c) 2023, JeriCraft-Perks-GUI. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.perkgui.data;

import com.chalwk.perkgui.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    private static File file;
    public static FileConfiguration config;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("JeriCraft-Perks-GUI").getDataFolder(), "config.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Main.getInstance().getLogger().warning("Could not create config.yml file!");
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return config;
    }

    public static void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            Main.getInstance().getLogger().warning("Could not save config.yml file!");
        }
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}
