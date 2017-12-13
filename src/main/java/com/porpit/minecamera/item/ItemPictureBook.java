package com.porpit.minecamera.item;

import com.porpit.minecamera.MineCamera;
import com.porpit.minecamera.creativetab.CreativeTabsLoader;
import com.porpit.minecamera.inventory.GuiElementLoader;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPictureBook extends Item {
	public ItemPictureBook() {
		super();
		this.setUnlocalizedName("picture_book");
		this.setCreativeTab(CreativeTabsLoader.tabMineCamera);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		playerIn.setActiveHand(hand);
		if (!worldIn.isRemote) {
			BlockPos pos = playerIn.getPosition();
			int id = GuiElementLoader.GUI_PICTURE_BOOK;
			playerIn.openGui(MineCamera.instance, id, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
}
