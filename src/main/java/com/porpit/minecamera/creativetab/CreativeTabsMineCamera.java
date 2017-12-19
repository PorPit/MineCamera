package com.porpit.minecamera.creativetab;

import com.porpit.minecamera.item.ItemLoader;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabsMineCamera extends CreativeTabs {
	public CreativeTabsMineCamera() {
		super("minecamera");
		this.setBackgroundImageName("minecamera.png");
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(ItemLoader.itemCamera);
	}

	@Override
	public boolean hasSearchBar() {
		return true;
	}
}