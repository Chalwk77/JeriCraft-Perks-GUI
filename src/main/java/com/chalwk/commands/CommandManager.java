/* Copyright (c) 2023, JeriCraftPerks. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static com.chalwk.Misc.send;
import static org.reflections.Reflections.log;

public class CommandManager implements CommandExecutor, TabCompleter {

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public CommandManager() {
        Reflections reflections = new Reflections("com.chalwk.commands.subcommands");
        for (Class<?> commandClass : reflections.getSubTypesOf(SubCommand.class)) {
            try {
                subcommands.add((SubCommand) commandClass.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                log.warn("Failed to load command: " + commandClass.getName(), e, Level.WARNING);
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            log.info("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        if (args.length > 0) {
            for (int i = 0; i < getSubCommands().size(); i++) {
                String subCommand = getSubCommands().get(i).getName();
                String permissionNode = getSubCommands().get(i).getPermission();
                if (args[0].equalsIgnoreCase(subCommand)) {
                    if (hasPermission(player, permissionNode)) {
                        getSubCommands().get(i).perform(player, args);
                    }
                    return true;
                }
            }
        }

        send(player, "&cInvalid Syntax. Usage: &b/perks show/reload");
        return true;
    }

    public ArrayList<SubCommand> getSubCommands() {
        return subcommands;
    }

    public boolean hasPermission(Player player, String permission) {
        if (player.hasPermission(permission)) {
            return true;
        } else {
            send(player, "&cYou do not have permission to use this command!");
            return false;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        ArrayList<String> tab = new ArrayList<>();
        if (args.length == 1) {
            for (SubCommand subCommand : getSubCommands()) {
                tab.add(subCommand.getName());
            }
            return tab;
        }
        return tab;
    }
}
