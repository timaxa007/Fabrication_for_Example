package timaxa007.fabrication.lesson3;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

@Mod(modid = Fabrication.MODID, name = Fabrication.NAME, version = Fabrication.VERSION)
public final class Fabrication {

	public static final String
	MODID = "fabrication3",
	NAME = "Fabrication3",
	VERSION = "0.3";

	@Mod.Instance(MODID) public static Fabrication instance;

	public static Block block_fabrication;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		block_fabrication = new BlockFabrication();
		GameRegistry.registerBlock(block_fabrication, "block_fabrication3");
		GameRegistry.registerTileEntity(TileEntityFabrication.class, "TileEntityFabrication3");
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new HandlerGui());
	}

}
