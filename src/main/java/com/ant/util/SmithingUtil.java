package com.ant.util;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.recipe.input.SmithingRecipeInput; // <-- correct package!
import net.minecraft.server.world.ServerWorld;

public class SmithingUtil {
    public static ItemStack trySmithing(ServerWorld world, SimpleInventory inv) {
        SmithingRecipeInput input = new SmithingRecipeInput(
                inv.getStack(0),
                inv.getStack(1),
                inv.getStack(2)
        );

        ServerRecipeManager srm = (ServerRecipeManager) world.getRecipeManager();

        for (RecipeEntry<?> entry : srm.values()) {
            if (entry.value() instanceof SmithingRecipe recipe) {
                if (recipe.matches(input, world)) {
                    return recipe.craft(input, world.getRegistryManager());
                }
            }
        }
        return ItemStack.EMPTY;
    }
}
