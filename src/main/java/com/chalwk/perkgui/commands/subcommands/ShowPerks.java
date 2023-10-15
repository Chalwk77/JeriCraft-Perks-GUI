/* Copyright (c) 2023, JeriCraftPerks. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.perkgui.commands.subcommands;

import com.chalwk.perkgui.Main;
import com.chalwk.perkgui.commands.SubCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import static com.chalwk.perkgui.gui.MainMenu.showMainMenu;

public class ShowPerks extends SubCommand {

    private final FileConfiguration config = Main.getPluginConfig();

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
        showMainMenu(sender);
    }
}
