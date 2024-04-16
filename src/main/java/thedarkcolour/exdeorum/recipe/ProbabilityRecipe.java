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

package thedarkcolour.exdeorum.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jetbrains.annotations.Nullable;

public abstract class ProbabilityRecipe extends SingleIngredientRecipe {
    public final Item result;
    public final NumberProvider resultAmount;
    @Nullable
    protected final CompoundTag resultNbt;

    public ProbabilityRecipe(ResourceLocation id, Ingredient ingredient, Item result, NumberProvider resultAmount, @Nullable CompoundTag resultNbt) {
        super(id, ingredient);
        this.result = result;
        this.resultAmount = resultAmount;
        this.resultNbt = resultNbt;
    }

    @Nullable
    public CompoundTag getResultNbt() {
        return this.resultNbt == null ? null : this.resultNbt.copy();
    }

    // Do not use in ItemStack, compound tags are mutable and new copies should be made per stack
    @Nullable
    public CompoundTag getRawResultNbt() {
        return this.resultNbt;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        ItemStack result = new ItemStack(this.result, 1);
        result.setTag(getResultNbt());
        return result;
    }
}
