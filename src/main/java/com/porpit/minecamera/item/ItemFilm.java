package com.porpit.minecamera.item;

import java.text.SimpleDateFormat;
import java.util.List;

import com.porpit.minecamera.creativetab.CreativeTabsLoader;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
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
/*	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		// itemStackIn.getTagCompound().setString("pid",
		// "Player735_1510202549059");
		
		 * NBTTagCompound filmdata=new NBTTagCompound();
		 * filmdata.setString("id", "none"); NBTTagCompound nc=new
		 * NBTTagCompound(); itemStackIn.writeToNBT(nc); nc.setTag("tag",
		 * filmdata); itemStackIn.readFromNBT(nc);
		 
		System.out.println("0:" + hand);
		System.out.println("1:" + playerIn.getActiveHand());
		playerIn.setActiveHand(hand);
		if (playerIn instanceof EntityPlayerMP) {
			((EntityPlayerMP) playerIn).setActiveHand(hand);
		}
		System.out.println("2:" + playerIn.getActiveHand());
		System.out.println(itemStackIn.hasTagCompound());
		// String id =itemStackIn.getTagCompound().getString("pid");
		// String user =itemStackIn.getTagCompound().getString("user");
		// System.out.println("pid="+id);
		// System.out.println("user="+user);
		// itemStackIn.getTagCompound().setString("pid", "123456");
		// itemStackIn.getTagCompound().setString("user", "PorPit");
		return new ActionResult(EnumActionResult.PASS, itemStackIn);
	}*/

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		String used = (I18n.format("lore.film.info.used"));
		String unused = (I18n.format("lore.film.info.unused"));

		// System.out.println(stack.hasTagCompound());
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("pid")) {
			String pid = stack.getTagCompound().getString("pid");
			tooltip.add(TextFormatting.BLUE + I18n.format("lore.film.info") + TextFormatting.RED + used);
			if (pid.contains("%_%")) {
				String[] data = pid.split("%_%");
				String author = data[0];
				Long time = Long.valueOf(data[1]);
				tooltip.add(TextFormatting.BLUE + I18n.format("lore.film.author") + TextFormatting.GREEN + author);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String d = format.format(time);
				tooltip.add(TextFormatting.BLUE + I18n.format("lore.film.time") + TextFormatting.GREEN + d);
			}
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("lore.film.info") + TextFormatting.GREEN + unused);
		}
	}
}
