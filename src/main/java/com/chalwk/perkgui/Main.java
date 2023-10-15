/* Copyright (c) 2023, JeriCraftPerks. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk.perkgui;

import com.chalwk.perkgui.commands.CommandManager;
import com.chalwk.perkgui.listener.GUIListener;
import com.chalwk.perkgui.listener.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Main extends JavaPlugin {

    public static Map<UUID, Integer> playerSpend = new HashMap<>();
    public static Main instance;
    private static FileConfiguration config;

    public static void send(Player sender, String msg) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public static void ReloadConfig() {
        instance.reloadConfig();
    }

    public static Main getInstance() {
        return instance;
    }

    public static FileConfiguration getPluginConfig() {
        return config;
    }

    @Override
    public void onEnable() {

        instance = this;
        config = this.getConfig();
        config.options().copyDefaults(true);
        saveConfig();
        registerListeners();

        String pluginVersion = getDescription().getVersion();
        getLogger().info("JeriCraftPerks [" + pluginVersion + "] has been loaded!");
        getCommand("perks").setExecutor(new CommandManager());
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("JeriCraftPerks has been unloaded!");
    }
}
