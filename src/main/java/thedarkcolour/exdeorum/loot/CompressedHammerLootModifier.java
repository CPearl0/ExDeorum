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

package thedarkcolour.exdeorum.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.hammer.HammerRecipe;

public class CompressedHammerLootModifier extends HammerLootModifier {
    public static final Codec<CompressedHammerLootModifier> CODEC = RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst).apply(inst, CompressedHammerLootModifier::new));

    protected CompressedHammerLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Override
    protected @Nullable HammerRecipe getRecipe(Item itemForm) {
        return RecipeUtil.getCompressedHammerRecipe(itemForm);
    }
}
