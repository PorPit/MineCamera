package com.porpit.minecamera.item;

import java.text.SimpleDateFormat;
import java.util.List;

import com.porpit.minecamera.creativetab.CreativeTabsLoader;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPicture extends Item{
	public ItemPicture(){
		super();
        this.setUnlocalizedName("picture");
        this.setCreativeTab(CreativeTabsLoader.tabMineCamera);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		System.out.println(itemStackIn.getTagCompound());
		return new ActionResult(EnumActionResult.PASS, itemStackIn);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		// System.out.println(stack.hasTagCompound());
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("pid")){
			String pid=stack.getTagCompound().getString("pid");
			if(pid.contains("%_%")){
				String d="null";
				try {
					String[] data=pid.split("%_%");
					String author = data[0];
					Long time=Long.valueOf(data[1]);
					tooltip.add(TextFormatting.BLUE + I18n.format("lore.picture.author")+ TextFormatting.GREEN + author);
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					d = format.format(time);
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				tooltip.add(TextFormatting.BLUE + I18n.format("lore.picture.time") + TextFormatting.GREEN + d);
			}else{
				tooltip.add(TextFormatting.BLUE + I18n.format("lore.picture.noauthor"));
			}
		}
		else{
			tooltip.add(TextFormatting.RED + I18n.format("lore.picture.null"));
		}
	}
}
