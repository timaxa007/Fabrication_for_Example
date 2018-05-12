package timaxa007.fabrication.lesson0;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants.NBT;

public class TileEntityFabrication extends TileEntity implements IInventory {

	private final ItemStack[] inventory = new ItemStack[2];

	public TileEntityFabrication() {}

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
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slotID) {
		return inventory[slotID];
	}

	@Override
	public ItemStack decrStackSize(int slotID, int stackSize) {
		if (inventory[slotID] != null) {
			ItemStack itemstack;

			if (inventory[slotID].stackSize <= stackSize) {
				itemstack = inventory[slotID];
				inventory[slotID] = null;
				return itemstack;
			} else {
				itemstack = inventory[slotID].splitStack(stackSize);

				if (inventory[slotID].stackSize == 0)
					inventory[slotID] = null;

				return itemstack;
			}
		} else return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slotID) {
		if (inventory[slotID] != null) {
			ItemStack itemstack = inventory[slotID];
			inventory[slotID] = null;
			return itemstack;
		} else return null;
	}

	@Override
	public void setInventorySlotContents(int slotID, ItemStack itemStack) {
		inventory[slotID] = itemStack;
	}

	@Override
	public String getInventoryName() {
		return "inventory_fabrication";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
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

}
