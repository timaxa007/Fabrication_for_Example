package timaxa007.fabrication.lesson3;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class FabricationRecepts {

	public static class Recept {
		public final ItemStack input[], output;
		public final int time;
		public Recept(ItemStack[] input, ItemStack output, int time) {
			this.input = input;
			this.output = output;
			this.time = time;
		}
	}

	private static final ArrayList<Recept> list = new ArrayList<Recept>();

	static {

		addRecept(new ItemStack[]{
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
				new ItemStack(Blocks.coal_block, 1, 0), 45);

		addRecept(new ItemStack[]{
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
				new ItemStack(Items.coal, 8, 0), 160);

	}

	public static boolean addRecept(ItemStack[] input, ItemStack output, int time) {
		return list.add(new Recept(input, output, time));
	}

	public static Recept getRecept(ItemStack[] input) {
		if (list == null || list.size() == 0) return null;
		if (input == null) return null;

		boolean next = true;

		for (Recept recept : list) {
			for (int i = 0; i < 9; ++i) {
				if (!isMatch(recept.input[i], input[i])) {
					next = true;
					break;
				} else {
					next = false;
				}
			}
			if (!next) return recept;
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
