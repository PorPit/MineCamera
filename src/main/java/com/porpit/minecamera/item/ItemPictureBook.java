package com.porpit.minecamera.item;

import java.util.List;

import com.porpit.minecamera.MineCamera;
import com.porpit.minecamera.creativetab.CreativeTabsLoader;
import com.porpit.minecamera.inventory.GuiElementLoader;

import net.minecraft.client.resources.I18n;
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
import net.minecraft.util.text.TextFormatting;
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
		tooltip.add(TextFormatting.GREEN + I18n.format("lore.picturebook.info") + TextFormatting.GRAY + ""+num);
	}
}
