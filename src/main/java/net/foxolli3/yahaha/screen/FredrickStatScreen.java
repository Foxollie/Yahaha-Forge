package net.foxolli3.yahaha.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.foxolli3.yahaha.Yahaha;
import net.foxolli3.yahaha.entity.ModEntities;
import net.foxolli3.yahaha.entity.custom.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.w3c.dom.Text;

import java.awt.*;
import java.io.ObjectInputFilter;
import java.text.DecimalFormat;
import java.util.logging.Logger;

import static net.minecraft.client.gui.screens.inventory.InventoryScreen.renderEntityInInventory;
import static net.minecraft.client.gui.screens.inventory.InventoryScreen.renderEntityInInventoryFollowsMouse;
import static net.minecraft.world.item.crafting.RecipeType.SMITHING;

public class FredrickStatScreen extends Screen {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Yahaha.MOD_ID, "textures/gui/fredrick_stats_gui.png");

    private final LivingEntity preview;
    private final LivingEntity fredrickMob;
    private final LivingEntity fredrick;
    private final int luck;
    private final int radius;
    private final int effectLevel;
    private final float damage;
    private final String screenTitle;
    private Vec3 fredrickDir;
    boolean savedDir = false;
    public FredrickStatScreen(Component pTitle, Mob fredrickMob, int luck, int radius, LivingEntity fredrick, float damage, int effectLevel) {
        super(pTitle);
        this.fredrick = fredrick;
        this.fredrickMob = fredrickMob;
        this.screenTitle = pTitle.getString();
        this.luck = luck;
        this.effectLevel = effectLevel;
        this.damage = damage;
        this.radius = radius;
        if (fredrick.getType() == ModEntities.FREDRICK.get()) {
            this.preview = new FredrickMob(ModEntities.FREDRICK.get(), fredrickMob.level());
        } else if (fredrick.getType() == ModEntities.CRYO_FREDRICK.get()) {
            this.preview = new CryoFredrickMob(ModEntities.CRYO_FREDRICK.get(), fredrickMob.level());
        } else if (fredrick.getType() == ModEntities.PYRO_FREDRICK.get()) {
            this.preview = new PyroFredrickMob(ModEntities.PYRO_FREDRICK.get(), fredrickMob.level());
        } else if (fredrick.getType() == ModEntities.ELECTRO_FREDRICK.get()) {
            this.preview = new ElectroFredrickMob(ModEntities.ELECTRO_FREDRICK.get(), fredrickMob.level());
        } else {
            this.preview = new HydroFredrickMob(ModEntities.HYDRO_FREDRICK.get(), fredrickMob.level());
        }
    }
    float pmouseX;
    float pmouseY;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        pmouseX = mouseX;
        pmouseY = mouseY;
        // Render the backgroundstats
        guiGraphics.drawString(Minecraft.getInstance().font, "Health: " +decimalFormat.format(fredrickMob.getHealth()) + "/" + decimalFormat.format(fredrickMob.getMaxHealth()), this.width / 2 - 120, this.height / 2 - 80, 0xe0b1ff);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, screenTitle, this.width / 2 + 40, this.height / 2 - 80, 0xffffff);
        guiGraphics.drawString(Minecraft.getInstance().font, "Attack: " + this.damage, this.width / 2 - 120, this.height / 2 - 65, 0xebebec);
        guiGraphics.drawString(Minecraft.getInstance().font, "Defense: " + decimalFormat.format(fredrickMob.getAttributeValue(Attributes.ARMOR)), this.width / 2 - 120, this.height / 2 - 50, 0x776a7a);
        guiGraphics.drawString(Minecraft.getInstance().font, "Speed: " + decimalFormat.format(fredrickMob.getAttributeValue(Attributes.MOVEMENT_SPEED)*100-20), this.width / 2 - 120, this.height / 2 - 35, 0xfacfa7);
        guiGraphics.drawString(Minecraft.getInstance().font, "Scale: " + decimalFormat.format(fredrickMob.getScale()), this.width / 2 - 120, this.height / 2 - 20, 0x94a4ff);
        guiGraphics.drawString(Minecraft.getInstance().font, "Effect Radius: " + radius, this.width / 2 - 120, this.height / 2 - 5, 0xbafcfd);
        guiGraphics.drawString(Minecraft.getInstance().font, "Effect Level: " + effectLevel, this.width / 2 - 120, this.height / 2 + 10, 0x766d5d);
        guiGraphics.drawString(Minecraft.getInstance().font, "Trade Luck: " + luck, this.width / 2 - 120, this.height / 2 + 25, 0xa4fdab);
        if (preview.getType() == ModEntities.PYRO_FREDRICK.get()){
            guiGraphics.drawString(Minecraft.getInstance().font, "Burn Time: N/A", this.width / 2 - 120, this.height / 2 + 40, 0xff9d8c);
        }else {
            guiGraphics.drawString(Minecraft.getInstance().font, "Burn Time: " + decimalFormat.format(fredrick.getAttributeValue(Attributes.BURNING_TIME)), this.width / 2 - 120, this.height / 2 + 40, 0xff9d8c);
        }
    }

    @Override
    protected void renderMenuBackground(GuiGraphics pGuiGraphics, int pX, int pY, int pWidth, int pHeight) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width) / 2 - 50;
        int y = (height) / 2 - 20;

        pGuiGraphics.blit(TEXTURE, x - 85, y - 80, 0, 0, 256, 256);
        Vec3 cameraPosition = new Vec3(0, preview.getEyeHeight(), -1); // Adjust as needed
        preview.lookAt(EntityAnchorArgument.Anchor.FEET,cameraPosition);
        super.renderMenuBackground(pGuiGraphics, pX, pY, pWidth, pHeight);
        try {
            renderEntityInInventory(
                    pGuiGraphics,
                    (width) / 2 + 40,  // X position
                    (height) / 2 + 35,      // Y position
                    80,                // Scale
                    new Vector3f(),    // Translation vector
                    new Quaternionf().rotateY((float) Math.toRadians(190)).rotateX((float) Math.toRadians(180)),
                    new Quaternionf().rotateX((float) Math.toRadians(0)).rotateZ((float) Math.toRadians(180)),   // Pitch rotation (to ensure upright orientation)
                    preview            // The entity to render
            );
        } catch (NullPointerException e) {

        }
    }
    @Override
    public boolean isMouseOver(double pMouseX, double pMouseY) {
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
    @Override
    public void onClose() {
        savedDir = false;
        preview.remove(Entity.RemovalReason.DISCARDED);
        super.onClose();
    }
}