package timaxa007.fabrication.lesson2a;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants.NBT;

public class TileEntityFabrication extends TileEntity implements ISidedInventory {

	private final ItemStack[]
			inventory_input = new ItemStack[9],
			inventory_output = new ItemStack[1];
	private static final int[]
			topSlots = new int[] {9},
			sideSlots = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
	private FabricationRecepts.Recept recept = null;
	private String custom_name;

	public TileEntityFabrication() {}

	@Override
	public void updateEntity() {
		if (worldObj.isRemote) return;
		ItemStack output = getStackInSlot(9);
		recept = FabricationRecepts.getRecept(inventory_input);

		if (recept != null) {

			if (output == null) {

				for (int i = 0; i < 9; ++i) {
					if (this.recept.input[i] == null) continue;
					ItemStack int_input = inventory_input[i];
					if (int_input != null) {
						if (int_input.getItem().hasContainerItem(int_input)) {
							setInventorySlotContents(i, int_input.getItem().getContainerItem(int_input));
						} else {
							int_input.stackSize -= this.recept.input[i].stackSize;
							if (int_input.stackSize <= 0)
								setInventorySlotContents(i, null);
							else
								setInventorySlotContents(i, int_input);
						}
					}
				}

				setInventorySlotContents(9, recept.output.copy());
			} else if (output.isItemEqual(recept.output) && ItemStack.areItemStackTagsEqual(output, recept.output) && recept.output.stackSize + output.stackSize <= 64) {
				output.stackSize += recept.output.stackSize;
				setInventorySlotContents(9, output);

				for (int i = 0; i < 9; ++i) {
					if (this.recept.input[i] == null) continue;
					ItemStack int_input = inventory_input[i];
					if (int_input != null) {
						if (int_input.getItem().hasContainerItem(int_input)) {
							setInventorySlotContents(i, int_input.getItem().getContainerItem(int_input));
						} else {
							int_input.stackSize -= this.recept.input[i].stackSize;
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
			return inventory_output[0];
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
		} else if (slotID == inventory_input.length && inventory_output[0] != null) {
			ItemStack itemstack;

			if (inventory_output[0].stackSize <= stackSize) {
				itemstack = inventory_output[0];
				inventory_output[0] = null;
				return itemstack;
			} else {
				itemstack = inventory_output[0].splitStack(stackSize);

				if (inventory_output[0].stackSize == 0)
					inventory_output[0] = null;

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
		} else if (slotID == inventory_input.length && inventory_output[0] != null) {
			ItemStack itemstack = inventory_output[0];
			inventory_output[0] = null;
			return itemstack;
		} else return null;
	}

	@Override
	public void setInventorySlotContents(int slotID, ItemStack itemStack) {
		if (slotID >= 0 && slotID < inventory_input.length)
			inventory_input[slotID] = itemStack;
		else
			inventory_output[0] = itemStack;
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
