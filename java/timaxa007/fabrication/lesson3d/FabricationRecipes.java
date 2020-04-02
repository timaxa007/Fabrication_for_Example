package timaxa007.fabrication.lesson3d;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class FabricationRecipes {

	private static final ArrayList<Recipe> list = new ArrayList<Recipe>();

	static {

		addRecipe(
				new ItemStack[]{
						new ItemStack(Items.coal, 1, 1),
						new ItemStack(Items.coal, 1, 1),
						new ItemStack(Items.coal, 1, 1),

						new ItemStack(Items.coal, 1, 1),
						new ItemStack(Items.flint, 1, 0),
						new ItemStack(Items.coal, 1, 1),

						new ItemStack(Items.coal, 1, 1),
						new ItemStack(Items.coal, 1, 1),
						new ItemStack(Items.coal, 1, 1)
				},
				new ItemStack[]{
						new ItemStack(Blocks.coal_block, 1, 0)
				},
				45);

		addRecipe(
				new ItemStack[]{
						new ItemStack(Blocks.log, 1, OreDictionary.WILDCARD_VALUE),
						new ItemStack(Blocks.log, 1, OreDictionary.WILDCARD_VALUE),
						new ItemStack(Blocks.log, 1, OreDictionary.WILDCARD_VALUE),

						new ItemStack(Blocks.log, 1, OreDictionary.WILDCARD_VALUE),
						new ItemStack(Items.lava_bucket, 1, 0),
						new ItemStack(Blocks.log, 1, OreDictionary.WILDCARD_VALUE),

						new ItemStack(Blocks.log, 1, OreDictionary.WILDCARD_VALUE),
						new ItemStack(Blocks.log, 1, OreDictionary.WILDCARD_VALUE),
						new ItemStack(Blocks.log, 1, OreDictionary.WILDCARD_VALUE)
				},
				new ItemStack[]{
						new ItemStack(Items.coal, 8, 1),
						new ItemStack(Items.coal, 1, 0)
				},
				160);

		addRecipe(
				new Recipe.Input[]{
						new Recipe.Input(new ItemStack(Items.iron_axe, 1, OreDictionary.WILDCARD_VALUE), true),
						new Recipe.Input(new ItemStack(Blocks.planks, 1, 0), false)
				},
				new Recipe.Output[]{
						new Recipe.Output(new ItemStack(Blocks.wooden_slab, 2, 0), 1F),	//100%
						new Recipe.Output(new ItemStack(Items.stick, 1, 0), 0.75F),		//75%
						new Recipe.Output(new ItemStack(Items.stick, 1, 0), 0.5F),		//50%
						new Recipe.Output(new ItemStack(Blocks.torch, 1, 0), 0.1F)		//10%
				},
				45);

	}

	public static boolean addRecipe(Recipe.Input[] input, Recipe.Output[] output, int time) {
		return list.add(new Recipe(input, output, time));
	}

	public static boolean addRecipe(ItemStack[] input, ItemStack[] output, int time) {
		Recipe.Input[] in = new Recipe.Input[input.length];
		Recipe.Output[] out = new Recipe.Output[output.length];
		for (int i = 0; i < input.length; i++) in[i] = new Recipe.Input(input[i], false);
		for (int i = 0; i < output.length; i++) out[i] = new Recipe.Output(output[i], 1F);
		return list.add(new Recipe(in, out, time));
	}

	public static Recipe getRecipe(ItemStack[] input) {
		if (list == null || list.size() == 0) return null;
		if (input == null) return null;

		boolean next = true;

		for (Recipe recipe : list) {
			for (int i = 0; i < input.length; ++i) {
				if (recipe.input.length <= i) break;
				if (!recipe.input[i].isMatch(recipe.input[i].itemStack, input[i])) {
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

	public static class Recipe {

		public final Recipe.Input[] input;
		public final Recipe.Output[] output;
		public final int time;

		public Recipe(Recipe.Input[] input, Recipe.Output[] output, int time) {
			this.input = input;
			this.output = output;
			this.time = time;
		}

		public static class Input {

			final ItemStack itemStack;
			final boolean isDamage;

			public Input(ItemStack itemStack, boolean isDamage) {
				this.itemStack = itemStack;
				this.isDamage = isDamage;
			}

			public boolean isMatch(ItemStack itemStack, ItemStack input) {
				return itemStack == null || input == null ? false :
					itemStack.getItem() == input.getItem() &&
					(itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE || isDamage || input.getItemDamage() == itemStack.getItemDamage()) &&
					(itemStack.hasTagCompound() ? ItemStack.areItemStackTagsEqual(itemStack, input) : true) &&
					(isDamage || 0 <= input.stackSize - itemStack.stackSize);
			}

		}

		public static class Output {

			final ItemStack itemStack;
			final float chance;

			public Output(ItemStack itemStack, float chance) {
				this.itemStack = itemStack;
				this.chance = chance;
			}

		}

	}

}
