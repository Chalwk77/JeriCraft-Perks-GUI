/* Copyright (c) 2023, JeriCraftPerks. Jericho Crosby <jericho.crosby227@gmail.com> */
package com.chalwk.gui;

import org.bukkit.inventory.ItemStack;

public class GUIButton {

    private final ItemStack stack;
    private Runnable action;

    public GUIButton(ItemStack stack) {
        this.stack = stack;
    }

    public Runnable getAction() {
        return action;
    }

    public void setAction(Runnable runnable) {
        this.action = runnable;
    }

    public ItemStack getStack() {
        return stack;
    }
}
