package thedarkcolour.exnihiloreborn.recipe.crucible;

import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import thedarkcolour.exnihiloreborn.recipe.CodecUtil;

import javax.annotation.Nullable;

public class FinishedCrucibleRecipe implements IFinishedRecipe {
    private final ResourceLocation id;
    private final IRecipeSerializer<?> serializer;
    private final Ingredient ingredient;
    private final FluidStack fluidStack;

    public FinishedCrucibleRecipe(ResourceLocation id, IRecipeSerializer<?> serializer, Ingredient ingredient, Fluid fluid, int amount) {
        this(id, serializer, ingredient, new FluidStack(fluid, amount));
    }

    public FinishedCrucibleRecipe(ResourceLocation id, IRecipeSerializer<?> serializer, Ingredient ingredient, FluidStack fluidStack) {
        this.id = id;
        this.serializer = serializer;
        this.ingredient = ingredient;
        this.fluidStack = fluidStack;
    }


    @Override
    public void serializeRecipeData(JsonObject json) {
        json.add("ingredient", ingredient.toJson());
        json.add("fluid", CodecUtil.encode(FluidStack.CODEC, fluidStack));
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getType() {
        return serializer;
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
        return null;
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
        return null;
    }
}
