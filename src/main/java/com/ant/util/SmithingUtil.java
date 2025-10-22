package com.ant.util;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.server.world.ServerWorld;

public class SmithingUtil {

    public static boolean trySmithing(ServerWorld world, SimpleInventory inv) {
        SmithingRecipeInput input = new SmithingRecipeInput(
                inv.getStack(0),
                inv.getStack(1),
                inv.getStack(2)
        );

        ServerRecipeManager srm = (ServerRecipeManager) world.getRecipeManager();
        for (RecipeEntry<?> entry : srm.values()) {
            if (entry.value() instanceof SmithingRecipe recipe) {
                if (recipe.matches(input, world)) {
                    ItemStack result = recipe.craft(input, world.getRegistryManager());
                    inv.setStack(3, result);
                    return true;
                }
            }
        }
        inv.setStack(3, ItemStack.EMPTY);
        return false;
    }
}
