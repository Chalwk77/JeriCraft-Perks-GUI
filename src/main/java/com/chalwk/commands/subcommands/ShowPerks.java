/* Copyright (c) 2023, JeriCraftPerks. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.commands.subcommands;

import com.chalwk.commands.SubCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static com.chalwk.JeriCraftPerkGUI.getPluginConfig;
import static com.chalwk.gui.Menu.showMenu;

public class ShowPerks extends SubCommand {

    private static final FileConfiguration config = getPluginConfig();
    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getDescription() {
        return "Shows you the perks you have purchased.";
    }

    @Override
    public String getSyntax() {
        return "/perks " + getName();
    }

    @Override
    public String getPermission() {
        return "perks.show";
    }

    @Override
    public void perform(Player sender, String[] args) {
        showMenu(sender, config.getString("MAIN-MENU-TITLE"), 3, true);
    }
}
