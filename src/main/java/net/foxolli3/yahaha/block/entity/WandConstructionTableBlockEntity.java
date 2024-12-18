package net.foxolli3.yahaha.block.entity;

import net.foxolli3.yahaha.component.ModDataComponentTypes;
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

    ItemStack result;

    private void craftItem() {
        this.itemHandler.extractItem(INPUT_SLOT,1,false);
        this.itemHandler.extractItem(INPUT_SLOT_2,1,false);
        this.itemHandler.extractItem(INPUT_SLOT_3,1,false);
        this.itemHandler.extractItem(INPUT_SLOT_4,1,false);
        this.itemHandler.extractItem(INPUT_SLOT_5,1,false);
        this.itemHandler.extractItem(INPUT_SLOT_6,1,false);
        this.itemHandler.extractItem(INPUT_SLOT_7,1,false);



        ItemStack outputStack = new ItemStack(result.getItem(),
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount());

        // Check if the result has the FROND_USES component and copy it
            if (result.has(ModDataComponentTypes.FROND_USES.get())) {
                outputStack.set(ModDataComponentTypes.FROND_USES.get(), result.get(ModDataComponentTypes.FROND_USES.get()));
            }

        this.itemHandler.setStackInSlot(OUTPUT_SLOT, outputStack);


    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private boolean hasRecipe() {
        // Recipe 1: Crafting with Octorok Eyeballs and other ingredients
        boolean hasRecipe1 =
                        this.itemHandler.getStackInSlot(INPUT_SLOT).getItem() == Moditems.OCTOROK_EYEBALL.get() && // top left
                        this.itemHandler.getStackInSlot(INPUT_SLOT_2).getItem() == Moditems.OCTOROK_EYEBALL.get() && // top right
                        this.itemHandler.getStackInSlot(INPUT_SLOT_3).getItem() == Blocks.OAK_LOG.asItem() && // bottom right
                        this.itemHandler.getStackInSlot(INPUT_SLOT_4).getItem() == Moditems.KOROK_FROND.get() && // top center
                                (this.itemHandler.getStackInSlot(INPUT_SLOT_5).getItem() == Moditems.KOROK_WAND_BLOCK.get() ||
                                        (this.itemHandler.getStackInSlot(INPUT_SLOT_5).getItem() == Moditems.KOROK_WAND_BLOCK.get() ||
                                                (this.itemHandler.getStackInSlot(INPUT_SLOT_5).getItem() == Moditems.KOROK_WAND_BLOCK.get() ||
                                                        (this.itemHandler.getStackInSlot(INPUT_SLOT_5).getItem() == Moditems.KOROK_WAND_BLOCK.get() ||
                                                                (this.itemHandler.getStackInSlot(INPUT_SLOT_5).getItem() == Moditems.KOROK_WAND_BLOCK.get())))))  && // middle
                        this.itemHandler.getStackInSlot(INPUT_SLOT_6).getItem() == Items.COOKED_BEEF && // bottom
                        this.itemHandler.getStackInSlot(INPUT_SLOT_7).getItem() == Blocks.OAK_LOG.asItem(); // bottom left
        if(hasRecipe1){
            result = new ItemStack(Moditems.KOROK_WAND_FULL.get());
        }

        // Recipe 2: Crafting with Korok Wand Empty and Korok Fronds
        boolean hasKorokWandEmptyInMiddle = this.itemHandler.getStackInSlot(INPUT_SLOT_5).getItem() == Moditems.KOROK_WAND_EMPTY.get(); // middle
        int korokFrondCount = 0;
        boolean noClutter = true;

        // Count Korok Fronds in all the other slots (excluding the middle)
        for (int i = 0; i < 8; i++) {
            if (i != INPUT_SLOT_5 && this.itemHandler.getStackInSlot(i).getItem() == Moditems.KOROK_FROND.get()) {
                korokFrondCount++;
            } else if(i != INPUT_SLOT_5 && (this.itemHandler.getStackInSlot(i).getItem() != Moditems.KOROK_FROND.get() && this.itemHandler.getStackInSlot(i).getCount() > 0)){
                    noClutter = false;
            }
        }

        for (int i = 0; i < 8; i++) {
            if (i != INPUT_SLOT_5 && this.itemHandler.getStackInSlot(i).getItem() == Moditems.KOROK_FROND.get()) {
                korokFrondCount++;
            } else if(i != INPUT_SLOT_5 && (this.itemHandler.getStackInSlot(i).getItem() != Moditems.KOROK_FROND.get() && this.itemHandler.getStackInSlot(i).getCount() > 0)){
                noClutter = false;
            }
        }

        // Recipe 2 conditions: Korok Wand Empty in the middle and 1-6 Korok Fronds in the other slots
        boolean hasFrondsAndWandEmpty = hasKorokWandEmptyInMiddle && korokFrondCount > 0 && korokFrondCount <= 6;
        boolean hasShroomAndWandEmpty = hasKorokWandEmptyInMiddle && korokFrondCount > 0 && korokFrondCount <= 6;

        boolean canCraft = false;

        // If Recipe 2 is valid, craft the Korok Wand Frond with the correct frond_use component
        if (hasFrondsAndWandEmpty) {
            // Create the item with the correct frond_use component
            result = new ItemStack(Moditems.KOROK_WAND_FROND.get());
            // Set the frond_use component based on how many fronds are in the grid
            result.set(ModDataComponentTypes.FROND_USES.get(), korokFrondCount);

            // Check if the crafted item can be inserted into the output slot
            canCraft = canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem()) && noClutter;
        }


        if (hasShroomAndWandEmpty) {
            // Create the item with the correct frond_use component
            result = new ItemStack(Moditems.KOROK_WAND_FROND.get());
            // Set the frond_use component based on how many fronds are in the grid
            result.set(ModDataComponentTypes.FROND_USES.get(), korokFrondCount);

            // Check if the crafted item can be inserted into the output slot
            canCraft = canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem()) && noClutter;
        }

        // Return true if either Recipe 1 or Recipe 2 is valid and can craft
        return (hasRecipe1 && canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem())) || (hasFrondsAndWandEmpty && canCraft);
    }



    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

}
