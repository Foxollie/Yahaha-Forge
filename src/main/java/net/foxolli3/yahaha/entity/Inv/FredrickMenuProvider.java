package net.foxolli3.yahaha.entity.Inv;

import net.foxolli3.yahaha.entity.custom.FredrickMob;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class FredrickMenuProvider implements MenuProvider {
    private final FredrickMob fredrickMob;

    public FredrickMenuProvider(FredrickMob fredrickMob) {
        this.fredrickMob = fredrickMob;
    }

    @Override
    public Component getDisplayName() {
        return Component.nullToEmpty("Fredrick Inventory");
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
        // Create and return your custom container menu here
        return null; // Replace null with your custom container menu instance
    }
}

