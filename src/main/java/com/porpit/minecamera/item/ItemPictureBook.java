package com.porpit.minecamera.item;

import com.porpit.minecamera.MineCamera;
import com.porpit.minecamera.creativetab.CreativeTabsLoader;
import com.porpit.minecamera.inventory.GuiElementLoader;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
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
		if (playerIn.isSneaking()) {
			if (!worldIn.isRemote) {
				BlockPos pos = playerIn.getPosition();
				int id = GuiElementLoader.GUI_PICTURE_BOOK;
				playerIn.openGui(MineCamera.instance, id, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}
		}
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}
}

