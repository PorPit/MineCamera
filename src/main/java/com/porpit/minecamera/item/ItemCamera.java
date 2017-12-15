package com.porpit.minecamera.item;

import java.util.List;

import com.porpit.minecamera.MineCamera;
import com.porpit.minecamera.creativetab.CreativeTabsLoader;
import com.porpit.minecamera.inventory.GuiElementLoader;
import com.porpit.minecamera.network.MessageImageSyncSave;
import com.porpit.minecamera.network.MessageSpawnParticle;
import com.porpit.minecamera.network.NetworkLoader;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemCamera extends Item {
	public ItemCamera() {
		super();
		this.setMaxStackSize(1);
		this.setUnlocalizedName("camera");
		this.setCreativeTab(CreativeTabsLoader.tabMineCamera);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target,
			EnumHand hand) {
		if (target instanceof EntityZombie) {
			target.setHeldItem(EnumHand.MAIN_HAND, stack);
		}
		return false;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		//System.out.println("1:"+playerIn.getActiveHand());
		playerIn.setActiveHand(hand);
		//System.out.println("2:"+playerIn.getActiveHand());
		if (playerIn.isSneaking()) {
			if (!worldIn.isRemote) {
				BlockPos pos = playerIn.getPosition();
				int id = GuiElementLoader.GUI_CAMERA;
				playerIn.openGui(MineCamera.instance, id, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}
		} else {
			// ÎïÆ·²Ù×÷
			if (!itemStackIn.hasTagCompound()) {
				itemStackIn.setTagCompound(new NBTTagCompound());
			}
			if (itemStackIn.getTagCompound().hasKey("filmOutCatch")) {
				if (!worldIn.isRemote)
					playerIn.addChatComponentMessage(new TextComponentTranslation("chat.minecamera.isouting"));
				return new ActionResult(EnumActionResult.PASS, itemStackIn);
			}
			if (!itemStackIn.getTagCompound().hasKey("filmSlot")) {
				if (!worldIn.isRemote)
					playerIn.addChatComponentMessage(new TextComponentTranslation("chat.minecamera.nofilm"));
				return new ActionResult(EnumActionResult.PASS, itemStackIn);
			}
			if (!itemStackIn.getTagCompound().hasKey("betterySlot")) {
				if (!worldIn.isRemote)
					playerIn.addChatComponentMessage(new TextComponentTranslation("chat.minecamera.nobettery"));
				return new ActionResult(EnumActionResult.PASS, itemStackIn);
			}
			if (itemStackIn.getTagCompound().hasKey("filmOutSlot")) {
				if (!worldIn.isRemote)
					playerIn.addChatComponentMessage(new TextComponentTranslation("chat.minecamera.hasfilmout"));
				return new ActionResult(EnumActionResult.PASS, itemStackIn);
			}
			ItemStack betteryStack = ItemStack
					.loadItemStackFromNBT(itemStackIn.getTagCompound().getCompoundTag("betterySlot"));
			if (betteryStack.getItemDamage() == betteryStack.getMaxDamage()) {
				itemStackIn.getTagCompound().removeTag("betterySlot");
				if (!worldIn.isRemote)
					playerIn.addChatComponentMessage(new TextComponentTranslation("chat.minecamera.betteryrunout"));
			} else {
				betteryStack.damageItem(1, playerIn);
				NBTTagCompound itemTag = new NBTTagCompound();
				betteryStack.writeToNBT(itemTag);
				itemStackIn.getTagCompound().setTag("betterySlot", itemTag);
			}
			ItemStack filmStack = ItemStack
					.loadItemStackFromNBT(itemStackIn.getTagCompound().getCompoundTag("filmSlot"));
			String createdpid = playerIn.getName() + "%_%" + System.currentTimeMillis();
			if (filmStack.hasTagCompound() && filmStack.getTagCompound().hasKey("pid")) {
				if (!worldIn.isRemote)
					playerIn.addChatComponentMessage((new TextComponentTranslation("chat.minecamera.filmcantwrite")));
				return new ActionResult(EnumActionResult.PASS, itemStackIn);
			} else {
				itemStackIn.getTagCompound().removeTag("filmSlot");
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("pid", createdpid);
				filmStack.setTagCompound(nbt);
				NBTTagCompound itemTag = new NBTTagCompound();
				filmStack.writeToNBT(itemTag);
				itemStackIn.getTagCompound().setTag("filmOutCatch", itemTag);
				if (!worldIn.isRemote) {
					MessageImageSyncSave message = new MessageImageSyncSave();
					message.imageName = createdpid;
					NetworkLoader.instance.sendTo(message, (EntityPlayerMP) playerIn);
				}
			}
			itemStackIn.getTagCompound().setInteger("burnTime", 0);
			// äÖÈ¾
			if (!worldIn.isRemote) {
				double particlePosX = playerIn.posX, particlePosY = playerIn.posY + 1.35, particlePosZ = playerIn.posZ;
				particlePosX = particlePosX + (-Math.sin(Math.toRadians(playerIn.rotationYaw + 15)) * 0.7)
						* Math.cos(Math.toRadians(playerIn.rotationPitch));
				particlePosY = particlePosY + (-Math.sin(Math.toRadians(playerIn.rotationPitch)) * 0.7);
				particlePosZ = particlePosZ + (Math.cos(Math.toRadians(playerIn.rotationYaw + 15)) * 0.7)
						* Math.cos(Math.toRadians(playerIn.rotationPitch));
				List<EntityPlayer> listentity = worldIn.getEntitiesWithinAABB(EntityPlayer.class,
						new AxisAlignedBB(playerIn.posX - 16, playerIn.posY - 16, playerIn.posZ - 16,
								playerIn.posX + 16, playerIn.posY + 16, playerIn.posZ + 16));
				if (listentity != null) {
					for (EntityPlayer i : listentity) {
						EntityPlayerMP entityplayermp = (EntityPlayerMP) i;
						entityplayermp.connection.sendPacket(new SPacketCustomSound("minecamera:minecamera.kacha",
								SoundCategory.PLAYERS, playerIn.posX, playerIn.posY, playerIn.posZ, 1.0F, 1.0F));
						MessageSpawnParticle message = new MessageSpawnParticle();
						message.delay = 200;
						message.typeid = EnumParticleTypes.FIREWORKS_SPARK.getParticleID();
						message.PosX = particlePosX;
						message.PosY = particlePosY;
						message.PosZ = particlePosZ;
						message.SpeedX = 0;
						message.SpeedY = 0;
						message.SpeedZ = 0;
						NetworkLoader.instance.sendTo(message, entityplayermp);
						/*
						 * entityplayermp.connection.sendPacket(new
						 * SPacketParticles(EnumParticleTypes.FIREWORKS_SPARK,
						 * EnumParticleTypes.FIREWORKS_SPARK.
						 * getShouldIgnoreRange(), (float) particlePosX, (float)
						 * particlePosY, (float) particlePosZ, 0F, 0F, 0F,
						 * 0.03F, 1));
						 */
					}
				}
			} 

			if (!worldIn.isRemote)
				playerIn.addChatComponentMessage((new TextComponentTranslation("chat.minecamera.success")));
		}

		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
}
