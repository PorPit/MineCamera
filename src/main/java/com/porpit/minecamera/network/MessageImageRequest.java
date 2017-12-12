package com.porpit.minecamera.network;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.porpit.minecamera.MineCamera;
import com.porpit.minecamera.util.EnumFailLoadImage;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageImageRequest implements IMessage {
	public String filename;

	@Override
	public void fromBytes(ByteBuf buf) {
		int filenamelength = buf.readInt();
		byte filenamebyte[] = new byte[filenamelength];
		buf.readBytes(filenamebyte);
		filename = new String(filenamebyte);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(filename.getBytes().length);
		buf.writeBytes(filename.getBytes());
	}

	public static class Handler implements IMessageHandler<MessageImageRequest, IMessage> {
		@Override
		public IMessage onMessage(MessageImageRequest message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				File file = new File(MineCamera.ServerCatchFile + message.filename + ".jpeg");
				if (file.exists()) {
					try {
						BufferedImage image = ImageIO.read(file);
						MessageImage messageimage = new MessageImage();
						messageimage.image = image;
						// DimensionManager.getWorld(id)
						messageimage.imageName = message.filename;
						return messageimage;
					} catch (IOException e) {
						e.printStackTrace();
						MessageFailLoadImage messagefail = new MessageFailLoadImage();
						messagefail.imagename = message.filename;
						messagefail.type = EnumFailLoadImage.ErrorToLoadFile;
						return messagefail;
					}
				} else {
					System.out.println("服务器端图片不存在，加载失败!");
					MessageFailLoadImage messagefail = new MessageFailLoadImage();
					messagefail.imagename = message.filename;
					messagefail.type = EnumFailLoadImage.FileNoFound;
					return messagefail;
				}
			}
			return null;
		}
	}
}
