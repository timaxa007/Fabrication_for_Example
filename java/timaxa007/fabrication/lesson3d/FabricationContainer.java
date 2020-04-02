package timaxa007.fabrication.lesson3d;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class FabricationContainer extends Container {

	final TileEntityFabrication tile_entity;
	private int last_time, last_time_max;

	public FabricationContainer(final InventoryPlayer inventory_player, final TileEntityFabrication tile_entity) {
		tile_entity.openInventory();

		int j;
		int k;

		for (j = 0; j < 3; ++j) {
			for (k = 0; k < 3; ++k) {
				this.addSlotToContainer(new Slot(tile_entity, k + j * 3, 14 + k * 18, 15 + j * 18));
			}
		}

		for (j = 0; j < 3; ++j) {
			for (k = 0; k < 3; ++k) {
				this.addSlotToContainer(new Slot(tile_entity, 9 + k + j * 3, 110 + k * 18, 15 + j * 18){
					@Override
					public boolean isItemValid(ItemStack itemStack) {
						return false;
					}
				});
			}
		}

		for (j = 0; j < 3; ++j) {
			for (k = 0; k < 9; ++k) {
				this.addSlotToContainer(new Slot(inventory_player, 9 + k + j * 9, 8 + k * 18, 84 + j * 18));
			}
		}

		for (j = 0; j < 9; ++j) {
			this.addSlotToContainer(new Slot(inventory_player, j, 8 + j * 18, 142));
		}

		this.tile_entity = tile_entity;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(slotID);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotID < tile_entity.getSizeInventory()) {
				if (!this.mergeItemStack(itemstack1, tile_entity.getSizeInventory() - 9, this.inventorySlots.size(), true))
					return null;
			} else if (!this.mergeItemStack(itemstack1, 0, tile_entity.getSizeInventory() - 9, false))
				return null;

			if (itemstack1.stackSize == 0)
				slot.putStack(null);
			else
				slot.onSlotChanged();
		}

		return itemstack;
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		tile_entity.closeInventory();
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tile_entity.isUseableByPlayer(player);
	}

	@Override
	public void addCraftingToCrafters(ICrafting ic) {
		super.addCraftingToCrafters(ic);
		ic.sendProgressBarUpdate(this, 0, this.tile_entity.time);
		ic.sendProgressBarUpdate(this, 1, this.tile_entity.time_max);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < this.crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting)this.crafters.get(i);

			if (this.last_time != this.tile_entity.time)
				icrafting.sendProgressBarUpdate(this, 0, this.tile_entity.time);

			if (this.last_time_max != this.tile_entity.time_max)
				icrafting.sendProgressBarUpdate(this, 1, this.tile_entity.time_max);

		}

		this.last_time = this.tile_entity.time;
		this.last_time_max = this.tile_entity.time_max;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int value) {
		switch(id) {
		case 0:
			this.tile_entity.time = value;
			break;
		case 1:
			this.tile_entity.time_max = value;
			break;
		default:break;
		}
	}

}
