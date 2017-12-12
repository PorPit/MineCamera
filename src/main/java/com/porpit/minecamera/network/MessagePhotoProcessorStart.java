package com.porpit.minecamera.network;

import com.porpit.minecamera.item.ItemLoader;
import com.porpit.minecamera.tileentity.TileEntityPhotoProcessor;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class MessagePhotoProcessorStart implements IMessage {
	public int dimid;
	public BlockPos bp;
	public String playername;

	@Override
	public void fromBytes(ByteBuf buf) {
		dimid = buf.readInt();
		int x, y, z;
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		bp = new BlockPos(x, y, z);
		int length = buf.readInt();
		byte namebyte[] = new byte[length];
		buf.readBytes(namebyte);
		playername = new String(namebyte);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(dimid);
		buf.writeInt(bp.getX());
		buf.writeInt(bp.getY());
		buf.writeInt(bp.getZ());
		buf.writeInt(playername.getBytes().length);
		buf.writeBytes(playername.getBytes());
	}

	public static class Handler implements IMessageHandler<MessagePhotoProcessorStart, IMessage> {
		@Override
		public IMessage onMessage(MessagePhotoProcessorStart message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				System.out.println(" ’µΩ£∫" + message.dimid + "," + message.bp);
				if (DimensionManager.getWorld(message.dimid).isBlockLoaded(message.bp) && DimensionManager
						.getWorld(message.dimid).getTileEntity(message.bp) instanceof TileEntityPhotoProcessor) {
					TileEntityPhotoProcessor te = (TileEntityPhotoProcessor) (DimensionManager.getWorld(message.dimid)
							.getTileEntity(message.bp));
					IItemHandler items = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
					items.getStackInSlot(0).damageItem(1, DimensionManager.getWorld(0).getMinecraftServer()
							.getPlayerList().getPlayerByUsername(message.playername));
					items.getStackInSlot(1).damageItem(1, DimensionManager.getWorld(0).getMinecraftServer()
							.getPlayerList().getPlayerByUsername(message.playername));
					items.extractItem(3, 1, false);
					ItemStack itemstack = new ItemStack(ItemLoader.itemPicture);
					String imagename = items.getStackInSlot(2).getTagCompound().getString("pid");
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setString("pid", imagename);
					itemstack.setTagCompound(nbt);
					if (items.getStackInSlot(5) != null) {
						items.extractItem(5, items.getStackInSlot(2).stackSize, false);
					}
					items.insertItem(5, itemstack, false);
					for (int i = 0; i <= 1; i++) {
						if (items.getStackInSlot(i).stackSize == 0) {
							items.extractItem(i, 1, false);
						}
					}
					te.setBurnTime(0);
				}
			}
			return null;
		}
	}

}
