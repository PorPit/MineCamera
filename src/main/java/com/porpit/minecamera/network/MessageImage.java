package com.porpit.minecamera.network;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.porpit.minecamera.MineCamera;
import com.porpit.minecamera.util.PictureFactory;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageImage implements IMessage {
	private static Map<String, byte[]> imagecatchdata = new HashMap<String, byte[]>();
	// success flag
	boolean flag = true;
	public BufferedImage image;
	// when packMax>1
	public byte[] imageData;
	public int packMax = 1;
	public int packIndex = 1;
	public String imageName;

	@Override
	public void fromBytes(ByteBuf buf) {
		// read imagename
		int namelength = buf.readInt();
		byte namebyte[] = new byte[namelength];
		buf.readBytes(namebyte);
		imageName = new String(namebyte);
		System.out.println(imageName);
		// read pack
		packMax = buf.readInt();
		packIndex = buf.readInt();
		// read imagedata
		if (packMax == 1) {
			int imagelength = buf.readInt();
			byte imagebyte[] = new byte[imagelength];
			buf.readBytes(imagebyte);
			ByteArrayInputStream in = new ByteArrayInputStream(imagebyte);
			try {
				image = ImageIO.read(in);
				System.out.println("获取数据" + image.getHeight());
			} catch (IOException e) {
				flag = false;
				e.printStackTrace();
			}
		} else if (packMax > packIndex) {
			int imagedatalength = buf.readInt();
			imageData = new byte[imagedatalength];
			buf.readBytes(imageData);
			if (!imagecatchdata.containsKey(imageName)) {
				imagecatchdata.put(imageName, imageData);
			} else {
				byte[] temp = imagecatchdata.get(imageName);
				imagecatchdata.put(imageName, PictureFactory.byteMerger(temp, imageData));
			}
		} else {
			int imagedatalength = buf.readInt();
			imageData = new byte[imagedatalength];
			buf.readBytes(imageData);
			byte[] temp = imagecatchdata.get(imageName);
			byte[] fulldata = PictureFactory.byteMerger(temp, imageData);
			ByteArrayInputStream in = new ByteArrayInputStream(fulldata);
			try {
				image = ImageIO.read(in);
				System.out.println("获取数据" + image.getHeight());
			} catch (IOException e) {
				flag = false;
				e.printStackTrace();
			} finally {
				System.out.println("删除传输缓存");
				imagecatchdata.remove(imageName);
			}

		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		if (packMax == 1) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				ImageIO.write(image, "jpg", out);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("写入数据1" + out.toByteArray().length);
			System.out.println(buf.readerIndex());
			buf.writeInt(imageName.getBytes().length);
			buf.writeBytes(imageName.getBytes());
			buf.writeInt(packMax);
			buf.writeInt(packIndex);
			buf.writeInt(out.toByteArray().length);
			buf.writeBytes(out.toByteArray());
			System.out.println("写入数据2" + buf.array().length);

			// ByteBufUtils.writeTag(buf, nbt);
		} else {
			buf.writeInt(imageName.getBytes().length);
			buf.writeBytes(imageName.getBytes());
			buf.writeInt(packMax);
			buf.writeInt(packIndex);
			buf.writeInt(imageData.length);
			buf.writeBytes(imageData);
		}
	}

	public static class Handler implements IMessageHandler<MessageImage, IMessage> {
		@Override
		public IMessage onMessage(MessageImage message, MessageContext ctx) {
			System.out.println(ctx.side.toString());
			if (ctx.side == Side.CLIENT) {
				final BufferedImage image = message.image;
				final String name = message.imageName;

				System.out.println("读取服务器发送的图片中...");
				try {
					File file = new File(MineCamera.ClientCatchFile+ message.imageName + ".jpeg");
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					if (!file.exists()) {
						file.createNewFile();
					}
					ImageIO.write(image, "jpg", file);
					Minecraft.getMinecraft().addScheduledTask(new Runnable() {
						@Override
						public void run() {
							int textureId = PictureFactory.loadTexture(name, image);
							PictureFactory.loadedPicture.put(name, textureId);
							PictureFactory.lodingPicture.remove(name);
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("读取客户端发送的图片中");
				if (message.packMax == message.packIndex) {
					final BufferedImage image = message.image;
					//判断是否为本地服务器
					File fileclient = new File(
							MineCamera.ClientCatchFile);
					if(fileclient.exists()){
						System.out.println("检测为本地客户端,不保存图片");
						return null;
					}
					File file = new File(
							MineCamera.ServerCatchFile+ message.imageName + ".jpeg");
					try {
						if (!file.getParentFile().exists()) {
							file.getParentFile().mkdirs();
						}
						if (!file.exists()) {
							file.createNewFile();
						}
						ImageIO.write(image, "jpg", file);
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("大小：" + image.getHeight());
				}
			}
			return null;
		}
	}
}
