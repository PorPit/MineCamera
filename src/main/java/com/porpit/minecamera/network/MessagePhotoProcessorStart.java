package com.porpit.minecamera.network;

import java.util.List;

import com.porpit.minecamera.inventory.ContainerPhotoProcessor;
import com.porpit.minecamera.item.ItemLoader;
import com.porpit.minecamera.item.ItemTripod;
import com.porpit.minecamera.tileentity.TileEntityPhotoProcessor;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class MessagePhotoProcessorStart implements IMessage {

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class Handler implements IMessageHandler<MessagePhotoProcessorStart, IMessage> {
		@Override
		public IMessage onMessage(MessagePhotoProcessorStart message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				//System.out.println(" ’µΩ£∫" + message.dimid + "," + message.bp);
				EntityPlayerMP playerMP=ctx.getServerHandler().player;
				if (playerMP!=null&&playerMP.openContainer!=null&&playerMP.openContainer instanceof ContainerPhotoProcessor&&((ContainerPhotoProcessor)playerMP.openContainer).getTileEntity()!=null) {
						ContainerPhotoProcessor container=(ContainerPhotoProcessor) playerMP.openContainer;
						TileEntityPhotoProcessor te = container.getTileEntity();
						IItemHandler items = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
						items.getStackInSlot(0).damageItem(1, playerMP);
						items.getStackInSlot(1).damageItem(1, playerMP);
						items.extractItem(3, 1, false);
						ItemStack itemstack = new ItemStack(ItemLoader.itemPicture);
						String imagename = items.getStackInSlot(2).getTagCompound().getString("pid");
						NBTTagCompound nbt = new NBTTagCompound();
						nbt.setString("pid", imagename);
						itemstack.setTagCompound(nbt);
						if (items.getStackInSlot(5) != null) {
							items.extractItem(5, items.getStackInSlot(2).getCount(), false);
						}
						items.insertItem(5, itemstack, false);
						for (int i = 0; i <= 1; i++) {
							if (items.getStackInSlot(i).getCount() == 0) {
								items.extractItem(i, 1, false);
							}
						}
						te.setBurnTime(0);
						List<EntityPlayer> listentity = te.getWorld().getEntitiesWithinAABB(EntityPlayer.class,
								new AxisAlignedBB(te.getPos().getX() - 16, te.getPos().getY() - 16, te.getPos().getZ() - 16, te.getPos().getX() + 16,
										te.getPos().getY() + 16, te.getPos().getZ() + 16));
						if (listentity != null) {
							for (EntityPlayer i : listentity) {
								EntityPlayerMP entityplayermp = (EntityPlayerMP) i;
								entityplayermp.connection.sendPacket(new SPacketCustomSound("minecamera:minecamera.output",
										SoundCategory.PLAYERS, te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), 1.0F, 1.0F));
							}
						}
						//TODO
						/*if(!playerMP.hasAchievement(AchievementLoader.craftpicture)){
							playerMP.addStat(AchievementLoader.crafttripod);
						}*/
				}
			}
			return null;
		}
	}

}
