package timaxa007.fabrication.lesson2a;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class FabricationContainer extends Container {

	final TileEntityFabrication tile_entity;

	public FabricationContainer(final InventoryPlayer inventory_player, final TileEntityFabrication tile_entity) {
		tile_entity.openInventory();

		int j;
		int k;

		for (j = 0; j < 3; ++j) {
			for (k = 0; k < 3; ++k) {
				this.addSlotToContainer(new Slot(tile_entity, k + j * 3, 14 + k * 18, 15 + j * 18));
			}
		}

		this.addSlotToContainer(new Slot(tile_entity, 9, 110, 33));

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

	public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(slotID);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotID < tile_entity.getSizeInventory()) {
				if (!this.mergeItemStack(itemstack1, tile_entity.getSizeInventory() - 1, this.inventorySlots.size(), true))
					return null;
			} else if (!this.mergeItemStack(itemstack1, 0, tile_entity.getSizeInventory() - 1, false))
				return null;

			if (itemstack1.stackSize == 0)
				slot.putStack(null);
			else
				slot.onSlotChanged();
		}

		return itemstack;
	}

	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		tile_entity.closeInventory();
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tile_entity.isUseableByPlayer(player);
	}

}
