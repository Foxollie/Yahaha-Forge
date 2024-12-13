package net.foxolli3.yahaha.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.foxolli3.yahaha.Yahaha;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FredrickWandSchematicScreen extends Screen {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "textures/gui/fredrick_wand_schematic_gui.png");

    public FredrickWandSchematicScreen(Component pTitle) {
        super(pTitle);
    }
    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void renderBackground(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width) / 2;
        int y = (height) / 2;

        pGuiGraphics.blit(TEXTURE, x - 85, y - 80, 0, 0, 256, 256);
    }

    @Override
    protected void renderBlurredBackground(float pPartialTick) {
        super.renderBlurredBackground(pPartialTick);
    }


    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics,pMouseX,pMouseY,pPartialTick);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}