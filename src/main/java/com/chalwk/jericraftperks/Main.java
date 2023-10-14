/* Copyright (c) 2023, JeriCraftPerks. Jericho Crosby <jericho.crosby227@gmail.com> */

package com.chalwk.jericraftperks;

import com.chalwk.jericraftperks.commands.CommandManager;
import com.chalwk.jericraftperks.listener.GUIListener;
import com.chalwk.jericraftperks.listener.JoinListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main instance;

    public static void send(Player sender, String msg) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public static void ReloadConfig() {
        instance.reloadConfig();
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new GUIListener(), this);
        this.getServer().getPluginManager().registerEvents(new JoinListener(), this);

        String pluginVersion = getDescription().getVersion();
        getLogger().info("JeriCraftPerks [" + pluginVersion + "] has been loaded!");
        getCommand("perks").setExecutor(new CommandManager());
    }

    @Override
    public void onDisable() {
        getLogger().info("JeriCraftPerks has been unloaded!");
    }
}
