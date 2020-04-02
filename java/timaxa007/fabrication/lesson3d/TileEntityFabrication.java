package timaxa007.fabrication.lesson3d;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants.NBT;
import timaxa007.fabrication.lesson3d.FabricationRecipes.Recipe.Output;

public class TileEntityFabrication extends TileEntity implements ISidedInventory {

	private final ItemStack[]
			inventory_input = new ItemStack[9],
			inventory_output = new ItemStack[9];
	private static final int[]
			topSlots = new int[] {9, 10, 11, 12, 13, 14, 15, 16, 17},
			sideSlots = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
	private FabricationRecipes.Recipe recipe = null;
	private String custom_name;
	public int time = 0, time_max = 0;
	private final ArrayList<ItemStack> temp = new ArrayList<ItemStack>();

	public TileEntityFabrication() {}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote) return;

		if (time == 0) {
			if (recipe != null) {

				if (!temp.isEmpty()) {
					for (int i = 0; i < temp.size(); ++i) {
						if (addOutput(temp.get(i))) temp.remove(i);
					}
					if (temp.isEmpty()) {
						recipe = null;
						time_max = 0;
					}
				}

			} else {
				recipe = FabricationRecipes.getRecipe(inventory_input);

				if (recipe != null) {
					time_max = time = recipe.time;

					if (recipe.output != null && recipe.output.length > 0) {
						for (Output output : recipe.output) {
							if (output.chance == 1F || output.chance >= worldObj.rand.nextFloat())
								temp.add(output.itemStack.copy());
						}
					}

					for (int i = 0; i < inventory_input.length; ++i) {
						if (recipe.input.length <= i) break;
						if (this.recipe.input[i] == null) continue;
						ItemStack int_input = inventory_input[i];
						if (int_input != null) {
							if (int_input.getItem().hasContainerItem(int_input)) {
								setInventorySlotContents(i, int_input.getItem().getContainerItem(int_input));
							} else {
								if (this.recipe.input[i].isDamage) {
									int_input.setItemDamage(int_input.getItemDamage() + 1);
									if (int_input.getItemDamage() >= int_input.getMaxDamage())
										setInventorySlotContents(i, null);
									
								} else {
									int_input.stackSize -= this.recipe.input[i].itemStack.stackSize;
									if (int_input.stackSize <= 0)
										setInventorySlotContents(i, null);
									else
										setInventorySlotContents(i, int_input);
								}
							}
						}
					}

				}
			}
		} else {
			--time;
		}

	}

	private boolean addOutput(ItemStack itemStack) {
		for (int i = 0; i < inventory_output.length; ++i) {
			ItemStack output = inventory_output[i];
			if (output != null && output.isItemEqual(itemStack) && ItemStack.areItemStackTagsEqual(output, itemStack) && itemStack.stackSize + output.stackSize <= 64) {
				output.stackSize += itemStack.stackSize;
				setInventorySlotContents(i + 9, output);
				return true;
			} else if (output == null) {
				setInventorySlotContents(i + 9, itemStack);
				return true;
			}
		}
		return false;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < getSizeInventory(); ++i) {
			if (getStackInSlot(i) == null) continue;
			NBTTagCompound slot_nbt = new NBTTagCompound();
			getStackInSlot(i).writeToNBT(slot_nbt);
			slot_nbt.setByte("Slot", (byte)i);
			list.appendTag(slot_nbt);
		}
		nbt.setTag("Inventory", list);
		if (hasCustomInventoryName()) nbt.setString("CustomName", getInventoryName());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("Inventory", NBT.TAG_LIST)) {
			NBTTagList list = nbt.getTagList("Inventory", NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); ++i) {
				NBTTagCompound slot_nbt = list.getCompoundTagAt(i);
				setInventorySlotContents((slot_nbt.getByte("Slot") & 255), ItemStack.loadItemStackFromNBT(slot_nbt));
			}
		}
		if (nbt.hasKey("CustomName", NBT.TAG_STRING)) setInventoryName(nbt.getString("CustomName"));
	}

	@Override
	public int getSizeInventory() {
		return inventory_input.length + inventory_output.length;
	}

	@Override
	public ItemStack getStackInSlot(int slotID) {
		if (slotID >= 0 && slotID < inventory_input.length)
			return inventory_input[slotID];
		else
			return inventory_output[slotID - inventory_input.length];
	}

	@Override
	public ItemStack decrStackSize(int slotID, int stackSize) {
		if (slotID >= 0 && slotID < inventory_input.length && inventory_input[slotID] != null) {
			ItemStack itemstack;

			if (inventory_input[slotID].stackSize <= stackSize) {
				itemstack = inventory_input[slotID];
				inventory_input[slotID] = null;
				return itemstack;
			} else {
				itemstack = inventory_input[slotID].splitStack(stackSize);

				if (inventory_input[slotID].stackSize == 0)
					inventory_input[slotID] = null;

				return itemstack;
			}
		} else if (slotID >= inventory_input.length && inventory_output[slotID - inventory_input.length] != null) {
			ItemStack itemstack;

			if (inventory_output[slotID - inventory_input.length].stackSize <= stackSize) {
				itemstack = inventory_output[slotID - inventory_input.length];
				inventory_output[slotID - inventory_input.length] = null;
				return itemstack;
			} else {
				itemstack = inventory_output[slotID - inventory_input.length].splitStack(stackSize);

				if (inventory_output[slotID - inventory_input.length].stackSize == 0)
					inventory_output[slotID - inventory_input.length] = null;

				return itemstack;
			}
		} else return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slotID) {
		if (slotID >= 0 && slotID < inventory_input.length && inventory_input[slotID] != null) {
			ItemStack itemstack = inventory_input[slotID];
			inventory_input[slotID] = null;
			return itemstack;
		} else if (slotID >= inventory_input.length && inventory_output[slotID - inventory_input.length] != null) {
			ItemStack itemstack = inventory_output[slotID - inventory_input.length];
			inventory_output[slotID - inventory_input.length] = null;
			return itemstack;
		} else return null;
	}

	@Override
	public void setInventorySlotContents(int slotID, ItemStack itemStack) {
		if (slotID >= 0 && slotID < inventory_input.length)
			inventory_input[slotID] = itemStack;
		else
			inventory_output[slotID - inventory_input.length] = itemStack;
	}

	public void setInventoryName(String name) {
		custom_name = name;
	}

	@Override
	public String getInventoryName() {
		return hasCustomInventoryName() ? custom_name : "inventory_fabrication";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return custom_name != null && custom_name.length() > 0;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemStack) {
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return side == 0 ? topSlots : sideSlots;
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemStack, int side) {
		return side != 0;
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemStack, int side) {
		return side == 0;
	}

}
