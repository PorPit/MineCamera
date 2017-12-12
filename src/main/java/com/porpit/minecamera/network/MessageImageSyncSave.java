package com.porpit.minecamera.network;

import com.porpit.minecamera.util.SaveImageThread;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageImageSyncSave implements IMessage {
	public String imageName;

	@Override
	public void fromBytes(ByteBuf buf) {
		int namelength = buf.readInt();
		byte namebyte[] = new byte[namelength];
		buf.readBytes(namebyte);
		imageName = new String(namebyte);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(imageName.getBytes().length);
		buf.writeBytes(imageName.getBytes());
	}

	public static class Handler implements IMessageHandler<MessageImageSyncSave, IMessage> {
		@Override
		public IMessage onMessage(MessageImageSyncSave message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				SaveImageThread thread = new SaveImageThread(message.imageName);
				thread.start();
			}
			return null;
		}

	}
}
