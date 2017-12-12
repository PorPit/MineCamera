package com.porpit.minecamera.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CreativeTabsLoader {
	public static CreativeTabs tabMineCamera;

	public CreativeTabsLoader(FMLPreInitializationEvent event) {
		tabMineCamera = new CreativeTabsMineCamera();
	}
}
