package com.porpit.minecamera.network;

import com.porpit.minecamera.inventory.ContainerPictureBook;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessagePictureBookIndex implements IMessage {
	public int index;

	@Override
	public void fromBytes(ByteBuf buf) {
		index = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(index);
	}

	public static class Handler implements IMessageHandler<MessagePictureBookIndex, IMessage> {
		@Override
		public IMessage onMessage(MessagePictureBookIndex message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				EntityPlayerMP player = ctx.getServerHandler().player;
				if (player != null && player.openContainer != null
						&& player.openContainer instanceof ContainerPictureBook) {
					ContainerPictureBook contaner = (ContainerPictureBook) player.openContainer;
					if (message.index >= 0 && message.index < contaner.getTotalPictureNum()) {
						contaner.setIndex(message.index);
					}
				}
			}
			return null;
		}
	}
}
