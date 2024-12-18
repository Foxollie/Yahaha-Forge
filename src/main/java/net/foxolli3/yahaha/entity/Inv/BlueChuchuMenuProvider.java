package net.foxolli3.yahaha.entity.Inv;

import net.foxolli3.yahaha.entity.custom.BlueChuchuMob;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class BlueChuchuMenuProvider implements MenuProvider {
    private final BlueChuchuMob blueChuchuMob;

    public BlueChuchuMenuProvider(BlueChuchuMob blueChuchuMob) {
        this.blueChuchuMob = blueChuchuMob;
    }

    @Override
    public Component getDisplayName() {
        return Component.nullToEmpty("Blue Chuchu Inventory");
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
        // Create and return your custom container menu here
        return null; // Replace null with your custom container menu instance
    }
}

