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

package thedarkcolour.exdeorum.recipe.hammer;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.ProbabilityRecipe;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.sieve.SieveRecipe;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.Objects;

public class HammerRecipe extends ProbabilityRecipe {
    public HammerRecipe(ResourceLocation id, Ingredient ingredient, Item result, NumberProvider resultAmount, @Nullable CompoundTag resultNbt) {
        super(id, ingredient, result, resultAmount, resultNbt);
    }

    public static boolean areEqual(HammerRecipe a, HammerRecipe b) {
        if (a.getClass() != b.getClass()) return false;
        return RecipeUtil.areIngredientsEqual(a.ingredient, b.ingredient)
                && Objects.equals(a.result, b.result)
                && Objects.equals(a.resultNbt, b.resultNbt);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.HAMMER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.HAMMER.get();
    }

    public static abstract class AbstractSerializer<T extends HammerRecipe> implements RecipeSerializer<T> {
        protected abstract T createHammerRecipe(ResourceLocation id, Ingredient ingredient, Item result, NumberProvider resultAmount, @Nullable CompoundTag resultNbt);

        @Override
        public T fromJson(ResourceLocation name, JsonObject json) {
            Ingredient ingredient = RecipeUtil.readIngredient(json, "ingredient");
            Item result = RecipeUtil.readItem(json, "result");
            NumberProvider resultAmount = RecipeUtil.readNumberProvider(json, "result_amount");
            CompoundTag resultNbt = RecipeUtil.readNbtTag(json, "result_nbt");

            return createHammerRecipe(name, ingredient, result, resultAmount, resultNbt);
        }

        @Override
        @SuppressWarnings("deprecation")
        public T fromNetwork(ResourceLocation name, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            Item result = Objects.requireNonNull(buffer.readById(BuiltInRegistries.ITEM));
            NumberProvider resultAmount = RecipeUtil.fromNetworkNumberProvider(buffer);
            CompoundTag resultNbt = buffer.readNbt();
            return createHammerRecipe(name, ingredient, result, resultAmount, resultNbt);
        }

        @Override
        @SuppressWarnings("deprecation")
        public void toNetwork(FriendlyByteBuf buffer, T recipe) {
            recipe.getIngredient().toNetwork(buffer);
            buffer.writeId(BuiltInRegistries.ITEM, recipe.result);
            RecipeUtil.toNetworkNumberProvider(buffer, recipe.resultAmount);
            buffer.writeNbt(recipe.resultNbt);
        }
    }

    public static class Serializer extends AbstractSerializer<HammerRecipe> {
        @Override
        protected HammerRecipe createHammerRecipe(ResourceLocation id, Ingredient ingredient, Item result, NumberProvider resultAmount, @Nullable CompoundTag resultNbt) {
            return new HammerRecipe(id, ingredient, result, resultAmount, resultNbt);
        }
    }
}
