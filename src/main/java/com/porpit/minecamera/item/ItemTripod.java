package com.porpit.minecamera.item;

import java.util.List;

import com.porpit.minecamera.creativetab.CreativeTabsLoader;
import com.porpit.minecamera.entity.EntityTripod;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTripod extends Item {
	public ItemTripod() {
		super();
		this.setUnlocalizedName("tripod");
		this.setCreativeTab(CreativeTabsLoader.tabMineCamera);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack=player.getHeldItem(hand);
		if (!worldIn.isRemote) {
			if(!facing.equals(EnumFacing.UP)){
				player.sendMessage(new TextComponentTranslation("chat.tripod.mustup"));
				return EnumActionResult.PASS;
			}
			Entity entity = new EntityTripod(worldIn);
			entity.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			entity.rotationYaw = player.rotationYaw;
			entity.rotationPitch = player.rotationPitch;
			worldIn.spawnEntity(entity);
			System.out.println("spawn");
			// worldIn.updateEntities();
			stack.shrink(1);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add(TextFormatting.BLUE + I18n.format("lore.tripod.info"));
		tooltip.add(TextFormatting.BLUE + I18n.format("lore.tripod.info2"));
	}
}
