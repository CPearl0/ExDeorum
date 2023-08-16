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

package thedarkcolour.exdeorum.recipe;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.recipe.barrel.BarrelCompostRecipe;
import thedarkcolour.exdeorum.recipe.barrel.BarrelMixingRecipe;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exdeorum.recipe.sieve.SieveRecipe;
import thedarkcolour.exdeorum.registry.ERecipeTypes;
import thedarkcolour.exdeorum.tag.EItemTags;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class RecipeUtil {
    private static final int CONSTANT_TYPE = 1;
    private static final int UNIFORM_TYPE = 2;
    private static final int BINOMIAL_TYPE = 3;
    private static final int UNKNOWN_TYPE = 99;

    private static final Cache<SieveCacheKey, ImmutableList<SieveRecipe>> SIEVE_RECIPE_CACHE = CacheBuilder.newBuilder().expireAfterAccess(Duration.ofMinutes(3)).build();
    private static Lazy<Map<Item, BarrelCompostRecipe>> barrelCompostRecipeCache;
    private static Lazy<Map<Item, CrucibleRecipe>> lavaCrucibleRecipeCache;
    private static Lazy<Map<Item, CrucibleRecipe>> waterCrucibleRecipeCache;
    private static final Map<TagKey<Item>, Item> preferredTagItems = new Object2ObjectOpenHashMap<>(11, Hash.DEFAULT_LOAD_FACTOR);

    public static void reload(RecipeManager recipes) {
        SIEVE_RECIPE_CACHE.invalidateAll();

        barrelCompostRecipeCache = Lazy.of(() -> loadSimpleRecipeCache(recipes, ERecipeTypes.BARREL_COMPOST));
        lavaCrucibleRecipeCache = Lazy.of(() -> loadSimpleRecipeCache(recipes, ERecipeTypes.LAVA_CRUCIBLE));
        waterCrucibleRecipeCache = Lazy.of(() -> loadSimpleRecipeCache(recipes, ERecipeTypes.WATER_CRUCIBLE));

        preferredTagItems.clear();
        preferredTagItems.put(EItemTags.ORES_ALUMINUM, ForgeRegistries.ITEMS.getValue(EConfig.SERVER.preferredAluminumOre.get()));
        preferredTagItems.put(EItemTags.ORES_COBALT, ForgeRegistries.ITEMS.getValue(EConfig.SERVER.preferredCobaltOre.get()));
        preferredTagItems.put(EItemTags.ORES_SILVER, ForgeRegistries.ITEMS.getValue(EConfig.SERVER.preferredSilverOre.get()));
        preferredTagItems.put(EItemTags.ORES_LEAD, ForgeRegistries.ITEMS.getValue(EConfig.SERVER.preferredLeadOre.get()));
        preferredTagItems.put(EItemTags.ORES_PLATINUM, ForgeRegistries.ITEMS.getValue(EConfig.SERVER.preferredPlatinumOre.get()));
        preferredTagItems.put(EItemTags.ORES_NICKEL, ForgeRegistries.ITEMS.getValue(EConfig.SERVER.preferredNickelOre.get()));
        preferredTagItems.put(EItemTags.ORES_URANIUM, ForgeRegistries.ITEMS.getValue(EConfig.SERVER.preferredUraniumOre.get()));
        preferredTagItems.put(EItemTags.ORES_OSMIUM, ForgeRegistries.ITEMS.getValue(EConfig.SERVER.preferredOsmiumOre.get()));
        preferredTagItems.put(EItemTags.ORES_TIN, ForgeRegistries.ITEMS.getValue(EConfig.SERVER.preferredTinOre.get()));
        preferredTagItems.put(EItemTags.ORES_ZINC, ForgeRegistries.ITEMS.getValue(EConfig.SERVER.preferredZincOre.get()));
        preferredTagItems.put(EItemTags.ORES_IRIDIUM, ForgeRegistries.ITEMS.getValue(EConfig.SERVER.preferredIridiumOre.get()));
    }

    // Copied from ServerLifecycleHooks.getServerConfigPath
    private static Path getServerConfigPath(final MinecraftServer server) {
        final Path serverConfig = server.getWorldPath(new LevelResource("serverconfig"));
        if (!Files.isDirectory(serverConfig)) {
            try {
                Files.createDirectories(serverConfig);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return serverConfig;
    }

    private static <T extends SingleIngredientRecipe> ImmutableMap<Item, T> loadSimpleRecipeCache(RecipeManager recipes, Supplier<RecipeType<T>> recipeType) {
        var builder = new ImmutableMap.Builder<Item, T>();
        for (var recipe : recipes.byType(recipeType.get()).values()) {
            for (var item : recipe.getIngredient().getItems()) {
                builder.put(item.getItem(), recipe);
            }
        }

        return builder.buildKeepingLast();
    }

    public static List<SieveRecipe> getSieveRecipes(RecipeManager manager, Item mesh, ItemStack item) {
        var cacheKey = new SieveCacheKey(mesh, item.getItem());
        var cacheVal = SIEVE_RECIPE_CACHE.getIfPresent(cacheKey);
        if (cacheVal != null) return cacheVal;

        var builder = new ImmutableList.Builder<SieveRecipe>();
        var cache = true;

        for (var recipe : byType(manager, ERecipeTypes.SIEVE.get())) {
            if (recipe.test(mesh, item)) {
                builder.add(recipe);

                if (recipe.dependsOnNbt) {
                    cache = false;
                }
            }
        }

        var recipes = builder.build();
        if (cache) {
            SIEVE_RECIPE_CACHE.put(cacheKey, recipes);
        }

        return recipes;
    }

    public static CrucibleRecipe getLavaCrucibleRecipe(ItemStack item) {
        return lavaCrucibleRecipeCache.get().get(item.getItem());
    }

    public static CrucibleRecipe getWaterCrucibleRecipe(ItemStack item) {
        return waterCrucibleRecipeCache.get().get(item.getItem());
    }

    public static BarrelCompostRecipe getBarrelCompostRecipe(ItemStack playerItem) {
        return barrelCompostRecipeCache.get().get(playerItem.getItem());
    }

    public static <C extends Container, T extends Recipe<C>> Collection<T> byType(RecipeManager manager, RecipeType<T> type) {
        return manager.byType(type).values();
    }

    public static Ingredient readIngredient(JsonObject json, String key) {
        if (GsonHelper.isArrayNode(json, key)) {
            return Ingredient.fromJson(GsonHelper.getAsJsonArray(json, key));
        } else {
            return Ingredient.fromJson(GsonHelper.getAsJsonObject(json, key));
        }
    }

    public static Item readItem(JsonObject json, String key) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(json, key)));
    }

    public static NumberProvider readNumberProvider(JsonObject json, String key) {
        var obj = json.get(key);
        return LootDataType.PREDICATE.parser().fromJson(obj, NumberProvider.class);
    }

    public static void toNetworkNumberProvider(FriendlyByteBuf buffer, NumberProvider provider) {
        if (provider.getType() == NumberProviders.CONSTANT) {
            buffer.writeByte(CONSTANT_TYPE);
            buffer.writeFloat(((ConstantValue) provider).value);
        } else if (provider.getType() == NumberProviders.UNIFORM) {
            var uniform = (UniformGenerator) provider;
            buffer.writeByte(UNIFORM_TYPE);
            toNetworkNumberProvider(buffer, uniform.min);
            toNetworkNumberProvider(buffer, uniform.max);
        } else if (provider.getType() == NumberProviders.BINOMIAL) {
            var binomial = (BinomialDistributionGenerator) provider;
            buffer.writeByte(BINOMIAL_TYPE);
            toNetworkNumberProvider(buffer, binomial.n);
            toNetworkNumberProvider(buffer, binomial.p);
        } else {
            buffer.writeByte(UNKNOWN_TYPE);
        }
    }

    public static NumberProvider fromNetworkNumberProvider(FriendlyByteBuf buffer) {
        return switch (buffer.readByte()) {
            case CONSTANT_TYPE -> ConstantValue.exactly(buffer.readFloat());
            case UNIFORM_TYPE -> new UniformGenerator(fromNetworkNumberProvider(buffer), fromNetworkNumberProvider(buffer));
            case BINOMIAL_TYPE -> new BinomialDistributionGenerator(fromNetworkNumberProvider(buffer), fromNetworkNumberProvider(buffer));
            default -> ConstantValue.exactly(1f);
        };
    }

    // todo support Forge's ingredient types
    public static boolean areIngredientsEqual(Ingredient first, Ingredient second) {
        // although unlikely, we should check this anyway
        if (first == second) return true;

        if (first.isVanilla() && second.isVanilla()) {
            var firstValues = new ObjectArrayList<>(first.values);
            var secondValues = new ObjectArrayList<>(second.values);

            // if arrays are same size, check if their contents are equal (order does not matter)
            if (firstValues.size() == secondValues.size()) {
                outer:
                for (int i = 0; i < firstValues.size(); i++) {
                    var firstValue = firstValues.get(i);

                    for (int j = 0; j < firstValues.size(); j++) {
                        if (areValuesEqual(firstValue, secondValues.get(j))) {
                            firstValues.remove(i);
                            secondValues.remove(j);
                            i--;

                            continue outer;
                        }
                    }

                    return false;
                }

                // return true if everything was equal
                return true;
            }
        }

        return false;
    }

    private static boolean areValuesEqual(Ingredient.Value firstValue, Ingredient.Value secondValue) {
        Class<?> firstKlass = firstValue.getClass();
        Class<?> secondKlass = secondValue.getClass();

        // if values are the same type of class
        if (firstKlass == secondKlass) {
            if (firstKlass == Ingredient.ItemValue.class) {
                // if items are different, return false
                return ItemStack.matches(((Ingredient.ItemValue) firstValue).item, ((Ingredient.ItemValue) secondValue).item);
            } else if (firstKlass == Ingredient.TagValue.class) {
                // if tags are different, return false
                // identity comparison is okay because tags are always interned in vanilla
                return ((Ingredient.TagValue) firstValue).tag == ((Ingredient.TagValue) secondValue).tag;
            } else {
                var firstItems = firstValue.getItems();
                var secondItems = secondValue.getItems();
                var len = firstItems.size();

                if (len == secondItems.size()) {
                    Iterator<ItemStack> firstIter = firstItems.iterator();
                    Iterator<ItemStack> secondIter = secondItems.iterator();

                    while (firstIter.hasNext()) {
                        if (!ItemStack.matches(firstIter.next(), secondIter.next())) {
                            // if one of the items is different, return false
                            return false;
                        }
                    }
                } else {
                    // if values have different amounts of items, return false
                    return false;
                }

                // if all items are the same, return true
                return true;
            }
        } else {
            // if the values are different types, return false
            return false;
        }
    }

    public static boolean hasSieveResult(RecipeManager recipes, Item mesh, ItemStack stack) {
        for (var recipe : byType(recipes, ERecipeTypes.SIEVE.get())) {
            if (recipe.test(mesh, stack)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isCompostable(ItemStack stack) {
        return barrelCompostRecipeCache != null && barrelCompostRecipeCache.get().containsKey(stack.getItem());
    }

    public static @Nullable BarrelMixingRecipe getBarrelMixingRecipe(RecipeManager recipes, ItemStack stack, FluidStack fluid) {
        for (var recipe : byType(recipes, ERecipeTypes.BARREL_MIXING.get())) {
            if (recipe.matches(stack, fluid)) {
                return recipe;
            }
        }

        return null;
    }

    public static double getExpectedValue(NumberProvider provider) {
        if (provider instanceof ConstantValue constant) {
            return constant.value;
        } else if (provider instanceof UniformGenerator uniform) {
            return getExpectedValue(uniform.min) + getExpectedValue(uniform.max) / 2.0;
        } else if (provider instanceof BinomialDistributionGenerator binomial) {
            return getExpectedValue(binomial.n) * getExpectedValue(binomial.p);
        } else {
            // no way of knowing beforehand so just put them last
            return -1.0;
        }
    }

    @SuppressWarnings("deprecation")
    public static Item getPreferredItem(TagKey<Item> tag) {
        Item preferred = preferredTagItems.get(tag);

        if (preferred != null && preferred != Items.AIR) {
            return preferred;
        } else {
            var collection = Lists.newArrayList(BuiltInRegistries.ITEM.getTagOrEmpty(tag));

            if (collection.isEmpty()) {
                return Items.AIR;
            } else {
                collection.sort(Comparator.comparing(holder -> BuiltInRegistries.ITEM.getKey(holder.get())));

                return collection.get(0).get();
            }
        }
    }

    private record SieveCacheKey(Item mesh, Item ingredient) {
    }
}
