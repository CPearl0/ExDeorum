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

package thedarkcolour.exdeorum.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import thedarkcolour.exdeorum.ExDeorum;

public class EItemTags {
    public static final TagKey<Item> CROOKS = tag("crooks");
    public static final TagKey<Item> HAMMERS = tag("hammers");
    public static final TagKey<Item> COMPRESSED_HAMMERS = tag("compressed_hammers");
    public static final TagKey<Item> SIEVE_MESHES = tag("sieve_meshes");
    public static final TagKey<Item> PEBBLES = tag("pebbles");
    public static final TagKey<Item> END_CAKE_MATERIAL = tag("end_cake_materials");
    public static final TagKey<Item> ORE_CHUNKS = tag("ore_chunks");

    public static final TagKey<Item> WOODEN_BARRELS = tag("wooden_barrels");
    public static final TagKey<Item> STONE_BARRELS = tag("stone_barrels");
    public static final TagKey<Item> BARRELS = tag("barrels");

    public static final TagKey<Item> ORES_ALUMINUM = forgeTag("ores/aluminum");
    public static final TagKey<Item> ORES_COBALT = forgeTag("ores/cobalt");
    public static final TagKey<Item> ORES_SILVER = forgeTag("ores/silver");
    public static final TagKey<Item> ORES_LEAD = forgeTag("ores/lead");
    public static final TagKey<Item> ORES_PLATINUM = forgeTag("ores/platinum");
    public static final TagKey<Item> ORES_NICKEL = forgeTag("ores/nickel");
    public static final TagKey<Item> ORES_URANIUM = forgeTag("ores/uranium");
    public static final TagKey<Item> ORES_OSMIUM = forgeTag("ores/osmium");
    public static final TagKey<Item> ORES_TIN = forgeTag("ores/tin");
    public static final TagKey<Item> ORES_ZINC = forgeTag("ores/zinc");
    public static final TagKey<Item> ORES_IRIDIUM = forgeTag("ores/iridium");
    public static final TagKey<Item> ORES_THORIUM = forgeTag("ores/thorium");
    public static final TagKey<Item> ORES_MAGNESIUM = forgeTag("ores/magnesium");
    public static final TagKey<Item> ORES_LITHIUM = forgeTag("ores/lithium");
    public static final TagKey<Item> ORES_BORON = forgeTag("ores/boron");

    public static final TagKey<Item> COMPRESSED_SANDS = tag("compressed/sands");

    public static TagKey<Item> tag(String name) {
        return ItemTags.create(new ResourceLocation(ExDeorum.ID, name));
    }

    public static TagKey<Item> forgeTag(String name) {
        return ItemTags.create(new ResourceLocation("forge", name));
    }
}
