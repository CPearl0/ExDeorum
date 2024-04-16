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

package thedarkcolour.exdeorum.recipe.barrel;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.SingleIngredientRecipe;
import thedarkcolour.exdeorum.registry.ERecipeSerializers;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

public class BarrelMixingRecipe extends SingleIngredientRecipe {
    public final Fluid fluid;
    public final int fluidAmount;
    public final Item result;
    @Nullable
    protected final CompoundTag resultNbt;

    public BarrelMixingRecipe(ResourceLocation id, Ingredient ingredient, Fluid fluid, int fluidAmount, Item result, @Nullable CompoundTag resultNbt) {
        super(id, ingredient);
        this.fluid = fluid;
        this.fluidAmount = fluidAmount;
        this.result = result;
        this.resultNbt = resultNbt;
    }

    @Nullable
    public CompoundTag getResultNbt() {
        return this.resultNbt == null ? null : this.resultNbt.copy();
    }

    // Do not use
    @Override
    @Deprecated
    public boolean matches(Container inventory, Level level) {
        return false;
    }

    public boolean matches(ItemStack item, FluidStack fluid) {
        return this.ingredient.test(item) && fluid.getFluid() == this.fluid && fluid.getAmount() >= this.fluidAmount;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return new ItemStack(this.result);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ERecipeSerializers.BARREL_MIXING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ERecipeTypes.BARREL_MIXING.get();
    }

    public static class Serializer implements RecipeSerializer<BarrelMixingRecipe> {
        @Override
        public BarrelMixingRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient ingredient = RecipeUtil.readIngredient(json, "ingredient");
            Fluid fluid = RecipeUtil.readFluid(json, "fluid");
            int fluidAmount = GsonHelper.getAsInt(json, "fluid_amount");
            Item result = RecipeUtil.readItem(json, "result");
            CompoundTag resultNbt = RecipeUtil.readNbtTag(json, "result_nbt");

            return new BarrelMixingRecipe(id, ingredient, fluid, fluidAmount, result, resultNbt);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, BarrelMixingRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeRegistryId(ForgeRegistries.FLUIDS, recipe.fluid);
            buffer.writeVarInt(recipe.fluidAmount);
            buffer.writeRegistryId(ForgeRegistries.ITEMS, recipe.result);
            buffer.writeNbt(recipe.resultNbt);
        }

        @Override
        public @Nullable BarrelMixingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            Fluid fluid = buffer.readRegistryId();
            int fluidAmount = buffer.readVarInt();
            Item result = buffer.readRegistryId();
            CompoundTag resultNbt = buffer.readNbt();

            return new BarrelMixingRecipe(id, ingredient, fluid, fluidAmount, result, resultNbt);
        }
    }
}
