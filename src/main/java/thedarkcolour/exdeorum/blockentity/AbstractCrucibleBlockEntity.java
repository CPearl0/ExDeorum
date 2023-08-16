/*
 * Ex Deorum
 * Copyright (c) 2023 thedarkcolour
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package thedarkcolour.exdeorum.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exdeorum.registry.EItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.function.Consumer;

public abstract class AbstractCrucibleBlockEntity extends EBlockEntity {
    public static final Lazy<HashMap<Item, Block>> MELT_OVERRIDES = Lazy.concurrentOf(() -> {
        var map = new HashMap<Item, Block>();
        addMeltOverrides(map);
        return map;
    });

    public static final int MAX_SOLIDS = 1_000;

    private final AbstractCrucibleBlockEntity.ItemHandler item = new AbstractCrucibleBlockEntity.ItemHandler();
    private final AbstractCrucibleBlockEntity.FluidHandler tank = new AbstractCrucibleBlockEntity.FluidHandler();
    // Capabilities
    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> item);
    private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> tank);

    private Block lastMelted;
    private Fluid fluid;
    private short solids;

    public AbstractCrucibleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!remove) {
            if (cap == ForgeCapabilities.FLUID_HANDLER) {
                return fluidHandler.cast();
            } else if (cap == ForgeCapabilities.ITEM_HANDLER) {
                return itemHandler.cast();
            }
        }

        return super.getCapability(cap, side);
    }

    // NBT
    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        nbt.put("Tank", tank.writeToNBT(new CompoundTag()));
        nbt.putString("LastMelted", ForgeRegistries.BLOCKS.getKey(lastMelted).toString());
        nbt.putString("Fluid", ForgeRegistries.FLUIDS.getKey(fluid).toString());
        nbt.putShort("Solids", solids);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        tank.readFromNBT(nbt.getCompound("Tank"));
        lastMelted = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("LastMelted")));
        fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(nbt.getString("Fluid")));
        solids = nbt.getShort("Solids");
    }

    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        var playerItem = player.getItemInHand(hand);

        if (playerItem.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()) {
            return FluidUtil.interactWithFluidHandler(player, hand, tank) ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            tryMelt(playerItem, player.getAbilities().instabuild ? stack -> {} : stack -> stack.shrink(1));
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    // Gets a crucible recipe, using the cache if possible
    protected abstract CrucibleRecipe getRecipe(ItemStack item);

    /**
     * Tries to melt the specified item into the crucible.
     *
     * @param item         Item to melt
     * @param shrinkAction What to do when item is melted
     */
    private void tryMelt(ItemStack item, Consumer<ItemStack> shrinkAction) {
        CrucibleRecipe recipe = getRecipe(item);

        if (recipe != null) {
            FluidStack result = recipe.getResult();
            FluidStack contained = tank.getFluid();

            if (((result.isFluidEqual(contained) || contained.isEmpty()) && result.getAmount() + solids <= MAX_SOLIDS)) {
                var meltItem = item.getItem();
                shrinkAction.accept(item);
                solids += result.getAmount();

                if (contained.isEmpty()) {
                    fluid = result.getFluid();
                }

                var melts = MELT_OVERRIDES.get();
                if (melts.containsKey(meltItem)) {
                    lastMelted = melts.get(meltItem);
                } else if (meltItem.getClass() == BlockItem.class) {
                    lastMelted = ((BlockItem) meltItem).getBlock();
                } else {
                    // If we already have something else inside just use that instead of switching to default
                    if (lastMelted == null) {
                        lastMelted = getDefaultMeltBlock();
                    }
                }

                markUpdated();
            }
        }
    }

    public int getMeltingRate() {
        return 1;
    }

    public int getSolids() {
        return solids;
    }

    public FluidTank getTank() {
        return tank;
    }

    public abstract Block getDefaultMeltBlock();

    public Block getLastMelted() {
        return lastMelted;
    }

    @Override
    public void setRemoved() {
        itemHandler.invalidate();
        fluidHandler.invalidate();
        super.setRemoved();
    }

    private static void addMeltOverrides(HashMap<Item, Block> overrides) {
        overrides.put(Items.OAK_SAPLING, Blocks.OAK_LEAVES);
        overrides.put(Items.SPRUCE_SAPLING, Blocks.SPRUCE_LEAVES);
        overrides.put(Items.ACACIA_SAPLING, Blocks.ACACIA_LEAVES);
        overrides.put(Items.JUNGLE_SAPLING, Blocks.JUNGLE_LEAVES);
        overrides.put(Items.DARK_OAK_SAPLING, Blocks.DARK_OAK_LEAVES);
        overrides.put(Items.BIRCH_SAPLING, Blocks.BIRCH_LEAVES);
        overrides.put(Items.CHERRY_SAPLING, Blocks.CHERRY_LEAVES);
        overrides.put(Items.MANGROVE_PROPAGULE, Blocks.MANGROVE_LEAVES);
        overrides.put(Items.SWEET_BERRIES, Blocks.SPRUCE_LEAVES);
        overrides.put(Items.GLOW_BERRIES, Blocks.MOSS_BLOCK);
        overrides.put(EItems.GRASS_SEEDS.get(), Blocks.GRASS_BLOCK);
        overrides.put(EItems.MYCELIUM_SPORES.get(), Blocks.MYCELIUM);
        overrides.put(EItems.WARPED_NYLIUM_SPORES.get(), Blocks.WARPED_NYLIUM);
        overrides.put(EItems.CRIMSON_NYLIUM_SPORES.get(), Blocks.WARPED_NYLIUM);

        for (var sapling : ForgeRegistries.BLOCKS.getEntries()) {
            var key = sapling.getKey().location();

            if (key.getPath().endsWith("sapling")) {
                try {
                    var item = sapling.getValue().asItem();
                    ForgeRegistries.BLOCKS.getValue(new ResourceLocation(key.getNamespace(), key.getPath().replace("sapling", "leaves")));
                    overrides.put(item, Blocks.OAK_LEAVES);
                } catch (Exception ignored) {
                }
            }
        }
    }

    private static class FluidHandler extends FluidTank {
        public FluidHandler() {
            super(4_000);
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return false;
        }
    }

    // inner class
    private class ItemHandler extends ItemStackHandler {
        @Override
        protected void onContentsChanged(int slot) {
            tryMelt(getItem(), item -> setStackInSlot(0, ItemStack.EMPTY));
        }

        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return getRecipe(stack) != null;
        }

        public ItemStack getItem() {
            return stacks.get(0);
        }
    }

    // Only ticks on client
    public static class Ticker implements BlockEntityTicker<AbstractCrucibleBlockEntity> {
        @Override
        public void tick(Level level, BlockPos pos, BlockState state, AbstractCrucibleBlockEntity crucible) {
            // Update twice per tick
            if ((level.getGameTime() % 10L) == 0L) {
                int delta = Math.min(crucible.solids, crucible.getMeltingRate());

                // Skip if no heat
                if (delta <= 0) return;

                if (crucible.tank.getSpace() >= delta) {
                    // Remove solids
                    crucible.solids -= delta;

                    // Add lava
                    if (crucible.tank.isEmpty()) {
                        crucible.tank.setFluid(new FluidStack(crucible.fluid, delta));
                    } else {
                        crucible.tank.getFluid().grow(delta);
                    }

                    // Sync to client
                    crucible.markUpdated();
                }
            }
        }
    }
}