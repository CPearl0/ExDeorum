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

package thedarkcolour.exdeorum.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.blockentity.BarrelBlockEntity;
import thedarkcolour.exdeorum.blockentity.InfestedLeavesBlockEntity;
import thedarkcolour.exdeorum.blockentity.LavaCrucibleBlockEntity;
import thedarkcolour.exdeorum.blockentity.SieveBlockEntity;
import thedarkcolour.exdeorum.blockentity.WaterCrucibleBlockEntity;

public class EBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ExDeorum.ID);

    public static final RegistryObject<BlockEntityType<InfestedLeavesBlockEntity>> INFESTED_LEAVES = BLOCK_ENTITIES.register("infested_leaves", () -> BlockEntityType.Builder.<InfestedLeavesBlockEntity>of(InfestedLeavesBlockEntity::new, EBlocks.INFESTED_LEAVES.get()).build(null));
    public static final RegistryObject<BlockEntityType<LavaCrucibleBlockEntity>> LAVA_CRUCIBLE = BLOCK_ENTITIES.register("lava_crucible", () -> BlockEntityType.Builder.of(LavaCrucibleBlockEntity::new,
            EBlocks.WARPED_CRUCIBLE.get(),
            EBlocks.CRIMSON_CRUCIBLE.get(),
            EBlocks.PORCELAIN_CRUCIBLE.get()
    ).build(null));
    public static final RegistryObject<BlockEntityType<WaterCrucibleBlockEntity>> WATER_CRUCIBLE = BLOCK_ENTITIES.register("water_crucible", () -> BlockEntityType.Builder.of(WaterCrucibleBlockEntity::new,
            EBlocks.OAK_CRUCIBLE.get(),
            EBlocks.SPRUCE_CRUCIBLE.get(),
            EBlocks.BIRCH_CRUCIBLE.get(),
            EBlocks.JUNGLE_CRUCIBLE.get(),
            EBlocks.ACACIA_CRUCIBLE.get(),
            EBlocks.DARK_OAK_CRUCIBLE.get(),
            EBlocks.MANGROVE_CRUCIBLE.get(),
            EBlocks.CHERRY_CRUCIBLE.get(),
            EBlocks.BAMBOO_CRUCIBLE.get()
    ).build(null));
    public static final RegistryObject<BlockEntityType<BarrelBlockEntity>> BARREL = BLOCK_ENTITIES.register("barrel", () -> BlockEntityType.Builder.of(BarrelBlockEntity::new,
            EBlocks.OAK_BARREL.get(),
            EBlocks.SPRUCE_BARREL.get(),
            EBlocks.BIRCH_BARREL.get(),
            EBlocks.JUNGLE_BARREL.get(),
            EBlocks.ACACIA_BARREL.get(),
            EBlocks.DARK_OAK_BARREL.get(),
            EBlocks.MANGROVE_BARREL.get(),
            EBlocks.CHERRY_BARREL.get(),
            EBlocks.BAMBOO_BARREL.get(),
            EBlocks.CRIMSON_BARREL.get(),
            EBlocks.WARPED_BARREL.get(),
            EBlocks.STONE_BARREL.get()
    ).build(null));
    public static final RegistryObject<BlockEntityType<SieveBlockEntity>> SIEVE = BLOCK_ENTITIES.register("sieve", () -> BlockEntityType.Builder.of(SieveBlockEntity::new,
            EBlocks.OAK_SIEVE.get(),
            EBlocks.SPRUCE_SIEVE.get(),
            EBlocks.BIRCH_SIEVE.get(),
            EBlocks.JUNGLE_SIEVE.get(),
            EBlocks.ACACIA_SIEVE.get(),
            EBlocks.DARK_OAK_SIEVE.get(),
            EBlocks.MANGROVE_SIEVE.get(),
            EBlocks.CHERRY_SIEVE.get(),
            EBlocks.BAMBOO_SIEVE.get(),
            EBlocks.CRIMSON_SIEVE.get(),
            EBlocks.WARPED_SIEVE.get()
    ).build(null));
}