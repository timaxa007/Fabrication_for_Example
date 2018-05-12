package timaxa007.fabrication.lesson3c;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class HandlerGui implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile_entity = world.getTileEntity(x, y, z);
		switch(ID) {
		case 0:return new FabricationContainer(player.inventory, (TileEntityFabrication)tile_entity);
		default:return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile_entity = world.getTileEntity(x, y, z);
		switch(ID) {
		case 0:return new FabricationGuiContainer(player.inventory, (TileEntityFabrication)tile_entity);
		default:return null;
		}
	}

}
