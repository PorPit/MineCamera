package com.porpit.minecamera.network;

import java.util.List;

import com.porpit.minecamera.entity.EntityTripod;
import com.porpit.minecamera.item.ItemLoader;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class MessageTripodFilmOut implements IMessage {

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class Handler implements IMessageHandler<MessageTripodFilmOut, IMessage> {
		@Override
		public IMessage onMessage(MessageTripodFilmOut message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				EntityPlayerMP player = ctx.getServerHandler().playerEntity;
				Entity entity = player.getEntityWorld()
						.getEntityByID(player.getEntityData().getInteger("renderViewCamera"));
				if (player != null && entity != null&&entity instanceof EntityTripod) {
					EntityTripod entityTripod=(EntityTripod)entity;
					IItemHandler items = entityTripod.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
							player.getHorizontalFacing());
					if (items.getStackInSlot(3) != null) {
						player.addChatComponentMessage(new ChatComponentTranslation("chat.minecamera.isouting"));
						return null;
					}
					if (items.getStackInSlot(0) == null) {
						player.addChatComponentMessage(new ChatComponentTranslation("chat.minecamera.nobettery"));
						return null;
					}
					if (items.getStackInSlot(1) == null) {
						player.addChatComponentMessage(new ChatComponentTranslation("chat.minecamera.nofilm"));
						return null;
					}
					if (items.getStackInSlot(2) != null) {
						player.addChatComponentMessage(new ChatComponentTranslation("chat.minecamera.hasfilmout"));
						return null;
					}
					if (items.getStackInSlot(1).hasTagCompound()
							&& items.getStackInSlot(1).getTagCompound().hasKey("pid")) {
						player.addChatComponentMessage((new ChatComponentTranslation("chat.minecamera.filmcantwrite")));
						return null;
					}
					items.getStackInSlot(0).damageItem(1, player);
					if (items.getStackInSlot(0).stackSize == 0) {
						items.extractItem(0, 1, false);
					}
					items.extractItem(1, 64, false);
					String imagename = ctx.getServerHandler().playerEntity.getName() + "%_%"
							+ System.currentTimeMillis();
					ItemStack itemfilm = new ItemStack(ItemLoader.itemFilm, 1);
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setString("pid", imagename);
					itemfilm.setTagCompound(nbt);
					/*
					 * if(!(items.getStackInSlot(3)==null)){
					 * items.getStackInSlot(3).stackSize=0; }
					 */
					items.insertItem(3, itemfilm, false);
					entityTripod.setBurnTime(0);
					player.addChatComponentMessage((new ChatComponentTranslation("chat.minecamera.success")));
					// effect
					double particlePosX = entityTripod.posX, particlePosY = entityTripod.posY + 1.35, particlePosZ = entityTripod.posZ;
					particlePosX = particlePosX + (-Math.sin(Math.toRadians(entityTripod.rotationYaw + 15)) * 0.7);
					particlePosZ = particlePosZ + (Math.cos(Math.toRadians(entityTripod.rotationYaw + 15)) * 0.7);
					List<EntityPlayer> listentity = entityTripod.getEntityWorld().getEntitiesWithinAABB(EntityPlayer.class,
							new AxisAlignedBB(entityTripod.posX - 16, entityTripod.posY - 16, entityTripod.posZ - 16, entityTripod.posX + 16,
									entityTripod.posY + 16, entityTripod.posZ + 16));
					// System.out.println("listsize:"+listentityTripod.size());
					if (listentity != null) {
						for (EntityPlayer i : listentity) {
							EntityPlayerMP entityplayermp = (EntityPlayerMP) i;
							entityplayermp.playSound("minecamera:minecamera.kacha", 1F, 1F);;
							MessageSpawnParticle message2 = new MessageSpawnParticle();
							message2.delay = 200;
							message2.typeid = EnumParticleTypes.FIREWORKS_SPARK.getParticleID();
							message2.PosX = particlePosX;
							message2.PosY = particlePosY;
							message2.PosZ = particlePosZ;
							message2.SpeedX = 0;
							message2.SpeedY = 0;
							message2.SpeedZ = 0;
							NetworkLoader.instance.sendTo(message2, entityplayermp);
						}
					}

					MessageImageSyncSave message2 = new MessageImageSyncSave();
					message2.imageName = imagename;
					NetworkLoader.instance.sendTo(message2, player);
					return null;
				}
			}
			return null;
		}
	}
}
