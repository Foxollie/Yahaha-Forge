package net.foxolli3.yahaha.item.custom;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.foxolli3.yahaha.entity.ModEntities;
import net.foxolli3.yahaha.item.Moditems;
import net.foxolli3.yahaha.screen.FredrickWandSchematicScreen;
import net.foxolli3.yahaha.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class KorokSeedItem extends Item {
    public KorokSeedItem(Properties properties) {
        super(properties);
    }

    @Override @OnlyIn(Dist.CLIENT)
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(pLevel.isClientSide()) {
            Minecraft.getInstance().setScreen(new FredrickWandSchematicScreen(Component.translatable("item.yahaha.fredrick_wand_schematic")));
            pLevel.playSeededSound(null, pPlayer.getX(),pPlayer.getY(),pPlayer.getZ(),
                    SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 0.4f, 1f,0);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}