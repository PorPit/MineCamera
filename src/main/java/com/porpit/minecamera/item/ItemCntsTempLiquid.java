package com.porpit.minecamera.item;

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

public class ItemCntsTempLiquid extends Item {
	public ItemCntsTempLiquid() {
		super();
		this.setUnlocalizedName("cnst_temp_liquid");
		this.setCreativeTab(CreativeTabsLoader.tabMineCamera);
		this.setMaxDamage(4);
	}

/*	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		// itemStackIn.damageItem(1, playerIn);;
		return new ActionResult(EnumActionResult.PASS, itemStackIn);
	}*/

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		String info = (stack.getMaxDamage() - stack.getItemDamage() + 1) + "/" + (stack.getMaxDamage() + 1);
		tooltip.add(TextFormatting.GREEN + I18n.format("lore.liquid.damage") + TextFormatting.GRAY + info);
	}
}
