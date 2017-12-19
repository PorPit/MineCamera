package com.porpit.minecamera.item;

import java.util.List;

import com.porpit.minecamera.creativetab.CreativeTabsLoader;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBattery extends Item {
	public ItemBattery() {
		super();
		this.setUnlocalizedName("battery");
		this.setCreativeTab(CreativeTabsLoader.tabMineCamera);
		this.setMaxDamage(9);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		String info = (stack.getMaxDamage() - stack.getItemDamage() + 1) + "/" + (stack.getMaxDamage() + 1);
		tooltip.add("¡ìa" + I18n.format("lore.battery.damage") + "¡ì7" + info);
		tooltip.add("¡ì8" + I18n.format("lore.battery.info"));
	}
}
