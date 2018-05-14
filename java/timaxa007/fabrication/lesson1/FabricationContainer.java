package timaxa007.fabrication.lesson1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class FabricationContainer extends Container {

	final TileEntityFabrication tile_entity;

	public FabricationContainer(final InventoryPlayer inventory_player, final TileEntityFabrication tile_entity) {
		tile_entity.openInventory();

		this.addSlotToContainer(new Slot(tile_entity, 0, 50, 33){
			@Override
			public boolean isItemValid(ItemStack itemStack) {
				if (FabricationRecipes.getRecipe(itemStack) == null)
					return false;
				return true;
			}
		});
		this.addSlotToContainer(new Slot(tile_entity, 1, 110, 33){
			@Override
			public boolean isItemValid(ItemStack itemStack) {
				return false;
			}
		});

		int j;
		int k;

		for (j = 0; j < 3; ++j) {
			for (k = 0; k < 9; ++k) {
				this.addSlotToContainer(new Slot(inventory_player, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
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

			System.out.println(slotID + " - " + itemstack);

			if (slotID < tile_entity.getSizeInventory()) {
				if (!this.mergeItemStack(itemstack1, tile_entity.getSizeInventory() - 1, this.inventorySlots.size(), true))
					return null;
			} else {
				if (FabricationRecipes.getRecipe(itemstack1) == null)
					return null;
				if (!this.mergeItemStack(itemstack1, 0, tile_entity.getSizeInventory() - 1, false))
					return null;
			}

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

}
