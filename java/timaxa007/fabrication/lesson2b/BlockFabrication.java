package timaxa007.fabrication.lesson2b;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFabrication extends Block implements ITileEntityProvider {

	public BlockFabrication() {
		super(Material.iron);
		this.setBlockName("block_fabrication");
		this.setBlockTextureName("fabrication:fabrication");
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileEntityFabrication();
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
		if (itemStack.hasDisplayName())
			((TileEntityFabrication)world.getTileEntity(x, y, z)).setInventoryName(itemStack.getDisplayName());
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote && world.getTileEntity(x, y, z) instanceof TileEntityFabrication)
			player.openGui(Fabrication.instance, 0, world, x, y, z);
		return true;
	}

}
