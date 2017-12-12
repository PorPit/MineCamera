package com.porpit.minecamera.creativetab;

import com.porpit.minecamera.item.ItemLoader;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabsMineCamera extends CreativeTabs {
	public CreativeTabsMineCamera() {
		super("minecamera");
		this.setBackgroundImageName("minecamera.png");
	}

	@Override
	public Item getTabIconItem() {
		return ItemLoader.itemCamera;
	}

	@Override
	public boolean hasSearchBar() {
		return true;
	}
}