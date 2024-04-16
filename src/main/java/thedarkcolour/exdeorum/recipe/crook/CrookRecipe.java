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

package thedarkcolour.exdeorum.recipe.crook;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.BlockPredicate;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.Objects;

public record CrookRecipe(ResourceLocation id, BlockPredicate blockPredicate, Item result, @Nullable CompoundTag resultNbt, float chance) implements Recipe<Container> {
    @Nullable
    public CompoundTag getResultNbt() {
        return this.resultNbt == null ? null : this.resultNbt.copy();
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return false;
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        ItemStack result = new ItemStack(this.result, 1);
        result.setTag(getResultNbt());
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.CROOK.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.CROOK.get();
    }

    public static class Serializer implements RecipeSerializer<CrookRecipe> {
        @Override
        public CrookRecipe fromJson(ResourceLocation id, JsonObject json) {
            BlockPredicate blockPredicate = Objects.requireNonNull(RecipeUtil.readBlockPredicate(id, json, "block_predicate"));

            Item result = RecipeUtil.readItem(json, "result");
            CompoundTag resultNbt = RecipeUtil.readNbtTag(json, "result_nbt");
            float chance = json.get("chance").getAsFloat();

            return new CrookRecipe(id, blockPredicate, result, resultNbt, chance);
        }

        @Override
        @SuppressWarnings("deprecation")
        public CrookRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            BlockPredicate blockPredicate = RecipeUtil.readBlockPredicateNetwork(id, buffer);
            if (blockPredicate == null) return null;

            Item result = Objects.requireNonNull(buffer.readById(BuiltInRegistries.ITEM));
            CompoundTag resultNbt = buffer.readNbt();
            float chance = buffer.readFloat();

            return new CrookRecipe(id, blockPredicate, result, resultNbt, chance);
        }

        @Override
        @SuppressWarnings("deprecation")
        public void toNetwork(FriendlyByteBuf buffer, CrookRecipe recipe) {
            recipe.blockPredicate.toNetwork(buffer);
            buffer.writeId(BuiltInRegistries.ITEM, recipe.result);
            buffer.writeNbt(recipe.resultNbt);
            buffer.writeFloat(recipe.chance);
        }
    }
}
