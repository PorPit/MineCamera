package com.porpit.minecamera.item;

import com.porpit.minecamera.creativetab.CreativeTabsLoader;

import net.minecraft.item.Item;

public class ItemPhotoPaper extends Item{
	public ItemPhotoPaper(){
		super();
		this.setMaxStackSize(64);
		this.setUnlocalizedName("photo_paper");
		this.setCreativeTab(CreativeTabsLoader.tabMineCamera);
	}
}
