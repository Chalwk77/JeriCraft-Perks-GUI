/* Copyright (c) 2023, JeriCraftPerks. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.commands;

import org.bukkit.entity.Player;

public abstract class SubCommand {

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract String getPermission();

    public abstract void perform(Player player, String[] args);
}
