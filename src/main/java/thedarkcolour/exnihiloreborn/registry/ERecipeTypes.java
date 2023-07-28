package thedarkcolour.exnihiloreborn.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.recipe.barrel.BarrelCompostRecipe;
import thedarkcolour.exnihiloreborn.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exnihiloreborn.recipe.hammer.HammerRecipe;
import thedarkcolour.exnihiloreborn.recipe.sieve.SieveRecipe;

public class ERecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, ExNihiloReborn.ID);

    public static final RegistryObject<RecipeType<BarrelCompostRecipe>> BARREL_COMPOST = RECIPE_TYPES.register("barrel_compost", () -> RecipeType.simple(ERecipeTypes.BARREL_COMPOST.getId()));

    public static final RegistryObject<RecipeType<CrucibleRecipe>> LAVA_CRUCIBLE = RECIPE_TYPES.register("lava_crucible", () -> RecipeType.simple(ERecipeTypes.LAVA_CRUCIBLE.getId()));
    public static final RegistryObject<RecipeType<CrucibleRecipe>> WATER_CRUCIBLE = RECIPE_TYPES.register("water_crucible", () -> RecipeType.simple(ERecipeTypes.WATER_CRUCIBLE.getId()));

    public static final RegistryObject<RecipeType<HammerRecipe>> HAMMER = RECIPE_TYPES.register("hammer", () -> RecipeType.simple(ERecipeTypes.HAMMER.getId()));

    public static final RegistryObject<RecipeType<SieveRecipe>> SIEVE = RECIPE_TYPES.register("sieve", () -> RecipeType.simple(ERecipeTypes.SIEVE.getId()));
}
