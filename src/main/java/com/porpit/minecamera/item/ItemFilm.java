package com.porpit.minecamera.item;

import java.text.SimpleDateFormat;
import java.util.List;

import com.porpit.minecamera.creativetab.CreativeTabsLoader;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFilm extends Item {
	public ItemFilm() {
		super();
		this.setUnlocalizedName("film");
		this.setCreativeTab(CreativeTabsLoader.tabMineCamera);
	}

	/*
	 * @Override
	 * 
	 * @SideOnly(Side.CLIENT) public void getSubItems(Item itemIn, CreativeTabs
	 * tab, List<ItemStack> subItems) { ItemStack film=new ItemStack(itemIn,1);
	 * NBTTagCompound filmdata=new NBTTagCompound(); filmdata.setString("pid",
	 * "none"); filmdata.setString("user", "none");
	 * film.setTagCompound(filmdata); subItems.add(film); }
	 */

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		String used = (I18n.format("lore.film.info.used"));
		String unused = (I18n.format("lore.film.info.unused"));

		// System.out.println(stack.hasTagCompound());
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("pid")) {
			String pid = stack.getTagCompound().getString("pid");
			tooltip.add("¡ì9" + I18n.format("lore.film.info") + "¡ìc" + used);
			if (pid.contains("%_%")) {
				String[] data = pid.split("%_%");
				String author = data[0];
				Long time = Long.valueOf(data[1]);
				tooltip.add("¡ì9" + I18n.format("lore.film.author") + "¡ìa" + author);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String d = format.format(time);
				tooltip.add("¡ì9" + I18n.format("lore.film.time") + "¡ìa" + d);
			}
		} else {
			tooltip.add("¡ì9" + I18n.format("lore.film.info") + "¡ìa" + unused);
		}
	}
}
