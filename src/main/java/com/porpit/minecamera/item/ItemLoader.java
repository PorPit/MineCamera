package com.porpit.minecamera.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLoader {
	public static Item itemCamera = new ItemCamera();
	public static Item itemPicture = new ItemPicture();
	public static Item itemBattery = new ItemBattery();
	public static Item itemFilm = new ItemFilm();
	public static Item itemCntsTempLiquid = new ItemCntsTempLiquid();
	public static Item itemDevelopingAgent = new ItemDevelopingAgent();
	public static Item itemTripod = new ItemTripod();
	public static Item itemPhotoPaper= new ItemPhotoPaper();
	public static Item itemPictureBook =new ItemPictureBook();
	public static ItemArmor itemGlassesHelmet = new ItemGlassesHelmet();

	public ItemLoader(FMLPreInitializationEvent event) {
		register(itemCamera, "camera");
		register(itemTripod, "tripod");
		register(itemBattery, "battery");
		register(itemFilm, "film");
		register(itemPhotoPaper, "photo_paper");
		register(itemPictureBook, "picture_book");
		register(itemCntsTempLiquid, "cnts_temp_liquid");
		register(itemDevelopingAgent, "developing_agent");
		register(itemPicture, "picture");
		register(itemGlassesHelmet, "glasses_helmet");
	}

	@SideOnly(Side.CLIENT)
	public static void registerRenders() {
		registerRender(itemCamera);
		registerRender(itemTripod);
		registerRender(itemBattery);
		registerRender(itemFilm);
		registerRender(itemCntsTempLiquid);
		registerRender(itemDevelopingAgent);
		registerRender(itemPhotoPaper);
		registerRender(itemPictureBook);
		registerRender(itemPicture);
		registerRender(itemGlassesHelmet);
	}

	private static void register(Item item, String name) {
		GameRegistry.register(item.setRegistryName(name));
	}

	@SideOnly(Side.CLIENT)
	private static void registerRender(Item item, int meta, String name) {
		ModelResourceLocation model = new ModelResourceLocation(name, "inventory");
		ModelLoader.setCustomModelResourceLocation(item, meta, model);
	}

	@SideOnly(Side.CLIENT)
	private static void registerRender(Item item) {
		System.out.println(item.getRegistryName().toString());
		registerRender(item, 0, item.getRegistryName().toString());
	}
}
