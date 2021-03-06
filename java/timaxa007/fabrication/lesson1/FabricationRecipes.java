package timaxa007.fabrication.lesson1;

import java.util.ArrayList;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class FabricationRecipes {

	public static class Recipe {
		public final ItemStack input, output;
		public Recipe(ItemStack input, ItemStack output) {
			this.input = input;
			this.output = output;
		}
	}

	private static final ArrayList<Recipe> list = new ArrayList<Recipe>();

	static {
		addRecipe(new ItemStack(Items.coal, 1, 0), new ItemStack(Items.coal, 2, 1));
		addRecipe(new ItemStack(Items.coal, 2, 1), new ItemStack(Items.coal, 1, 0));
	}

	public static boolean addRecipe(ItemStack input, ItemStack output) {
		return list.add(new Recipe(input, output));
	}

	public static Recipe getRecipe(ItemStack input) {
		if (list == null || list.size() == 0) return null;
		if (input == null) return null;

		for (Recipe recipe : list) {
			if (isMatch(recipe.input, input)) return recipe;
		}
		return null;
	}

	public static boolean isMatch(ItemStack itemStack, ItemStack input) {
		return itemStack == null || input == null ? false :
			itemStack.getItem() == input.getItem() &&
			(itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE || input.getItemDamage() == itemStack.getItemDamage()) &&
			(itemStack.hasTagCompound() ? ItemStack.areItemStackTagsEqual(itemStack, input) : true) &&
			0 <= input.stackSize - itemStack.stackSize;
	}

}
