/* Copyright (c) 2023, JeriCraftPerks. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk.perkgui;

import com.chalwk.perkgui.commands.CommandManager;
import com.chalwk.perkgui.listener.GUIListener;
import com.chalwk.perkgui.listener.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Main extends JavaPlugin {

    static Main instance;
    public static FileConfiguration config;
    public static FileConfiguration getPluginConfig() {
        return config;
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        super.onEnable();
        instance = this;

        reloadConfig();
        registerListeners();

        String pluginVersion = getDescription().getVersion();
        getLogger().info("JeriCraftPerks [" + pluginVersion + "] has been loaded!");
        getCommand("perks").setExecutor(new CommandManager());
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        saveDefaultConfig();
        config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
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
