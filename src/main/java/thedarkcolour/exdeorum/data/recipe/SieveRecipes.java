/*
 * Ex Deorum
 * Copyright (c) 2024 thedarkcolour
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

package thedarkcolour.exdeorum.data.recipe;

import com.google.common.collect.ImmutableMap;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.compat.ModIds;
import thedarkcolour.exdeorum.data.ModCompatData;
import thedarkcolour.exdeorum.recipe.sieve.FinishedCompressedSieveRecipe;
import thedarkcolour.exdeorum.recipe.sieve.FinishedSieveRecipe;
import thedarkcolour.exdeorum.registry.ECompressedBlocks;
import thedarkcolour.exdeorum.registry.EItems;
import thedarkcolour.exdeorum.tag.EItemTags;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator.binomial;
import static thedarkcolour.modkit.data.MKRecipeProvider.ingredient;
import static thedarkcolour.modkit.data.MKRecipeProvider.path;

class SieveRecipes {
    // Ingredients do not implement .equals, so we need constants in order to map them to compressed variants
    private static final Ingredient
            DIRT = ingredient(Items.DIRT),
            GRAVEL = ingredient(Items.GRAVEL),
            SAND = ingredient(Items.SAND),
            DUST = ingredient(EItems.DUST.get()),
            RED_SAND = ingredient(Items.RED_SAND),
            CRUSHED_DEEPSLATE = ingredient(EItems.CRUSHED_DEEPSLATE.get()),
            CRUSHED_BLACKSTONE = ingredient(EItems.CRUSHED_BLACKSTONE.get()),
            CRUSHED_NETHERRACK = ingredient(EItems.CRUSHED_NETHERRACK.get()),
            SOUL_SAND = ingredient(Items.SOUL_SAND),
            CRUSHED_END_STONE = ingredient(EItems.CRUSHED_END_STONE),
            MOSS_BLOCK = ingredient(Items.MOSS_BLOCK);
    // mod condition is null for ex deorum blocks (ex deorum is always last priority)
    private static final Map<Ingredient, Ingredient> COMPRESSED_VARIANTS = ImmutableMap.<Ingredient, Ingredient>builder()
            .put(DIRT, ingredient(ECompressedBlocks.COMPRESSED_DIRT.getTag()))
            .put(GRAVEL, ingredient(ECompressedBlocks.COMPRESSED_GRAVEL.getTag()))
            .put(SAND, ingredient(ECompressedBlocks.COMPRESSED_SAND.getTag()))
            .put(DUST, ingredient(ECompressedBlocks.COMPRESSED_DUST.getTag()))
            .put(RED_SAND, ingredient(ECompressedBlocks.COMPRESSED_RED_SAND.getTag()))
            .put(CRUSHED_DEEPSLATE, ingredient(ECompressedBlocks.COMPRESSED_CRUSHED_DEEPSLATE.getTag()))
            .put(CRUSHED_BLACKSTONE, ingredient(ECompressedBlocks.COMPRESSED_CRUSHED_BLACKSTONE.getTag()))
            .put(CRUSHED_NETHERRACK, ingredient(ECompressedBlocks.COMPRESSED_CRUSHED_NETHERRACK.getTag()))
            .put(SOUL_SAND, ingredient(ECompressedBlocks.COMPRESSED_SOUL_SAND.getTag()))
            .put(CRUSHED_END_STONE, ingredient(ECompressedBlocks.COMPRESSED_CRUSHED_END_STONE.getTag()))
            .put(MOSS_BLOCK, ingredient(ECompressedBlocks.COMPRESSED_MOSS_BLOCK.getTag()))
            .build();

    static void sieveRecipes(Consumer<FinishedRecipe> writer) {
        var allMeshes = List.of(EItems.STRING_MESH, EItems.FLINT_MESH, EItems.IRON_MESH, EItems.GOLDEN_MESH, EItems.DIAMOND_MESH, EItems.NETHERITE_MESH);

        // Dirt -> String mesh
        forMesh(writer, DIRT, EItems.STRING_MESH, drops -> {
            drops.add(EItems.STONE_PEBBLE.get(), binomial(7, 0.6f));
            drops.add(Items.FLINT, chance(0.25f));
            drops.add(Items.WHEAT_SEEDS, chance(0.125f));
            drops.add(Items.MELON_SEEDS, chance(0.1f));
            drops.add(Items.PUMPKIN_SEEDS, chance(0.1f));
            drops.add(Items.BEETROOT_SEEDS, chance(0.1f));
            drops.add(Items.POTATO, chance(0.1f));
            drops.add(Items.CARROT, chance(0.1f));
            drops.add(EItems.GRASS_SEEDS.get(), chance(0.1f));
            drops.add(EItems.MYCELIUM_SPORES.get(), chance(0.03f));
            drops.add(Items.SUGAR_CANE, chance(0.1f));
            drops.add(Items.POISONOUS_POTATO, chance(0.05f));
            drops.add(Items.BAMBOO, chance(0.04f));
        });
        // Flint mesh will be used to get a larger variety of outputs from dirt, just so people don't always
        // have the inventory spam that are the -ite pebbles.
        // Dirt -> Flint mesh
        forMesh(writer, DIRT, EItems.FLINT_MESH, drops -> {
            drops.add(EItems.STONE_PEBBLE.get(), binomial(7, 0.6f));
            drops.add(Items.FLINT, chance(0.3f));
            drops.add(EItems.ANDESITE_PEBBLE.get(), binomial(7, 0.4f));
            drops.add(EItems.GRANITE_PEBBLE.get(), binomial(7, 0.4f));
            drops.add(EItems.DIORITE_PEBBLE.get(), binomial(7, 0.4f));
            drops.add(Items.WHEAT_SEEDS, chance(0.15f));
            drops.add(Items.MELON_SEEDS, chance(0.12f));
            drops.add(Items.PUMPKIN_SEEDS, chance(0.12f));
            drops.add(Items.POTATO, chance(0.13f));
            drops.add(Items.CARROT, chance(0.13f));
            drops.add(EItems.GRASS_SEEDS.get(), chance(0.15f));
            drops.add(EItems.MYCELIUM_SPORES.get(), chance(0.05f));
            drops.add(Items.SUGAR_CANE, chance(0.15f));
            drops.add(Items.POISONOUS_POTATO, chance(0.03f));
            drops.add(Items.BAMBOO, chance(0.04f));
            drops.add(Items.PINK_PETALS, chance(0.03f));
            drops.add(Items.SWEET_BERRIES, chance(0.05f));
            drops.addConditional(ModCompatData.SOURCEBERRY.get(), chance(0.03f), Recipes.modInstalled(ModIds.ARS_NOUVEAU));
        });
        // Dirt -> Iron mesh
        forMesh(writer, DIRT, EItems.IRON_MESH, drops -> {
            drops.add(EItems.STONE_PEBBLE.get(), binomial(8, 0.65f));
            drops.add(EItems.DEEPSLATE_PEBBLE.get(), binomial(3, 0.45f));
            drops.add(Items.FLINT, chance(0.3f));
            drops.add(Items.WHEAT_SEEDS, chance(0.175f));
            drops.add(Items.MELON_SEEDS, chance(0.15f));
            drops.add(Items.PUMPKIN_SEEDS, chance(0.15f));
            drops.add(Items.POTATO, chance(0.15f));
            drops.add(Items.CARROT, chance(0.15f));
            drops.add(EItems.GRASS_SEEDS.get(), chance(0.175f));
            drops.add(EItems.MYCELIUM_SPORES.get(), chance(0.1f));
            drops.add(Items.SUGAR_CANE, chance(0.15f));
            drops.add(Items.IRON_NUGGET, chance(0.05f));
            drops.add(Items.BAMBOO, chance(0.06f));
        });
        // Gold tends to spread its luster to whatever passes through it...
        // Dirt -> Gold mesh
        forMesh(writer, DIRT, EItems.GOLDEN_MESH, drops -> {
            drops.add(EItems.STONE_PEBBLE.get(), binomial(8, 0.7f));
            drops.add(EItems.DEEPSLATE_PEBBLE.get(), binomial(3, 0.55f));
            drops.add(Items.FLINT, chance(0.2f));
            drops.add(Items.WHEAT_SEEDS, chance(0.2f));
            drops.add(Items.MELON_SEEDS, chance(0.165f));
            drops.add(Items.PUMPKIN_SEEDS, chance(0.165f));
            drops.add(Items.POTATO, chance(0.175f));
            drops.add(Items.CARROT, chance(0.175f));
            drops.add(EItems.GRASS_SEEDS.get(), chance(0.25f));
            drops.add(EItems.MYCELIUM_SPORES.get(), chance(0.13f));
            drops.add(Items.GOLD_NUGGET, chance(0.05f));
            drops.add(Items.IRON_NUGGET, chance(0.05f));
            drops.add(Items.GOLDEN_CARROT, chance(0.02f));
            drops.add(Items.BAMBOO, chance(0.05f));
        });
        // Diamond tables have less junk items in them. Maybe you want those items? Use other meshes!
        // Dirt -> Diamond mesh
        forMesh(writer, DIRT, EItems.DIAMOND_MESH, drops -> {
            drops.add(EItems.STONE_PEBBLE.get(), binomial(8, 0.7f));
            drops.add(EItems.DEEPSLATE_PEBBLE.get(), binomial(3, 0.60f));
            drops.add(Items.FLINT, binomial(3, 0.3f));
            drops.add(Items.POTATO, chance(0.25f));
            drops.add(Items.CARROT, chance(0.25f));
            drops.add(EItems.GRASS_SEEDS.get(), chance(0.15f));
            drops.add(EItems.MYCELIUM_SPORES.get(), chance(0.1f));
            drops.add(Items.BAMBOO, chance(0.06f));
        });
        // Netherite should be the best for all drops (except pebbles)
        // Dirt -> Netherite mesh
        forMesh(writer, DIRT, EItems.NETHERITE_MESH, drops -> {
            drops.add(EItems.STONE_PEBBLE.get(), binomial(5, 0.4f));
            drops.add(EItems.DEEPSLATE_PEBBLE.get(), binomial(4, 0.65f));
            drops.add(Items.FLINT, binomial(3, 0.4f));
            drops.add(Items.POTATO, chance(0.3f));
            drops.add(Items.CARROT, chance(0.3f));
            drops.add(EItems.GRASS_SEEDS.get(), chance(0.2f));
            drops.add(EItems.MYCELIUM_SPORES.get(), chance(0.2f));
            drops.add(Items.GOLDEN_CARROT, chance(0.01f));
            drops.add(Items.GOLDEN_APPLE, chance(0.0025f));
            drops.add(Items.BAMBOO, chance(0.06f));
        });

        // Gravel -> String mesh
        forMesh(writer, GRAVEL, EItems.STRING_MESH, drops -> {
            drops.add(EItems.STONE_PEBBLE.get(), binomial(4, 0.4f));
            drops.add(Items.FLINT, chance(0.2f));
            drops.add(Items.COAL, chance(0.1f));
            drops.add(Items.LAPIS_LAZULI, chance(0.03f));
            drops.add(EItems.COPPER_ORE_CHUNK.get(), chance(0.08f));
            drops.add(EItems.IRON_ORE_CHUNK.get(), chance(0.10f));
            drops.add(EItems.GOLD_ORE_CHUNK.get(), chance(0.03f));
            drops.add(Items.DIAMOND, chance(0.02f));
            drops.add(Items.EMERALD, chance(0.01f));
            drops.add(Items.AMETHYST_SHARD, chance(0.01f));

            drops.addConditional(EItems.ALUMINUM_ORE_CHUNK.get(), chance(0.04f), Recipes.tagNotEmpty(EItemTags.ORES_ALUMINUM));
            drops.addConditional(EItems.SILVER_ORE_CHUNK.get(), chance(0.04f), Recipes.tagNotEmpty(EItemTags.ORES_SILVER));
            drops.addConditional(EItems.LEAD_ORE_CHUNK.get(), chance(0.04f), Recipes.tagNotEmpty(EItemTags.ORES_LEAD));
            drops.addConditional(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.03f), Recipes.tagNotEmpty(EItemTags.ORES_OSMIUM));
            drops.addConditional(EItems.NICKEL_ORE_CHUNK.get(), chance(0.04f), Recipes.tagNotEmpty(EItemTags.ORES_NICKEL));
            drops.addConditional(EItems.TIN_ORE_CHUNK.get(), chance(0.06f), Recipes.tagNotEmpty(EItemTags.ORES_TIN));
            drops.addConditional(EItems.ZINC_ORE_CHUNK.get(), chance(0.035f), Recipes.tagNotEmpty(EItemTags.ORES_ZINC));
            drops.addConditional(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.03f), Recipes.tagNotEmpty(EItemTags.ORES_IRIDIUM));
            drops.addConditional(EItems.MAGNESIUM_ORE_CHUNK.get(), chance(0.07f), Recipes.tagNotEmpty(EItemTags.ORES_MAGNESIUM));
            drops.addConditional(EItems.THORIUM_ORE_CHUNK.get(), chance(0.05f), Recipes.tagNotEmpty(EItemTags.ORES_THORIUM));
            drops.addConditional(EItems.BORON_ORE_CHUNK.get(), chance(0.03f), Recipes.tagNotEmpty(EItemTags.ORES_BORON));
        });
        // Gravel -> Flint mesh
        forMesh(writer, GRAVEL, EItems.FLINT_MESH, drops -> {
            drops.add(EItems.STONE_PEBBLE.get(), binomial(4, 0.5f));
            drops.add(EItems.ANDESITE_PEBBLE.get(), binomial(4, 0.4f));
            drops.add(EItems.GRANITE_PEBBLE.get(), binomial(4, 0.4f));
            drops.add(EItems.DIORITE_PEBBLE.get(), binomial(4, 0.4f));
            drops.add(Items.POINTED_DRIPSTONE, chance(0.15f));
            drops.add(Items.FLINT, chance(0.25f));
            drops.add(Items.COAL, chance(0.125f));
            drops.add(Items.LAPIS_LAZULI, chance(0.05f));
            drops.add(EItems.COPPER_ORE_CHUNK.get(), chance(0.1f));
            drops.add(EItems.IRON_ORE_CHUNK.get(), chance(0.12f));
            drops.add(EItems.GOLD_ORE_CHUNK.get(), chance(0.04f));
            drops.add(Items.DIAMOND, chance(0.03f));
            drops.add(Items.EMERALD, chance(0.015f));
            drops.add(Items.AMETHYST_SHARD, chance(0.015f));

            drops.addConditional(EItems.ALUMINUM_ORE_CHUNK.get(), chance(0.05f), Recipes.tagNotEmpty(EItemTags.ORES_ALUMINUM));
            drops.addConditional(EItems.SILVER_ORE_CHUNK.get(), chance(0.05f), Recipes.tagNotEmpty(EItemTags.ORES_SILVER));
            drops.addConditional(EItems.LEAD_ORE_CHUNK.get(), chance(0.05f), Recipes.tagNotEmpty(EItemTags.ORES_LEAD));
            drops.addConditional(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.04f), Recipes.tagNotEmpty(EItemTags.ORES_OSMIUM));
            drops.addConditional(EItems.NICKEL_ORE_CHUNK.get(), chance(0.055f), Recipes.tagNotEmpty(EItemTags.ORES_NICKEL));
            drops.addConditional(EItems.TIN_ORE_CHUNK.get(), chance(0.07f), Recipes.tagNotEmpty(EItemTags.ORES_TIN));
            drops.addConditional(EItems.ZINC_ORE_CHUNK.get(), chance(0.04f), Recipes.tagNotEmpty(EItemTags.ORES_ZINC));
            drops.addConditional(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.03f), Recipes.tagNotEmpty(EItemTags.ORES_IRIDIUM));
            drops.addConditional(EItems.MAGNESIUM_ORE_CHUNK.get(), chance(0.075f), Recipes.tagNotEmpty(EItemTags.ORES_MAGNESIUM));
            drops.addConditional(EItems.THORIUM_ORE_CHUNK.get(), chance(0.055f), Recipes.tagNotEmpty(EItemTags.ORES_THORIUM));
            drops.addConditional(EItems.BORON_ORE_CHUNK.get(), chance(0.0325f), Recipes.tagNotEmpty(EItemTags.ORES_BORON));
        });
        // Gravel -> Iron mesh
        forMesh(writer, GRAVEL, EItems.IRON_MESH, drops -> {
            drops.add(EItems.STONE_PEBBLE.get(), binomial(4, 0.5f));
            drops.add(EItems.DEEPSLATE_PEBBLE.get(), binomial(3, 0.55f));
            drops.add(Items.FLINT, chance(0.15f));
            drops.add(Items.COAL, chance(0.15f));
            drops.add(Items.LAPIS_LAZULI, chance(0.08f));
            drops.add(EItems.COPPER_ORE_CHUNK.get(), chance(0.12f));
            drops.add(EItems.IRON_ORE_CHUNK.get(), chance(0.14f));
            drops.add(EItems.GOLD_ORE_CHUNK.get(), chance(0.06f));
            drops.add(Items.DIAMOND, chance(0.05f));
            drops.add(Items.EMERALD, chance(0.04f));
            drops.add(Items.AMETHYST_SHARD, chance(0.04f));

            drops.addConditional(EItems.ALUMINUM_ORE_CHUNK.get(), chance(0.06f), Recipes.tagNotEmpty(EItemTags.ORES_ALUMINUM));
            drops.addConditional(EItems.SILVER_ORE_CHUNK.get(), chance(0.055f), Recipes.tagNotEmpty(EItemTags.ORES_SILVER));
            drops.addConditional(EItems.LEAD_ORE_CHUNK.get(), chance(0.06f), Recipes.tagNotEmpty(EItemTags.ORES_LEAD));
            drops.addConditional(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.045f), Recipes.tagNotEmpty(EItemTags.ORES_OSMIUM));
            drops.addConditional(EItems.NICKEL_ORE_CHUNK.get(), chance(0.07f), Recipes.tagNotEmpty(EItemTags.ORES_NICKEL));
            drops.addConditional(EItems.TIN_ORE_CHUNK.get(), chance(0.09f), Recipes.tagNotEmpty(EItemTags.ORES_TIN));
            drops.addConditional(EItems.ZINC_ORE_CHUNK.get(), chance(0.06f), Recipes.tagNotEmpty(EItemTags.ORES_ZINC));
            drops.addConditional(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.04f), Recipes.tagNotEmpty(EItemTags.ORES_IRIDIUM));
            drops.addConditional(EItems.MAGNESIUM_ORE_CHUNK.get(), chance(0.09f), Recipes.tagNotEmpty(EItemTags.ORES_MAGNESIUM));
            drops.addConditional(EItems.THORIUM_ORE_CHUNK.get(), chance(0.065f), Recipes.tagNotEmpty(EItemTags.ORES_THORIUM));
            drops.addConditional(EItems.BORON_ORE_CHUNK.get(), chance(0.045f), Recipes.tagNotEmpty(EItemTags.ORES_BORON));
        });
        // Golden mesh has much higher drops for gold and gems
        // Gravel -> Golden mesh
        forMesh(writer, GRAVEL, EItems.GOLDEN_MESH, drops -> {
            drops.add(EItems.STONE_PEBBLE.get(), binomial(4, 0.5f));
            drops.add(EItems.DEEPSLATE_PEBBLE.get(), binomial(3, 0.55f));
            drops.add(Items.FLINT, chance(0.13f));
            drops.add(Items.COAL, chance(0.2f));
            drops.add(Items.LAPIS_LAZULI, chance(0.1f));
            drops.add(EItems.COPPER_ORE_CHUNK.get(), chance(0.07f));
            drops.add(EItems.IRON_ORE_CHUNK.get(), chance(0.14f));
            drops.add(EItems.GOLD_ORE_CHUNK.get(), chance(0.1f));
            drops.add(Items.DIAMOND, chance(0.09f));
            drops.add(Items.EMERALD, chance(0.09f));
            drops.add(Items.AMETHYST_SHARD, chance(0.08f));
            drops.add(Items.GOLD_NUGGET, chance(0.08f));
            drops.add(Items.RAW_GOLD, chance(0.02f));

            drops.addConditional(EItems.ALUMINUM_ORE_CHUNK.get(), chance(0.07f), Recipes.tagNotEmpty(EItemTags.ORES_ALUMINUM));
            drops.addConditional(EItems.SILVER_ORE_CHUNK.get(), chance(0.12f), Recipes.tagNotEmpty(EItemTags.ORES_SILVER));
            drops.addConditional(EItems.LEAD_ORE_CHUNK.get(), chance(0.07f), Recipes.tagNotEmpty(EItemTags.ORES_LEAD));
            drops.addConditional(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.05f), Recipes.tagNotEmpty(EItemTags.ORES_OSMIUM));
            drops.addConditional(EItems.NICKEL_ORE_CHUNK.get(), chance(0.07f), Recipes.tagNotEmpty(EItemTags.ORES_NICKEL));
            drops.addConditional(EItems.TIN_ORE_CHUNK.get(), chance(0.07f), Recipes.tagNotEmpty(EItemTags.ORES_TIN));
            drops.addConditional(EItems.ZINC_ORE_CHUNK.get(), chance(0.05f), Recipes.tagNotEmpty(EItemTags.ORES_ZINC));
            drops.addConditional(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.05f), Recipes.tagNotEmpty(EItemTags.ORES_IRIDIUM));
            drops.addConditional(EItems.MAGNESIUM_ORE_CHUNK.get(), chance(0.04f), Recipes.tagNotEmpty(EItemTags.ORES_MAGNESIUM));
            drops.addConditional(EItems.THORIUM_ORE_CHUNK.get(), chance(0.04f), Recipes.tagNotEmpty(EItemTags.ORES_THORIUM));
            drops.addConditional(EItems.BORON_ORE_CHUNK.get(), chance(0.04f), Recipes.tagNotEmpty(EItemTags.ORES_BORON));
        });
        // Gravel -> Diamond mesh
        forMesh(writer, GRAVEL, EItems.DIAMOND_MESH, drops -> {
            drops.add(EItems.DEEPSLATE_PEBBLE.get(), binomial(5, 0.6f));
            drops.add(Items.FLINT, chance(0.05f));
            drops.add(Items.COAL, chance(0.06f));
            drops.add(Items.LAPIS_LAZULI, chance(0.11f));
            drops.add(EItems.COPPER_ORE_CHUNK.get(), chance(0.07f));
            drops.add(EItems.IRON_ORE_CHUNK.get(), chance(0.15f));
            drops.add(EItems.GOLD_ORE_CHUNK.get(), chance(0.08f));
            drops.add(Items.DIAMOND, chance(0.08f));
            drops.add(Items.EMERALD, chance(0.07f));
            drops.add(Items.AMETHYST_SHARD, chance(0.06f));

            drops.addConditional(EItems.ALUMINUM_ORE_CHUNK.get(), chance(0.08f), Recipes.tagNotEmpty(EItemTags.ORES_ALUMINUM));
            drops.addConditional(EItems.SILVER_ORE_CHUNK.get(), chance(0.08f), Recipes.tagNotEmpty(EItemTags.ORES_SILVER));
            drops.addConditional(EItems.LEAD_ORE_CHUNK.get(), chance(0.09f), Recipes.tagNotEmpty(EItemTags.ORES_LEAD));
            drops.addConditional(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.07f), Recipes.tagNotEmpty(EItemTags.ORES_OSMIUM));
            drops.addConditional(EItems.NICKEL_ORE_CHUNK.get(), chance(0.09f), Recipes.tagNotEmpty(EItemTags.ORES_NICKEL));
            drops.addConditional(EItems.TIN_ORE_CHUNK.get(), chance(0.11f), Recipes.tagNotEmpty(EItemTags.ORES_TIN));
            drops.addConditional(EItems.ZINC_ORE_CHUNK.get(), chance(0.08f), Recipes.tagNotEmpty(EItemTags.ORES_ZINC));
            drops.addConditional(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.05f), Recipes.tagNotEmpty(EItemTags.ORES_IRIDIUM));
            drops.addConditional(EItems.MAGNESIUM_ORE_CHUNK.get(), chance(0.105f), Recipes.tagNotEmpty(EItemTags.ORES_MAGNESIUM));
            drops.addConditional(EItems.THORIUM_ORE_CHUNK.get(), chance(0.07f), Recipes.tagNotEmpty(EItemTags.ORES_THORIUM));
            drops.addConditional(EItems.BORON_ORE_CHUNK.get(), chance(0.06f), Recipes.tagNotEmpty(EItemTags.ORES_BORON));
        });
        // Gravel -> Netherite mesh
        forMesh(writer, GRAVEL, EItems.NETHERITE_MESH, drops -> {
            drops.add(EItems.DEEPSLATE_PEBBLE.get(), binomial(6, 0.625f));
            drops.add(Items.COAL, chance(0.06f));
            drops.add(Items.LAPIS_LAZULI, chance(0.11f));
            drops.add(EItems.COPPER_ORE_CHUNK.get(), chance(0.1f));
            drops.add(EItems.IRON_ORE_CHUNK.get(), chance(0.17f));
            drops.add(EItems.GOLD_ORE_CHUNK.get(), chance(0.09f));
            drops.add(Items.DIAMOND, chance(0.1f));
            drops.add(Items.EMERALD, chance(0.09f));
            drops.add(Items.AMETHYST_SHARD, chance(0.08f));
            drops.add(Items.GOLD_NUGGET, chance(0.04f));
            drops.add(Items.RAW_GOLD, chance(0.01f));

            drops.addConditional(EItems.ALUMINUM_ORE_CHUNK.get(), chance(0.09f), Recipes.tagNotEmpty(EItemTags.ORES_ALUMINUM));
            drops.addConditional(EItems.SILVER_ORE_CHUNK.get(), chance(0.11f), Recipes.tagNotEmpty(EItemTags.ORES_SILVER));
            drops.addConditional(EItems.LEAD_ORE_CHUNK.get(), chance(0.11f), Recipes.tagNotEmpty(EItemTags.ORES_LEAD));
            drops.addConditional(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.09f), Recipes.tagNotEmpty(EItemTags.ORES_OSMIUM));
            drops.addConditional(EItems.NICKEL_ORE_CHUNK.get(), chance(0.10f), Recipes.tagNotEmpty(EItemTags.ORES_NICKEL));
            drops.addConditional(EItems.TIN_ORE_CHUNK.get(), chance(0.12f), Recipes.tagNotEmpty(EItemTags.ORES_TIN));
            drops.addConditional(EItems.ZINC_ORE_CHUNK.get(), chance(0.08f), Recipes.tagNotEmpty(EItemTags.ORES_ZINC));
            drops.addConditional(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.055f), Recipes.tagNotEmpty(EItemTags.ORES_IRIDIUM));
            drops.addConditional(EItems.MAGNESIUM_ORE_CHUNK.get(), chance(0.12f), Recipes.tagNotEmpty(EItemTags.ORES_MAGNESIUM));
            drops.addConditional(EItems.THORIUM_ORE_CHUNK.get(), chance(0.075f), Recipes.tagNotEmpty(EItemTags.ORES_THORIUM));
            drops.addConditional(EItems.BORON_ORE_CHUNK.get(), chance(0.065f), Recipes.tagNotEmpty(EItemTags.ORES_BORON));
        });

        // Sand -> String mesh
        forMesh(writer, SAND, EItems.STRING_MESH, drops -> {
            drops.add(Items.CACTUS, chance(0.13f));
            drops.add(Items.FLINT, chance(0.2f));
            drops.add(Items.DEAD_BUSH, chance(0.08f));
            drops.add(Items.GOLD_NUGGET, chance(0.13f));
            drops.add(Items.IRON_NUGGET, chance(0.13f));
            drops.add(Items.KELP, chance(0.1f));
            drops.add(Items.SEA_PICKLE, chance(0.05f));

            drops.addConditional(ModCompatData.CERTUS_QUARTZ_CRYSTAL.get(), chance(0.03f), Recipes.modInstalled(ModIds.APPLIED_ENERGISTICS_2));
            drops.addConditional(ModCompatData.CHARGED_CERTUS_QUARTZ_CRYSTAL.get(), chance(0.005f), Recipes.modInstalled(ModIds.APPLIED_ENERGISTICS_2));
        });
        forMesh(writer, SAND, EItems.FLINT_MESH, drops -> {
            drops.add(Items.FLINT, binomial(2, 0.2f));
            drops.add(Items.DEAD_BUSH, chance(0.03f));
            drops.add(Items.GOLD_NUGGET, chance(0.16f));
            drops.add(Items.IRON_NUGGET, chance(0.16f));
            drops.add(EItems.RANDOM_POTTERY_SHERD.get(), chance(0.04f));

            drops.addConditional(ModCompatData.CERTUS_QUARTZ_CRYSTAL.get(), chance(0.04f), Recipes.modInstalled(ModIds.APPLIED_ENERGISTICS_2));
            drops.addConditional(ModCompatData.CHARGED_CERTUS_QUARTZ_CRYSTAL.get(), chance(0.005f), Recipes.modInstalled(ModIds.APPLIED_ENERGISTICS_2));
        });
        forMesh(writer, SAND, EItems.IRON_MESH, drops -> {
            drops.add(Items.CACTUS, chance(0.13f));
            drops.add(Items.FLINT, chance(0.23f));
            drops.add(Items.DEAD_BUSH, chance(0.08f));
            drops.add(Items.GOLD_NUGGET, chance(0.18f));
            drops.add(Items.IRON_NUGGET, chance(0.18f));
            drops.add(Items.KELP, chance(0.07f));
            drops.add(Items.SEA_PICKLE, chance(0.03f));
            drops.add(Items.PRISMARINE_SHARD, chance(0.06f));
            drops.add(Items.PRISMARINE_CRYSTALS, chance(0.06f));

            drops.addConditional(ModCompatData.CERTUS_QUARTZ_CRYSTAL.get(), chance(0.06f), Recipes.modInstalled(ModIds.APPLIED_ENERGISTICS_2));
            drops.addConditional(ModCompatData.CHARGED_CERTUS_QUARTZ_CRYSTAL.get(), chance(0.0125f), Recipes.modInstalled(ModIds.APPLIED_ENERGISTICS_2));
        });
        forMesh(writer, SAND, EItems.GOLDEN_MESH, drops -> {
            drops.add(Items.CACTUS, chance(0.10f));
            drops.add(Items.FLINT, chance(0.18f));
            drops.add(Items.DEAD_BUSH, chance(0.06f));
            drops.add(Items.GOLD_NUGGET, binomial(3, 0.28f));
            drops.add(Items.IRON_NUGGET, chance(0.16f));
            drops.add(Items.KELP, chance(0.05f));
            drops.add(Items.SEA_PICKLE, chance(0.03f));
            drops.add(Items.PRISMARINE_SHARD, chance(0.08f));
            drops.add(Items.PRISMARINE_CRYSTALS, chance(0.08f));
            drops.add(Items.RAW_GOLD, chance(0.04f));
            drops.add(EItems.RANDOM_ARMOR_TRIM.get(), chance(0.02f));

            drops.addConditional(ModCompatData.CERTUS_QUARTZ_CRYSTAL.get(), chance(0.07f), Recipes.modInstalled(ModIds.APPLIED_ENERGISTICS_2));
            drops.addConditional(ModCompatData.CHARGED_CERTUS_QUARTZ_CRYSTAL.get(), chance(0.015f), Recipes.modInstalled(ModIds.APPLIED_ENERGISTICS_2));
        });
        forMesh(writer, SAND, EItems.DIAMOND_MESH, drops -> {
            drops.add(Items.FLINT, chance(0.23f));
            drops.add(Items.GOLD_NUGGET, chance(0.22f));
            drops.add(Items.IRON_NUGGET, chance(0.22f));
            drops.add(Items.PRISMARINE_SHARD, chance(0.09f));
            drops.add(Items.PRISMARINE_CRYSTALS, chance(0.09f));

            drops.addConditional(ModCompatData.CERTUS_QUARTZ_CRYSTAL.get(), chance(0.09f), Recipes.modInstalled(ModIds.APPLIED_ENERGISTICS_2));
            drops.addConditional(ModCompatData.CHARGED_CERTUS_QUARTZ_CRYSTAL.get(), chance(0.02f), Recipes.modInstalled(ModIds.APPLIED_ENERGISTICS_2));
        });
        forMesh(writer, SAND, EItems.NETHERITE_MESH, drops -> {
            drops.add(Items.CACTUS, chance(0.15f));
            drops.add(Items.FLINT, binomial(2, 0.23f));
            drops.add(Items.GOLD_NUGGET, chance(0.23f));
            drops.add(Items.IRON_NUGGET, chance(0.23f));
            drops.add(Items.KELP, chance(0.1f));
            drops.add(Items.SEA_PICKLE, chance(0.07f));
            drops.add(Items.PRISMARINE_SHARD, chance(0.12f));
            drops.add(Items.PRISMARINE_CRYSTALS, chance(0.12f));

            drops.addConditional(ModCompatData.CERTUS_QUARTZ_CRYSTAL.get(), chance(0.095f), Recipes.modInstalled(ModIds.APPLIED_ENERGISTICS_2));
            drops.addConditional(ModCompatData.CHARGED_CERTUS_QUARTZ_CRYSTAL.get(), chance(0.035f), Recipes.modInstalled(ModIds.APPLIED_ENERGISTICS_2));
        });

        // Red Sand -> String mesh
        forMesh(writer, RED_SAND, EItems.STRING_MESH, drops -> {
            drops.add(Items.CACTUS, chance(0.12f));
            drops.add(Items.DEAD_BUSH, chance(0.07f));
            drops.add(Items.GOLD_NUGGET, chance(0.09f));
            drops.add(Items.REDSTONE, chance(0.08f));
            drops.add(Items.RAW_GOLD, chance(0.03f));
        });
        forMesh(writer, RED_SAND, EItems.FLINT_MESH, drops -> {
            drops.add(Items.CACTUS, chance(0.12f));
            drops.add(Items.DEAD_BUSH, chance(0.07f));
            drops.add(Items.GOLD_NUGGET, chance(0.12f));
            drops.add(Items.REDSTONE, chance(0.09f));
            drops.add(Items.RAW_GOLD, chance(0.04f));
        });
        forMesh(writer, RED_SAND, EItems.IRON_MESH, drops -> {
            drops.add(Items.CACTUS, chance(0.12f));
            drops.add(Items.DEAD_BUSH, chance(0.07f));
            drops.add(Items.GOLD_NUGGET, chance(0.09f));
            drops.add(Items.REDSTONE, chance(0.11f));
            drops.add(Items.RAW_GOLD, chance(0.06f));
        });
        forMesh(writer, RED_SAND, EItems.GOLDEN_MESH, drops -> {
            drops.add(Items.CACTUS, chance(0.12f));
            drops.add(Items.DEAD_BUSH, chance(0.07f));
            drops.add(Items.GOLD_NUGGET, chance(0.19f));
            drops.add(Items.REDSTONE, chance(0.07f));
            drops.add(Items.RAW_GOLD, chance(0.11f));
        });
        forMesh(writer, RED_SAND, EItems.DIAMOND_MESH, drops -> {
            drops.add(Items.CACTUS, chance(0.10f));
            drops.add(Items.DEAD_BUSH, chance(0.03f));
            drops.add(Items.GOLD_NUGGET, chance(0.14f));
            drops.add(Items.REDSTONE, chance(0.14f));
            drops.add(Items.RAW_GOLD, chance(0.08f));
        });
        forMesh(writer, RED_SAND, EItems.NETHERITE_MESH, drops -> {
            drops.add(Items.CACTUS, chance(0.12f));
            drops.add(Items.GOLD_NUGGET, chance(0.15f));
            drops.add(Items.REDSTONE, chance(0.17f));
            drops.add(Items.RAW_GOLD, chance(0.10f));
        });

        forMesh(writer, DUST, EItems.STRING_MESH, drops -> {
            drops.add(Items.GUNPOWDER, chance(0.1f));
            drops.add(Items.BONE_MEAL, chance(0.1f));
            drops.add(Items.REDSTONE, chance(0.06f));
            drops.add(Items.GLOWSTONE_DUST, chance(0.04f));
            drops.add(Items.BLAZE_POWDER, chance(0.03f));

            drops.addConditional(ModCompatData.GRAINS_OF_INFINITY.get(), chance(0.06f), Recipes.ENDERIO);
            drops.addConditional(ModCompatData.YELLORIUM_DUST.get(), chance(0.05f), Recipes.EXTREME_REACTORS);
            drops.addConditional(ModCompatData.SKY_STONE_DUST.get(), chance(0.06f), Recipes.AE2);
            drops.addConditional(ModCompatData.CERTUS_QUARTZ_DUST.get(), chance(0.06f), Recipes.AE2);
        });
        forMesh(writer, DUST, EItems.FLINT_MESH, drops -> {
            drops.add(Items.GUNPOWDER, chance(0.11f));
            drops.add(Items.BONE_MEAL, chance(0.11f));
            drops.add(Items.REDSTONE, chance(0.09f));
            drops.add(Items.GLOWSTONE_DUST, chance(0.07f));
            drops.add(Items.BLAZE_POWDER, chance(0.04f));

            drops.addConditional(ModCompatData.GRAINS_OF_INFINITY.get(), chance(0.07f), Recipes.ENDERIO);
            drops.addConditional(ModCompatData.YELLORIUM_DUST.get(), chance(0.055f), Recipes.EXTREME_REACTORS);
            drops.addConditional(ModCompatData.SKY_STONE_DUST.get(), chance(0.07f), Recipes.AE2);
            drops.addConditional(ModCompatData.CERTUS_QUARTZ_DUST.get(), chance(0.07f), Recipes.AE2);
        });
        forMesh(writer, DUST, EItems.IRON_MESH, drops -> {
            drops.add(Items.GUNPOWDER, chance(0.13f));
            drops.add(Items.BONE_MEAL, chance(0.12f));
            drops.add(Items.REDSTONE, chance(0.1f));
            drops.add(Items.GLOWSTONE_DUST, chance(0.09f));
            drops.add(Items.BLAZE_POWDER, chance(0.05f));
            drops.add(Items.IRON_NUGGET, chance(0.06f));

            drops.addConditional(ModCompatData.GRAINS_OF_INFINITY.get(), chance(0.09f), Recipes.ENDERIO);
            drops.addConditional(ModCompatData.YELLORIUM_DUST.get(), chance(0.08f), Recipes.EXTREME_REACTORS);
            drops.addConditional(ModCompatData.SKY_STONE_DUST.get(), chance(0.075f), Recipes.AE2);
            drops.addConditional(ModCompatData.CERTUS_QUARTZ_DUST.get(), chance(0.075f), Recipes.AE2);
        });
        forMesh(writer, DUST, EItems.GOLDEN_MESH, drops -> {
            drops.add(Items.GUNPOWDER, chance(0.13f));
            drops.add(Items.BONE_MEAL, chance(0.11f));
            drops.add(Items.REDSTONE, chance(0.12f));
            drops.add(Items.GLOWSTONE_DUST, chance(0.11f));
            drops.add(Items.BLAZE_POWDER, chance(0.06f));
            drops.add(Items.GOLD_NUGGET, binomial(2, 0.18f));
            drops.add(Items.RAW_GOLD, chance(0.02f));

            drops.addConditional(ModCompatData.GRAINS_OF_INFINITY.get(), chance(0.11f), Recipes.ENDERIO);
            drops.addConditional(ModCompatData.YELLORIUM_DUST.get(), chance(0.10f), Recipes.EXTREME_REACTORS);
            drops.addConditional(ModCompatData.SKY_STONE_DUST.get(), chance(0.08f), Recipes.AE2);
            drops.addConditional(ModCompatData.CERTUS_QUARTZ_DUST.get(), chance(0.08f), Recipes.AE2);
        });
        forMesh(writer, DUST, EItems.DIAMOND_MESH, drops -> {
            drops.add(Items.GUNPOWDER, chance(0.14f));
            drops.add(Items.BONE_MEAL, chance(0.10f));
            drops.add(Items.REDSTONE, chance(0.12f));
            drops.add(Items.GLOWSTONE_DUST, chance(0.11f));
            drops.add(Items.BLAZE_POWDER, chance(0.06f));
            drops.add(Items.GOLD_NUGGET, chance(0.08f));

            drops.addConditional(ModCompatData.GRAINS_OF_INFINITY.get(), chance(0.12f), Recipes.ENDERIO);
            drops.addConditional(ModCompatData.YELLORIUM_DUST.get(), chance(0.12f), Recipes.EXTREME_REACTORS);
            drops.addConditional(ModCompatData.SKY_STONE_DUST.get(), chance(0.10f), Recipes.AE2);
            drops.addConditional(ModCompatData.CERTUS_QUARTZ_DUST.get(), chance(0.10f), Recipes.AE2);
        });
        forMesh(writer, DUST, EItems.NETHERITE_MESH, drops -> {
            drops.add(Items.GUNPOWDER, chance(0.14f));
            drops.add(Items.BONE_MEAL, chance(0.13f));
            drops.add(Items.REDSTONE, chance(0.14f));
            drops.add(Items.GLOWSTONE_DUST, chance(0.15f));
            drops.add(Items.BLAZE_POWDER, chance(0.1f));
            drops.add(Items.GOLD_NUGGET, chance(0.08f));
            drops.add(Items.IRON_NUGGET, chance(0.08f));

            drops.addConditional(ModCompatData.GRAINS_OF_INFINITY.get(), chance(0.135f), Recipes.ENDERIO);
            drops.addConditional(ModCompatData.YELLORIUM_DUST.get(), chance(0.14f), Recipes.EXTREME_REACTORS);
            drops.addConditional(ModCompatData.SKY_STONE_DUST.get(), chance(0.11f), Recipes.AE2);
            drops.addConditional(ModCompatData.CERTUS_QUARTZ_DUST.get(), chance(0.11f), Recipes.AE2);
        });

        // Crushed Deepslate -> String mesh
        forMesh(writer, CRUSHED_DEEPSLATE, EItems.STRING_MESH, drops -> {
            drops.add(EItems.DEEPSLATE_PEBBLE.get(), binomial(4, 0.5f));
            drops.add(EItems.COPPER_ORE_CHUNK.get(), chance(0.12f));
            drops.add(EItems.IRON_ORE_CHUNK.get(), chance(0.12f));
            drops.add(EItems.GOLD_ORE_CHUNK.get(), chance(0.08f));
            drops.add(Items.AMETHYST_SHARD, chance(0.05f));
            drops.add(Items.DIAMOND, chance(0.04f));
            drops.add(Items.LAPIS_LAZULI, chance(0.04f));
            drops.add(Items.EMERALD, chance(0.03f));

            drops.addConditional(EItems.SILVER_ORE_CHUNK.get(), chance(0.05f), Recipes.tagNotEmpty(EItemTags.ORES_SILVER));
            drops.addConditional(EItems.LEAD_ORE_CHUNK.get(), chance(0.05f), Recipes.tagNotEmpty(EItemTags.ORES_LEAD));
            drops.addConditional(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.06f), Recipes.tagNotEmpty(EItemTags.ORES_OSMIUM));
            drops.addConditional(EItems.NICKEL_ORE_CHUNK.get(), chance(0.04f), Recipes.tagNotEmpty(EItemTags.ORES_NICKEL));
            drops.addConditional(EItems.TIN_ORE_CHUNK.get(), chance(0.05f), Recipes.tagNotEmpty(EItemTags.ORES_TIN));
            drops.addConditional(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.04f), Recipes.tagNotEmpty(EItemTags.ORES_IRIDIUM));
            drops.addConditional(EItems.PLATINUM_ORE_CHUNK.get(), chance(0.03f), Recipes.tagNotEmpty(EItemTags.ORES_PLATINUM));
            drops.addConditional(EItems.URANIUM_ORE_CHUNK.get(), chance(0.04f), Recipes.tagNotEmpty(EItemTags.ORES_URANIUM));
            drops.addConditional(EItems.MAGNESIUM_ORE_CHUNK.get(), chance(0.09f), Recipes.tagNotEmpty(EItemTags.ORES_MAGNESIUM));
            drops.addConditional(EItems.THORIUM_ORE_CHUNK.get(), chance(0.06f), Recipes.tagNotEmpty(EItemTags.ORES_THORIUM));
            drops.addConditional(EItems.BORON_ORE_CHUNK.get(), chance(0.05f), Recipes.tagNotEmpty(EItemTags.ORES_BORON));
            drops.addConditional(EItems.LITHIUM_ORE_CHUNK.get(), chance(0.045f), Recipes.tagNotEmpty(EItemTags.ORES_LITHIUM));
        });
        forMesh(writer, CRUSHED_DEEPSLATE, EItems.FLINT_MESH, drops -> {
            drops.add(EItems.DEEPSLATE_PEBBLE.get(), binomial(4, 0.5f));
            drops.add(EItems.TUFF_PEBBLE.get(), binomial(4, 0.4f));
            drops.add(EItems.CALCITE_PEBBLE.get(), binomial(4, 0.4f));
            drops.add(EItems.BASALT_PEBBLE.get(), binomial(4, 0.4f));
            drops.add(EItems.COPPER_ORE_CHUNK.get(), chance(0.11f));
            drops.add(EItems.IRON_ORE_CHUNK.get(), chance(0.13f));
            drops.add(EItems.GOLD_ORE_CHUNK.get(), chance(0.08f));
            drops.add(Items.AMETHYST_SHARD, chance(0.06f));
            drops.add(Items.DIAMOND, chance(0.05f));
            drops.add(Items.LAPIS_LAZULI, chance(0.05f));
            drops.add(Items.EMERALD, chance(0.04f));

            drops.addConditional(EItems.SILVER_ORE_CHUNK.get(), chance(0.08f), Recipes.tagNotEmpty(EItemTags.ORES_SILVER));
            drops.addConditional(EItems.LEAD_ORE_CHUNK.get(), chance(0.08f), Recipes.tagNotEmpty(EItemTags.ORES_LEAD));
            drops.addConditional(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.08f), Recipes.tagNotEmpty(EItemTags.ORES_OSMIUM));
            drops.addConditional(EItems.NICKEL_ORE_CHUNK.get(), chance(0.06f), Recipes.tagNotEmpty(EItemTags.ORES_NICKEL));
            drops.addConditional(EItems.TIN_ORE_CHUNK.get(), chance(0.07f), Recipes.tagNotEmpty(EItemTags.ORES_TIN));
            drops.addConditional(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.05f), Recipes.tagNotEmpty(EItemTags.ORES_IRIDIUM));
            drops.addConditional(EItems.PLATINUM_ORE_CHUNK.get(), chance(0.05f), Recipes.tagNotEmpty(EItemTags.ORES_PLATINUM));
            drops.addConditional(EItems.URANIUM_ORE_CHUNK.get(), chance(0.06f), Recipes.tagNotEmpty(EItemTags.ORES_URANIUM));
            drops.addConditional(EItems.MAGNESIUM_ORE_CHUNK.get(), chance(0.10f), Recipes.tagNotEmpty(EItemTags.ORES_MAGNESIUM));
            drops.addConditional(EItems.THORIUM_ORE_CHUNK.get(), chance(0.065f), Recipes.tagNotEmpty(EItemTags.ORES_THORIUM));
            drops.addConditional(EItems.BORON_ORE_CHUNK.get(), chance(0.06f), Recipes.tagNotEmpty(EItemTags.ORES_BORON));
            drops.addConditional(EItems.LITHIUM_ORE_CHUNK.get(), chance(0.05f), Recipes.tagNotEmpty(EItemTags.ORES_LITHIUM));
        });
        forMesh(writer, CRUSHED_DEEPSLATE, EItems.IRON_MESH, drops -> {
            drops.add(EItems.DEEPSLATE_PEBBLE.get(), binomial(4, 0.6f));
            drops.add(EItems.COPPER_ORE_CHUNK.get(), chance(0.10f));
            drops.add(EItems.IRON_ORE_CHUNK.get(), chance(0.15f));
            drops.add(EItems.GOLD_ORE_CHUNK.get(), chance(0.09f));
            drops.add(Items.AMETHYST_SHARD, chance(0.06f));
            drops.add(Items.DIAMOND, chance(0.06f));
            drops.add(Items.LAPIS_LAZULI, chance(0.08f));
            drops.add(Items.EMERALD, chance(0.05f));

            drops.addConditional(EItems.SILVER_ORE_CHUNK.get(), chance(0.1f), Recipes.tagNotEmpty(EItemTags.ORES_SILVER));
            drops.addConditional(EItems.LEAD_ORE_CHUNK.get(), chance(0.1f), Recipes.tagNotEmpty(EItemTags.ORES_LEAD));
            drops.addConditional(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.10f), Recipes.tagNotEmpty(EItemTags.ORES_OSMIUM));
            drops.addConditional(EItems.NICKEL_ORE_CHUNK.get(), chance(0.09f), Recipes.tagNotEmpty(EItemTags.ORES_NICKEL));
            drops.addConditional(EItems.TIN_ORE_CHUNK.get(), chance(0.11f), Recipes.tagNotEmpty(EItemTags.ORES_TIN));
            drops.addConditional(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.06f), Recipes.tagNotEmpty(EItemTags.ORES_IRIDIUM));
            drops.addConditional(EItems.PLATINUM_ORE_CHUNK.get(), chance(0.065f), Recipes.tagNotEmpty(EItemTags.ORES_PLATINUM));
            drops.addConditional(EItems.URANIUM_ORE_CHUNK.get(), chance(0.08f), Recipes.tagNotEmpty(EItemTags.ORES_URANIUM));
            drops.addConditional(EItems.MAGNESIUM_ORE_CHUNK.get(), chance(0.11f), Recipes.tagNotEmpty(EItemTags.ORES_MAGNESIUM));
            drops.addConditional(EItems.THORIUM_ORE_CHUNK.get(), chance(0.075f), Recipes.tagNotEmpty(EItemTags.ORES_THORIUM));
            drops.addConditional(EItems.BORON_ORE_CHUNK.get(), chance(0.06f), Recipes.tagNotEmpty(EItemTags.ORES_BORON));
            drops.addConditional(EItems.LITHIUM_ORE_CHUNK.get(), chance(0.06f), Recipes.tagNotEmpty(EItemTags.ORES_LITHIUM));
        });
        forMesh(writer, CRUSHED_DEEPSLATE, EItems.GOLDEN_MESH, drops -> {
            drops.add(EItems.DEEPSLATE_PEBBLE.get(), binomial(4, 0.65f));
            drops.add(EItems.COPPER_ORE_CHUNK.get(), chance(0.09f));
            drops.add(EItems.IRON_ORE_CHUNK.get(), chance(0.15f));
            drops.add(EItems.GOLD_ORE_CHUNK.get(), chance(0.15f));
            drops.add(Items.AMETHYST_SHARD, chance(0.08f));
            drops.add(Items.DIAMOND, chance(0.08f));
            drops.add(Items.LAPIS_LAZULI, chance(0.07f));
            drops.add(Items.EMERALD, chance(0.07f));
            drops.add(Items.RAW_GOLD, chance(0.05f));
            drops.add(Items.GOLD_NUGGET, binomial(3, 0.1f));

            drops.addConditional(EItems.SILVER_ORE_CHUNK.get(), chance(0.15f), Recipes.tagNotEmpty(EItemTags.ORES_SILVER));
            drops.addConditional(EItems.LEAD_ORE_CHUNK.get(), chance(0.09f), Recipes.tagNotEmpty(EItemTags.ORES_LEAD));
            drops.addConditional(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.09f), Recipes.tagNotEmpty(EItemTags.ORES_OSMIUM));
            drops.addConditional(EItems.NICKEL_ORE_CHUNK.get(), chance(0.11f), Recipes.tagNotEmpty(EItemTags.ORES_NICKEL));
            drops.addConditional(EItems.TIN_ORE_CHUNK.get(), chance(0.12f), Recipes.tagNotEmpty(EItemTags.ORES_TIN));
            drops.addConditional(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.04f), Recipes.tagNotEmpty(EItemTags.ORES_IRIDIUM));
            drops.addConditional(EItems.PLATINUM_ORE_CHUNK.get(), chance(0.09f), Recipes.tagNotEmpty(EItemTags.ORES_PLATINUM));
            drops.addConditional(EItems.URANIUM_ORE_CHUNK.get(), chance(0.07f), Recipes.tagNotEmpty(EItemTags.ORES_URANIUM));
            drops.addConditional(EItems.MAGNESIUM_ORE_CHUNK.get(), chance(0.12f), Recipes.tagNotEmpty(EItemTags.ORES_MAGNESIUM));
            drops.addConditional(EItems.THORIUM_ORE_CHUNK.get(), chance(0.075f), Recipes.tagNotEmpty(EItemTags.ORES_THORIUM));
            drops.addConditional(EItems.BORON_ORE_CHUNK.get(), chance(0.065f), Recipes.tagNotEmpty(EItemTags.ORES_BORON));
            drops.addConditional(EItems.LITHIUM_ORE_CHUNK.get(), chance(0.065f), Recipes.tagNotEmpty(EItemTags.ORES_LITHIUM));
        });
        forMesh(writer, CRUSHED_DEEPSLATE, EItems.DIAMOND_MESH, drops -> {
            drops.add(EItems.DEEPSLATE_PEBBLE.get(), binomial(4, 0.65f));
            drops.add(EItems.COPPER_ORE_CHUNK.get(), chance(0.09f));
            drops.add(EItems.IRON_ORE_CHUNK.get(), chance(0.18f));
            drops.add(EItems.GOLD_ORE_CHUNK.get(), chance(0.13f));
            drops.add(Items.AMETHYST_SHARD, chance(0.07f));
            drops.add(Items.DIAMOND, chance(0.08f));
            drops.add(Items.LAPIS_LAZULI, chance(0.12f));
            drops.add(Items.EMERALD, chance(0.08f));

            drops.addConditional(EItems.SILVER_ORE_CHUNK.get(), chance(0.11f), Recipes.tagNotEmpty(EItemTags.ORES_SILVER));
            drops.addConditional(EItems.LEAD_ORE_CHUNK.get(), chance(0.11f), Recipes.tagNotEmpty(EItemTags.ORES_LEAD));
            drops.addConditional(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.12f), Recipes.tagNotEmpty(EItemTags.ORES_OSMIUM));
            drops.addConditional(EItems.NICKEL_ORE_CHUNK.get(), chance(0.11f), Recipes.tagNotEmpty(EItemTags.ORES_NICKEL));
            drops.addConditional(EItems.TIN_ORE_CHUNK.get(), chance(0.13f), Recipes.tagNotEmpty(EItemTags.ORES_TIN));
            drops.addConditional(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.065f), Recipes.tagNotEmpty(EItemTags.ORES_IRIDIUM));
            drops.addConditional(EItems.PLATINUM_ORE_CHUNK.get(), chance(0.075f), Recipes.tagNotEmpty(EItemTags.ORES_PLATINUM));
            drops.addConditional(EItems.URANIUM_ORE_CHUNK.get(), chance(0.1f), Recipes.tagNotEmpty(EItemTags.ORES_URANIUM));
            drops.addConditional(EItems.MAGNESIUM_ORE_CHUNK.get(), chance(0.12f), Recipes.tagNotEmpty(EItemTags.ORES_MAGNESIUM));
            drops.addConditional(EItems.THORIUM_ORE_CHUNK.get(), chance(0.095f), Recipes.tagNotEmpty(EItemTags.ORES_THORIUM));
            drops.addConditional(EItems.BORON_ORE_CHUNK.get(), chance(0.08f), Recipes.tagNotEmpty(EItemTags.ORES_BORON));
            drops.addConditional(EItems.LITHIUM_ORE_CHUNK.get(), chance(0.08f), Recipes.tagNotEmpty(EItemTags.ORES_LITHIUM));
        });
        forMesh(writer, CRUSHED_DEEPSLATE, EItems.NETHERITE_MESH, drops -> {
            drops.add(EItems.DEEPSLATE_PEBBLE.get(), binomial(4, 0.7f));
            drops.add(EItems.COPPER_ORE_CHUNK.get(), chance(0.10f));
            drops.add(EItems.IRON_ORE_CHUNK.get(), chance(0.20f));
            drops.add(EItems.GOLD_ORE_CHUNK.get(), chance(0.15f));
            drops.add(Items.AMETHYST_SHARD, chance(0.1f));
            drops.add(Items.DIAMOND, chance(0.1f));
            drops.add(Items.LAPIS_LAZULI, chance(0.14f));
            drops.add(Items.EMERALD, chance(0.1f));

            drops.addConditional(EItems.SILVER_ORE_CHUNK.get(), chance(0.12f), Recipes.tagNotEmpty(EItemTags.ORES_SILVER));
            drops.addConditional(EItems.LEAD_ORE_CHUNK.get(), chance(0.12f), Recipes.tagNotEmpty(EItemTags.ORES_LEAD));
            drops.addConditional(EItems.OSMIUM_ORE_CHUNK.get(), chance(0.14f), Recipes.tagNotEmpty(EItemTags.ORES_OSMIUM));
            drops.addConditional(EItems.NICKEL_ORE_CHUNK.get(), chance(0.15f), Recipes.tagNotEmpty(EItemTags.ORES_NICKEL));
            drops.addConditional(EItems.TIN_ORE_CHUNK.get(), chance(0.16f), Recipes.tagNotEmpty(EItemTags.ORES_TIN));
            drops.addConditional(EItems.IRIDIUM_ORE_CHUNK.get(), chance(0.065f), Recipes.tagNotEmpty(EItemTags.ORES_IRIDIUM));
            drops.addConditional(EItems.PLATINUM_ORE_CHUNK.get(), chance(0.09f), Recipes.tagNotEmpty(EItemTags.ORES_PLATINUM));
            drops.addConditional(EItems.URANIUM_ORE_CHUNK.get(), chance(0.12f), Recipes.tagNotEmpty(EItemTags.ORES_URANIUM));
            drops.addConditional(EItems.MAGNESIUM_ORE_CHUNK.get(), chance(0.14f), Recipes.tagNotEmpty(EItemTags.ORES_MAGNESIUM));
            drops.addConditional(EItems.THORIUM_ORE_CHUNK.get(), chance(0.11f), Recipes.tagNotEmpty(EItemTags.ORES_THORIUM));
            drops.addConditional(EItems.BORON_ORE_CHUNK.get(), chance(0.09f), Recipes.tagNotEmpty(EItemTags.ORES_BORON));
            drops.addConditional(EItems.LITHIUM_ORE_CHUNK.get(), chance(0.085f), Recipes.tagNotEmpty(EItemTags.ORES_LITHIUM));
        });

        forMesh(writer, CRUSHED_BLACKSTONE, EItems.STRING_MESH, drops -> {
            drops.add(EItems.BLACKSTONE_PEBBLE.get(), binomial(4, 0.6f));
            drops.add(EItems.BASALT_PEBBLE.get(), binomial(3, 0.5f));
            drops.add(Items.ANCIENT_DEBRIS, chance(0.02f));
            drops.add(Items.GOLD_NUGGET, binomial(4, 0.2f));
            drops.add(Items.MAGMA_CREAM, chance(0.08f));
            drops.add(Items.GUNPOWDER, chance(0.07f));
            drops.add(Items.BLACK_DYE, chance(0.07f));
        });
        forMesh(writer, CRUSHED_BLACKSTONE, EItems.FLINT_MESH, drops -> {
            drops.add(EItems.BLACKSTONE_PEBBLE.get(), binomial(4, 0.65f));
            drops.add(EItems.BASALT_PEBBLE.get(), binomial(3, 0.55f));
            drops.add(Items.ANCIENT_DEBRIS, chance(0.03f));
            drops.add(Items.GOLD_NUGGET, binomial(4, 0.225f));
            drops.add(Items.MAGMA_CREAM, chance(0.09f));
            drops.add(Items.GUNPOWDER, chance(0.09f));
            drops.add(Items.BLACK_DYE, chance(0.08f));
        });
        forMesh(writer, CRUSHED_BLACKSTONE, EItems.IRON_MESH, drops -> {
            drops.add(EItems.BLACKSTONE_PEBBLE.get(), binomial(5, 0.65f));
            drops.add(EItems.BASALT_PEBBLE.get(), binomial(4, 0.55f));
            drops.add(Items.ANCIENT_DEBRIS, chance(0.04f));
            drops.add(Items.GOLD_NUGGET, binomial(4, 0.25f));
            drops.add(Items.MAGMA_CREAM, chance(0.09f));
            drops.add(Items.GUNPOWDER, chance(0.09f));
            drops.add(Items.BLACK_DYE, chance(0.08f));
        });
        forMesh(writer, CRUSHED_BLACKSTONE, EItems.GOLDEN_MESH, drops -> {
            drops.add(EItems.BLACKSTONE_PEBBLE.get(), binomial(5, 0.7f));
            drops.add(EItems.BASALT_PEBBLE.get(), binomial(4, 0.5f));
            drops.add(Items.ANCIENT_DEBRIS, chance(0.05f));
            drops.add(Items.GOLD_NUGGET, binomial(8, 0.325f));
            drops.add(Items.MAGMA_CREAM, chance(0.1f));
            drops.add(Items.GUNPOWDER, chance(0.1f));
            drops.add(Items.BLACK_DYE, chance(0.06f));
        });
        forMesh(writer, CRUSHED_BLACKSTONE, EItems.DIAMOND_MESH, drops -> {
            drops.add(EItems.BLACKSTONE_PEBBLE.get(), binomial(5, 0.7f));
            drops.add(Items.ANCIENT_DEBRIS, chance(0.06f));
            drops.add(Items.GOLD_NUGGET, binomial(4, 0.275f));
            drops.add(Items.MAGMA_CREAM, chance(0.11f));
            drops.add(Items.GUNPOWDER, chance(0.11f));
        });
        forMesh(writer, CRUSHED_BLACKSTONE, EItems.NETHERITE_MESH, drops -> {
            drops.add(EItems.BLACKSTONE_PEBBLE.get(), binomial(5, 0.75f));
            drops.add(Items.ANCIENT_DEBRIS, chance(0.1f));
            drops.add(Items.GOLD_NUGGET, binomial(4, 0.325f));
            drops.add(Items.MAGMA_CREAM, chance(0.12f));
            drops.add(Items.GUNPOWDER, chance(0.11f));
        });

        forMesh(writer, CRUSHED_NETHERRACK, EItems.STRING_MESH, drops -> {
            drops.add(EItems.BLACKSTONE_PEBBLE.get(), binomial(3, 0.4f));
            drops.add(EItems.BASALT_PEBBLE.get(), binomial(3, 0.3f));
            drops.add(Items.BLAZE_POWDER, chance(0.08f));
            drops.add(Items.QUARTZ, chance(0.08f));
            drops.add(Items.MAGMA_CREAM, chance(0.05f));
            drops.add(Items.GUNPOWDER, chance(0.08f));
            drops.add(EItems.WARPED_NYLIUM_SPORES.get(), chance(0.05f));
            drops.add(EItems.CRIMSON_NYLIUM_SPORES.get(), chance(0.05f));
            drops.add(Items.GOLD_NUGGET, chance(0.07f));

            drops.addConditional(EItems.COBALT_ORE_CHUNK.get(), chance(0.04f), Recipes.tagNotEmpty(EItemTags.ORES_COBALT));
        });
        forMesh(writer, CRUSHED_NETHERRACK, EItems.FLINT_MESH, drops -> {
            drops.add(EItems.BLACKSTONE_PEBBLE.get(), binomial(4, 0.5f));
            drops.add(EItems.BASALT_PEBBLE.get(), binomial(4, 0.4f));
            drops.add(Items.BLAZE_POWDER, chance(0.09f));
            drops.add(Items.QUARTZ, chance(0.09f));
            drops.add(Items.MAGMA_CREAM, chance(0.06f));
            drops.add(Items.GUNPOWDER, chance(0.09f));
            drops.add(EItems.WARPED_NYLIUM_SPORES.get(), chance(0.07f));
            drops.add(EItems.CRIMSON_NYLIUM_SPORES.get(), chance(0.07f));
            drops.add(Items.GOLD_NUGGET, chance(0.08f));

            drops.addConditional(EItems.COBALT_ORE_CHUNK.get(), chance(0.05f), Recipes.tagNotEmpty(EItemTags.ORES_COBALT));
        });
        forMesh(writer, CRUSHED_NETHERRACK, EItems.IRON_MESH, drops -> {
            drops.add(EItems.BLACKSTONE_PEBBLE.get(), binomial(4, 0.6f));
            drops.add(EItems.BASALT_PEBBLE.get(), binomial(4, 0.45f));
            drops.add(Items.BLAZE_POWDER, chance(0.1f));
            drops.add(Items.QUARTZ, chance(0.11f));
            drops.add(Items.MAGMA_CREAM, chance(0.07f));
            drops.add(Items.GUNPOWDER, chance(0.1f));
            drops.add(EItems.WARPED_NYLIUM_SPORES.get(), chance(0.08f));
            drops.add(EItems.CRIMSON_NYLIUM_SPORES.get(), chance(0.08f));
            drops.add(Items.GOLD_NUGGET, chance(0.1f));

            drops.addConditional(EItems.COBALT_ORE_CHUNK.get(), chance(0.065f), Recipes.tagNotEmpty(EItemTags.ORES_COBALT));
        });
        forMesh(writer, CRUSHED_NETHERRACK, EItems.GOLDEN_MESH, drops -> {
            drops.add(EItems.BLACKSTONE_PEBBLE.get(), binomial(4, 0.6f));
            drops.add(EItems.BASALT_PEBBLE.get(), binomial(4, 0.45f));
            drops.add(Items.BLAZE_POWDER, chance(0.11f));
            drops.add(Items.QUARTZ, chance(0.13f));
            drops.add(Items.MAGMA_CREAM, chance(0.08f));
            drops.add(Items.GUNPOWDER, chance(0.11f));
            drops.add(EItems.WARPED_NYLIUM_SPORES.get(), chance(0.08f));
            drops.add(EItems.CRIMSON_NYLIUM_SPORES.get(), chance(0.08f));
            drops.add(Items.GOLD_NUGGET, chance(0.14f));
            drops.add(Items.RAW_GOLD, chance(0.03f));

            drops.addConditional(EItems.COBALT_ORE_CHUNK.get(), chance(0.07f), Recipes.tagNotEmpty(EItemTags.ORES_COBALT));
        });
        forMesh(writer, CRUSHED_NETHERRACK, EItems.DIAMOND_MESH, drops -> {
            drops.add(EItems.BLACKSTONE_PEBBLE.get(), binomial(4, 0.6f));
            drops.add(Items.BLAZE_POWDER, chance(0.14f));
            drops.add(Items.QUARTZ, chance(0.13f));
            drops.add(Items.MAGMA_CREAM, chance(0.1f));
            drops.add(Items.GUNPOWDER, chance(0.13f));
            drops.add(Items.GOLD_NUGGET, chance(0.12f));

            drops.addConditional(EItems.COBALT_ORE_CHUNK.get(), chance(0.09f), Recipes.tagNotEmpty(EItemTags.ORES_COBALT));
        });
        forMesh(writer, CRUSHED_NETHERRACK, EItems.NETHERITE_MESH, drops -> {
            drops.add(EItems.BLACKSTONE_PEBBLE.get(), binomial(5, 0.65f));
            drops.add(Items.BLAZE_POWDER, chance(0.15f));
            drops.add(Items.QUARTZ, chance(0.15f));
            drops.add(Items.MAGMA_CREAM, chance(0.1f));
            drops.add(Items.GUNPOWDER, chance(0.13f));
            drops.add(Items.GOLD_NUGGET, chance(0.12f));

            drops.addConditional(EItems.COBALT_ORE_CHUNK.get(), chance(0.11f), Recipes.tagNotEmpty(EItemTags.ORES_COBALT));
        });

        forMesh(writer, SOUL_SAND, EItems.STRING_MESH, drops -> {
            drops.add(Items.QUARTZ, chance(0.12f));
            drops.add(Items.GUNPOWDER, chance(0.07f));
            drops.add(Items.BONE, chance(0.08f));
            drops.add(Items.GHAST_TEAR, chance(0.06f));
            drops.add(Items.NETHER_WART, chance(0.06f));
            drops.add(Items.GLOWSTONE_DUST, chance(0.06f));
        });
        forMesh(writer, SOUL_SAND, EItems.FLINT_MESH, drops -> {
            drops.add(Items.QUARTZ, chance(0.14f));
            drops.add(Items.GUNPOWDER, chance(0.08f));
            drops.add(Items.BONE, chance(0.1f));
            drops.add(Items.GHAST_TEAR, chance(0.07f));
            drops.add(Items.GLOWSTONE_DUST, chance(0.07f));
            drops.add(Items.NETHER_WART, chance(0.06f));
            drops.add(EItems.WARPED_NYLIUM_SPORES.get(), chance(0.03f));
            drops.add(EItems.CRIMSON_NYLIUM_SPORES.get(), chance(0.03f));
        });
        forMesh(writer, SOUL_SAND, EItems.IRON_MESH, drops -> {
            drops.add(Items.QUARTZ, chance(0.15f));
            drops.add(Items.GUNPOWDER, chance(0.07f));
            drops.add(Items.BONE, chance(0.08f));
            drops.add(Items.GHAST_TEAR, chance(0.06f));
            drops.add(Items.GLOWSTONE_DUST, chance(0.06f));
            drops.add(Items.NETHER_WART, chance(0.05f));
        });
        forMesh(writer, SOUL_SAND, EItems.GOLDEN_MESH, drops -> {
            drops.add(Items.QUARTZ, chance(0.17f));
            drops.add(Items.GUNPOWDER, chance(0.1f));
            drops.add(Items.BONE, chance(0.11f));
            drops.add(Items.GHAST_TEAR, chance(0.08f));
            drops.add(Items.GLOWSTONE_DUST, chance(0.09f));
            drops.add(Items.NETHER_WART, chance(0.08f));
            drops.add(Items.GOLD_NUGGET, chance(0.15f));
        });
        forMesh(writer, SOUL_SAND, EItems.DIAMOND_MESH, drops -> {
            drops.add(Items.QUARTZ, chance(0.19f));
            drops.add(Items.GUNPOWDER, chance(0.11f));
            drops.add(Items.GHAST_TEAR, chance(0.09f));
            drops.add(Items.GLOWSTONE_DUST, chance(0.11f));
            drops.add(Items.NETHER_WART, chance(0.1f));
        });
        forMesh(writer, SOUL_SAND, EItems.NETHERITE_MESH, drops -> {
            drops.add(Items.QUARTZ, chance(0.21f));
            drops.add(Items.GUNPOWDER, chance(0.14f));
            drops.add(Items.GHAST_TEAR, chance(0.11f));
            drops.add(Items.GLOWSTONE_DUST, chance(0.13f));
            drops.add(Items.NETHER_WART, chance(0.12f));
        });

        forMesh(writer, CRUSHED_END_STONE, EItems.STRING_MESH, drops -> {
            drops.add(Items.ENDER_PEARL, chance(0.07f));
            drops.add(Items.CHORUS_FRUIT, chance(0.09f));
            drops.add(Items.CHORUS_FLOWER, chance(0.04f));
            drops.add(Items.ENDER_EYE, chance(0.02f));
        });
        forMesh(writer, CRUSHED_END_STONE, EItems.FLINT_MESH, drops -> {
            drops.add(Items.ENDER_PEARL, chance(0.08f));
            drops.add(Items.CHORUS_FRUIT, chance(0.11f));
            drops.add(Items.CHORUS_FLOWER, chance(0.06f));
            drops.add(Items.ENDER_EYE, chance(0.03f));
        });
        forMesh(writer, CRUSHED_END_STONE, EItems.IRON_MESH, drops -> {
            drops.add(Items.ENDER_PEARL, chance(0.10f));
            drops.add(Items.CHORUS_FRUIT, chance(0.13f));
            drops.add(Items.CHORUS_FLOWER, chance(0.07f));
            drops.add(Items.ENDER_EYE, chance(0.04f));
        });
        forMesh(writer, CRUSHED_END_STONE, EItems.GOLDEN_MESH, drops -> {
            drops.add(Items.ENDER_PEARL, chance(0.12f));
            drops.add(Items.CHORUS_FRUIT, chance(0.12f));
            drops.add(Items.CHORUS_FLOWER, chance(0.06f));
            drops.add(Items.ENDER_EYE, chance(0.07f));
        });
        forMesh(writer, CRUSHED_END_STONE, EItems.DIAMOND_MESH, drops -> {
            drops.add(Items.ENDER_PEARL, chance(0.15f));
            drops.add(Items.CHORUS_FRUIT, chance(0.10f));
            drops.add(Items.CHORUS_FLOWER, chance(0.04f));
            drops.add(Items.ENDER_EYE, chance(0.09f));
        });
        forMesh(writer, CRUSHED_END_STONE, EItems.NETHERITE_MESH, drops -> {
            drops.add(Items.ENDER_PEARL, chance(0.17f));
            drops.add(Items.CHORUS_FRUIT, chance(0.10f));
            drops.add(Items.CHORUS_FLOWER, chance(0.04f));
            drops.add(Items.ENDER_EYE, chance(0.09f));
            drops.add(Items.ECHO_SHARD, chance(0.03f));
            drops.add(Items.SCULK_SHRIEKER, chance(0.01f));
        });

        for (int i = 0; i < allMeshes.size(); i++) {
            var mesh = allMeshes.get(i);
            final int j = i;
            forMesh(writer, MOSS_BLOCK, mesh, drops -> {
                drops.add(Items.OAK_SAPLING, chance(0.13f));
                drops.add(Items.SPRUCE_SAPLING, chance(0.11f));
                drops.add(Items.BIRCH_SAPLING, chance(0.11f));
                drops.add(Items.ACACIA_SAPLING, chance(0.11f));
                drops.add(Items.DARK_OAK_SAPLING, chance(0.11f));
                drops.add(Items.JUNGLE_SAPLING, chance(0.11f));
                drops.add(Items.CHERRY_SAPLING, chance(0.11f));
                drops.add(Items.MANGROVE_PROPAGULE, chance(0.11f));
                drops.add(Items.AZALEA, chance(0.08f + j * 0.01f));
                drops.add(Items.GLOW_BERRIES, chance(0.04f + j * 0.075f));
                drops.add(Items.SMALL_DRIPLEAF, chance(0.07f + j * 0.025f));
                drops.add(Items.BIG_DRIPLEAF, chance(0.05f + j * 0.02f));
                drops.add(Items.SPORE_BLOSSOM, chance(0.03f + j * 0.015f));

                var bop = Recipes.modInstalled(ModIds.BIOMES_O_PLENTY);
                drops.addConditional(ModCompatData.ORIGIN_SAPLING.get(), chance(0.04f), bop);
                drops.addConditional(ModCompatData.FLOWERING_OAK_SAPLING.get(), chance(0.04f), bop);
                drops.addConditional(ModCompatData.SNOWBLOSSOM_SAPLING.get(), chance(0.04f), bop);
                drops.addConditional(ModCompatData.RAINBOW_BIRCH_SAPLING.get(), chance(0.04f), bop);
                drops.addConditional(ModCompatData.YELLOW_AUTUMN_SAPLING.get(), chance(0.04f), bop);
                drops.addConditional(ModCompatData.ORANGE_AUTUMN_SAPLING.get(), chance(0.04f), bop);
                drops.addConditional(ModCompatData.MAPLE_SAPLING.get(), chance(0.04f), bop);

                drops.addConditional(ModCompatData.FIR_SAPLING.get(), chance(0.04f), bop);
                drops.addConditional(ModCompatData.REDWOOD_SAPLING.get(), chance(0.04f), bop);
                drops.addConditional(ModCompatData.MAHOGANY_SAPLING.get(), chance(0.04f), bop);
                drops.addConditional(ModCompatData.JACARANDA_SAPLING.get(), chance(0.04f), bop);
                drops.addConditional(ModCompatData.PALM_SAPLING.get(), chance(0.04f), bop);
                drops.addConditional(ModCompatData.WILLOW_SAPLING.get(), chance(0.04f), bop);
                drops.addConditional(ModCompatData.DEAD_SAPLING.get(), chance(0.04f), bop);
                drops.addConditional(ModCompatData.MAGIC_SAPLING.get(), chance(0.04f), bop);
                drops.addConditional(ModCompatData.UMBRAN_SAPLING.get(), chance(0.04f), bop);
                drops.addConditional(ModCompatData.HELLBARK_SAPLING.get(), chance(0.04f), bop);

                var ars = Recipes.modInstalled(ModIds.ARS_NOUVEAU);
                drops.addConditional(ModCompatData.BLUE_ARCHWOOD_SAPLING.get(), chance(0.005f), ars);
                drops.addConditional(ModCompatData.RED_ARCHWOOD_SAPLING.get(), chance(0.005f), ars);
                drops.addConditional(ModCompatData.PURPLE_ARCHWOOD_SAPLING.get(), chance(0.005f), ars);
                drops.addConditional(ModCompatData.GREEN_ARCHWOOD_SAPLING.get(), chance(0.005f), ars);

                drops.addConditional(ModCompatData.SOURCEBERRY.get(), chance(0.01f), ars);
            });
        }
        forMesh(writer, MOSS_BLOCK, EItems.FLINT_MESH, drops -> {
            drops.add(Items.SWEET_BERRIES, chance(0.03f));
            drops.add(Items.FLOWERING_AZALEA, chance(0.03f));
            drops.add(Items.GLOW_LICHEN, chance(0.04f));
            drops.add(Items.LILY_PAD, chance(0.04f));
        });
    }

    private static BinomialDistributionGenerator chance(float p) {
        return binomial(1, p);
    }

    private static void forMesh(Consumer<FinishedRecipe> output, Ingredient block, RegistryObject<? extends Item> mesh, Consumer<MeshDrops> addDrops) {
        var folder = mesh.getId().getPath().replace("_mesh", "/");
        var basePath = path(block.getItems()[0].getItem()) + "/" + folder;

        addDrops.accept(new MeshDrops(output, "sieve/" + basePath, "compressed_sieve/" + basePath, block, mesh.get()));
    }

    private record MeshDrops(Consumer<FinishedRecipe> output, String basePath, String baseCompressedPath, Ingredient block, Item mesh) {
        private void add(Item result, NumberProvider resultAmount) {
            this.output.accept(new FinishedSieveRecipe(modLoc(this.basePath + path(result)), this.mesh, this.block, result, resultAmount));

            if (COMPRESSED_VARIANTS.containsKey(this.block)) {
                var compressedLoc = modLoc(this.baseCompressedPath + path(result));
                var multiplied = Recipes.compressedMultiplier(resultAmount);

                this.output.accept(new FinishedCompressedSieveRecipe(compressedLoc, this.mesh, COMPRESSED_VARIANTS.get(this.block), result, multiplied));
            }
        }

        private void addConditional(Item result, NumberProvider resultAmount, ICondition condition) {
            var path = modLoc(this.basePath + path(result));
            ConditionalRecipe.builder()
                    .addCondition(condition)
                    .addRecipe(new FinishedSieveRecipe(path, this.mesh, this.block, result, resultAmount))
                    .build(this.output, path);

            if (COMPRESSED_VARIANTS.containsKey(this.block)) {
                var compressedLoc = modLoc(this.baseCompressedPath + path(result));
                var multiplied = Recipes.compressedMultiplier(resultAmount);

                ConditionalRecipe.builder()
                        .addCondition(condition)
                        .addRecipe(new FinishedCompressedSieveRecipe(compressedLoc, this.mesh, COMPRESSED_VARIANTS.get(this.block), result, multiplied))
                        .build(this.output, compressedLoc);
            }
        }
    }

    public static ResourceLocation modLoc(String path) {
        return new ResourceLocation(ExDeorum.ID, path);
    }
}
