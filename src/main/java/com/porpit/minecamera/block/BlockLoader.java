package com.porpit.minecamera.block;

import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLoader {
	public static Block pictureFrame = new BlockPictureFrame();
	public static Block photoprocessor = new BlockPhotoProcessor();
	public static Block pictureFrameMultiple= new BlockPictureFrameMultiple();
	
	public BlockLoader(FMLPreInitializationEvent event) {
		// register(pictureFrame, "picture_frame");
		register(pictureFrame, "pictureframe");
		register(photoprocessor, "photoprocessor");
		register(pictureFrameMultiple, "pictureframe_multiple");
	}

	@SideOnly(Side.CLIENT)
	public static void registerRenders() {
		registerRender(pictureFrame);
		registerRender(photoprocessor);
		registerRender(pictureFrameMultiple);
	}

	private static void register(Block block, String name) {
		GameRegistry.registerBlock(block.setRegistryName(name));
	}

	@SideOnly(Side.CLIENT)
	private static void registerRender(Block block, int meta, String name) {
		ModelResourceLocation model = new ModelResourceLocation(block.getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, model);
	}

	@SideOnly(Side.CLIENT)
	private static void registerRender(Block block) {
		registerRender(block, 0, block.getRegistryName());
	}

	private static void register(Block block, ItemBlock itemBlock, String name) {
		GameRegistry.registerBlock(block.setRegistryName(name), (Class<? extends ItemBlock>) null);
		GameRegistry.registerItem(itemBlock.setRegistryName(name));
		GameData.getBlockItemMap().put(block, itemBlock);
	}
}
