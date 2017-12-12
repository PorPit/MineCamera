package com.porpit.minecamera.common;

import com.porpit.minecamera.achievement.AchievementLoader;
import com.porpit.minecamera.block.BlockLoader;
import com.porpit.minecamera.crafting.CraftingLoader;
import com.porpit.minecamera.creativetab.CreativeTabsLoader;
import com.porpit.minecamera.entity.EntityLoader;
import com.porpit.minecamera.inventory.GuiElementLoader;
import com.porpit.minecamera.item.ItemLoader;
import com.porpit.minecamera.network.NetworkLoader;
import com.porpit.minecamera.tileentity.TileEntityLoader;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommonProxy {
	public void preInit(FMLPreInitializationEvent event) {
		new CreativeTabsLoader(event);
		new BlockLoader(event);
		new ItemLoader(event);
		new TileEntityLoader(event);
		new EventLoader();
		new NetworkLoader(event);
		new GuiElementLoader();
		new EntityLoader();
		new CraftingLoader();
		new AchievementLoader();
	}

	public void init(FMLInitializationEvent event) {

	}

	public void postInit(FMLPostInitializationEvent event) {

	}

	public void serverStarting(FMLServerStartingEvent event) {
	}
}
