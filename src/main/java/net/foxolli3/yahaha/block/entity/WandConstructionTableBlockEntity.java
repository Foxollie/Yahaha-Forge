package net.foxolli3.yahaha.block.entity;

import net.foxolli3.yahaha.item.Moditems;
import net.foxolli3.yahaha.screen.WandConstructionTableMenu;
import net.foxolli3.yahaha.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.level.NoteBlockEvent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WandConstructionTableBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(8);
    private static final int INPUT_SLOT = 0;

    private static final int INPUT_SLOT_7 = 1;
    private static final int INPUT_SLOT_2 = 2;
    private static final int INPUT_SLOT_3 = 3;
    private static final int INPUT_SLOT_4 = 4;
    private static final int INPUT_SLOT_5 = 5;
    private static final int INPUT_SLOT_6 = 6;
    private static final int OUTPUT_SLOT = 7;

    private LazyOptional<IItemHandler> lazyitemhandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 110;

    String isCrafting = String.valueOf("enableAble");

    public WandConstructionTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.WAND_TABLE_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> WandConstructionTableBlockEntity.this.progress;
                    case 1 -> WandConstructionTableBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> WandConstructionTableBlockEntity.this.progress = pValue;
                    case 1 -> WandConstructionTableBlockEntity.this.maxProgress = pValue;
                }

            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyitemhandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyitemhandler = LazyOptional.of(() -> itemHandler);
    }
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyitemhandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i,itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.yahaha.wand_construction_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new WandConstructionTableMenu(pContainerId, pPlayerInventory, this,this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", itemHandler.serializeNBT(pRegistries));
        pTag.putInt("wand_construction_table.progress", progress);
        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        progress = pTag.getInt("wand_construction_table.progress");
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if(hasRecipe()) {
            increaseCraftingProgress();
            setChanged(pLevel, pPos, pState);
            if(isCrafting == "enableAble") {
                isCrafting = String.valueOf("true");
            }
            if(isCrafting == "true") {
                playHestuDance();
                isCrafting = String.valueOf("false");
            }

            if(hasProgressFinished()) {
                craftItem();
                resetProgress();
                playKorokPop();
            }
        } else{
            resetProgress();
        }
    }

    private void playHestuDance() {
        getLevel().playSeededSound(null, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(),
                ModSounds.HESTU_DANCE.get(), SoundSource.BLOCKS, 0.25f, 1f,0);
    }

    private void playKorokPop() {
        getLevel().playSeededSound(null, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(),
                ModSounds.KOROK_POP.get(), SoundSource.BLOCKS, 0.1f, 1f,0);
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void resetProgress() {
        progress = 0;
        isCrafting = String.valueOf("enableAble");
    }

    private void craftItem() {
        ItemStack result = new ItemStack(Moditems.KOROK_WAND_FULL.get(),1);
        this.itemHandler.extractItem(INPUT_SLOT,1,false);
        this.itemHandler.extractItem(INPUT_SLOT_2,1,false);
        this.itemHandler.extractItem(INPUT_SLOT_3,1,false);
        this.itemHandler.extractItem(INPUT_SLOT_4,1,false);
        this.itemHandler.extractItem(INPUT_SLOT_5,1,false);
        this.itemHandler.extractItem(INPUT_SLOT_6,1,false);
        this.itemHandler.extractItem(INPUT_SLOT_7,1,false);



        this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private boolean hasRecipe() {
        boolean hasCraftingItem = this.itemHandler.getStackInSlot(INPUT_SLOT).getItem() == Moditems.OCTOROK_EYEBALL.get() && //top left
                this.itemHandler.getStackInSlot(INPUT_SLOT_2).getItem() == Moditems.OCTOROK_EYEBALL.get() && //top right
                this.itemHandler.getStackInSlot(INPUT_SLOT_3).getItem() == Blocks.OAK_LOG.asItem() && //bottom right
                this.itemHandler.getStackInSlot(INPUT_SLOT_4).getItem() == Moditems.KOROK_FROND.get() && //top center
                this.itemHandler.getStackInSlot(INPUT_SLOT_5).getItem() == Moditems.KOROK_WAND_BLOCK.get() && //middle
                this.itemHandler.getStackInSlot(INPUT_SLOT_6).getItem() == Items.COOKED_BEEF && //bottom
                this.itemHandler.getStackInSlot(INPUT_SLOT_7).getItem() == Blocks.OAK_LOG.asItem(); //bottom left
        ItemStack result = new ItemStack(Moditems.KOROK_WAND_FULL.get());


        return hasCraftingItem && canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

}
