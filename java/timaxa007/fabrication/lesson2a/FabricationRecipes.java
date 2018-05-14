package timaxa007.fabrication.lesson2a;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class FabricationRecipes {

	public static class Recipe {
		public final ItemStack input[], output;
		public Recipe(ItemStack[] input, ItemStack output) {
			this.input = input;
			this.output = output;
		}
	}

	private static final ArrayList<Recipe> list = new ArrayList<Recipe>();

	static {

		addRecipe(new ItemStack[]{
				new ItemStack(Items.coal, 1, 1),
				new ItemStack(Items.coal, 1, 1),
				new ItemStack(Items.coal, 1, 1),

				new ItemStack(Items.coal, 1, 1),
				new ItemStack(Items.flint, 1, 0),
				new ItemStack(Items.coal, 1, 1),

				new ItemStack(Items.coal, 1, 1),
				new ItemStack(Items.coal, 1, 1),
				new ItemStack(Items.coal, 1, 1),
		},
				new ItemStack(Blocks.coal_block, 1, 0));

		addRecipe(new ItemStack[]{
				new ItemStack(Blocks.log, 1, OreDictionary.WILDCARD_VALUE),
				new ItemStack(Blocks.log, 1, OreDictionary.WILDCARD_VALUE),
				new ItemStack(Blocks.log, 1, OreDictionary.WILDCARD_VALUE),

				new ItemStack(Blocks.log, 1, OreDictionary.WILDCARD_VALUE),
				new ItemStack(Items.lava_bucket, 1, 0),
				new ItemStack(Blocks.log, 1, OreDictionary.WILDCARD_VALUE),

				new ItemStack(Blocks.log, 1, OreDictionary.WILDCARD_VALUE),
				new ItemStack(Blocks.log, 1, OreDictionary.WILDCARD_VALUE),
				new ItemStack(Blocks.log, 1, OreDictionary.WILDCARD_VALUE),
		},
				new ItemStack(Items.coal, 8, 0));

	}

	public static boolean addRecipe(ItemStack[] input, ItemStack output) {
		return list.add(new Recipe(input, output));
	}

	public static Recipe getRecipe(ItemStack[] input) {
		if (list == null || list.size() == 0) return null;
		if (input == null) return null;

		boolean next = true;

		for (Recipe recipe : list) {
			for (int i = 0; i < 9; ++i) {
				if (!isMatch(recipe.input[i], input[i])) {
					next = true;
					break;
				} else {
					next = false;
				}
			}
			if (!next) return recipe;
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
