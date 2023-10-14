/* Copyright (c) 2023, JeriCraftPerks. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.jericraftperks.commands.subcommands;

import com.chalwk.jericraftperks.Main;
import com.chalwk.jericraftperks.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Reload extends SubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads the plugin";
    }

    @Override
    public String getSyntax() {
        return "&cInvalid Syntax. Usage: &b/perks reload";
    }

    @Override
    public String getPermission() {
        return "perks.reload";
    }

    @Override
    public void perform(Player sender, String[] args) {
        if (args.length == 1) {
            Main.ReloadConfig();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8============================="));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3JeriCraftPerks reloaded!"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8============================="));
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getSyntax()));
        }
    }
}
