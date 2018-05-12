package timaxa007.fabrication.lesson2b;

import java.util.ArrayList;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class FabricationRecepts {

	public static class Recept {
		public final ItemStack input, output;
		public final int time;
		public Recept(ItemStack input, ItemStack output, int time) {
			this.input = input;
			this.output = output;
			this.time = time;
		}
	}

	private static final ArrayList<Recept> list = new ArrayList<Recept>();

	static {
		addRecept(new ItemStack(Items.coal, 1, 0), new ItemStack(Items.coal, 2, 1), 50);
		addRecept(new ItemStack(Items.coal, 2, 1), new ItemStack(Items.coal, 1, 0), 100);
	}

	public static boolean addRecept(ItemStack input, ItemStack output, int time) {
		return list.add(new Recept(input, output, time));
	}

	public static Recept getRecept(ItemStack input) {
		if (list == null || list.size() == 0) return null;
		if (input == null) return null;

		for (Recept recept : list) {
			if (isMatch(recept.input, input)) return recept;
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
