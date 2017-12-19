package com.porpit.minecamera.item;

import java.util.List;

import com.porpit.minecamera.creativetab.CreativeTabsLoader;
import com.porpit.minecamera.entity.EntityTripod;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
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
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {

		if (!worldIn.isRemote) {
			if(!side.equals(EnumFacing.UP)){
				playerIn.addChatComponentMessage(new ChatComponentTranslation("chat.tripod.mustup"));
				return true;
			}
			Entity entity = new EntityTripod(worldIn);
			entity.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			entity.rotationYaw = playerIn.rotationYaw;
			entity.rotationPitch = playerIn.rotationPitch;
			worldIn.spawnEntityInWorld(entity);
			// worldIn.updateEntities();
			stack.stackSize--;
			return true;
		}
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add("¡ì9" + I18n.format("lore.tripod.info"));
		tooltip.add("¡ì9" + I18n.format("lore.tripod.info2"));
	}
}
