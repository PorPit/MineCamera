package com.porpit.minecamera.item;

import java.util.List;

import com.porpit.minecamera.MineCamera;
import com.porpit.minecamera.creativetab.CreativeTabsLoader;
import com.porpit.minecamera.inventory.GuiElementLoader;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPictureBook extends Item {
	public ItemPictureBook() {
		super();
		this.setUnlocalizedName("picture_book");
		this.setCreativeTab(CreativeTabsLoader.tabMineCamera);
		this.setMaxStackSize(1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if (!worldIn.isRemote) {
			BlockPos pos = playerIn.getPosition();
			int id = GuiElementLoader.GUI_PICTURE_BOOK;
			playerIn.openGui(MineCamera.instance, id, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return itemStackIn;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		int num=0;
		if(stack.hasTagCompound())
		{
			if(stack.getTagCompound().hasKey("num")){
				num=stack.getTagCompound().getInteger("num");
			}else if(stack.getTagCompound().hasKey("listPid")&&!stack.getTagCompound().getString("listPid").equals("")){
				num=stack.getTagCompound().getString("listPid").split("%,%").length;
				stack.getTagCompound().setInteger("num", num);
			}
		}
		tooltip.add("¡ìa" + I18n.format("lore.picturebook.info") + "¡ì7" + ""+num);
	}
}
