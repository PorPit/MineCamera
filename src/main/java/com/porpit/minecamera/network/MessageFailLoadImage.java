package com.porpit.minecamera.network;

import com.porpit.minecamera.util.EnumFailLoadImage;
import com.porpit.minecamera.util.PictureFactory;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageFailLoadImage implements IMessage {
	public EnumFailLoadImage type;
	public String imagename;
	@Override
	public void fromBytes(ByteBuf buf) {
		type=EnumFailLoadImage.values()[buf.readInt()];
		int imagenamelength = buf.readInt();
		byte imagenamebyte[] = new byte[imagenamelength];
		buf.readBytes(imagenamebyte);
		imagename = new String(imagenamebyte);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(type.ordinal());
		buf.writeInt(imagename.length());
		buf.writeBytes(imagename.getBytes());
	}

	public static class Handler implements IMessageHandler<MessageFailLoadImage, IMessage> {
		@Override
		public IMessage onMessage(MessageFailLoadImage message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				PictureFactory.fildToLoadPicture.put(message.imagename, message.type);
				if(PictureFactory.lodingPicture.contains(message.imagename))
				{
					PictureFactory.lodingPicture.remove(message.imagename);
				}
			}
			return null;
		}
	}
}
