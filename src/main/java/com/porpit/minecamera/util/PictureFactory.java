package com.porpit.minecamera.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.porpit.minecamera.MineCamera;
import com.porpit.minecamera.network.MessageImage;
import com.porpit.minecamera.network.NetworkLoader;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class PictureFactory {
	public static final ResourceLocation FailToLoadImage = new ResourceLocation(
			MineCamera.MODID + ":" + "textures/gui/image/loding.png");
	public static final ResourceLocation LODINGIMAGE = new ResourceLocation(
			MineCamera.MODID + ":" + "textures/gui/image/loding.png");
	public static final ResourceLocation DONATEIMAGE = new ResourceLocation(
			MineCamera.MODID + ":" + "textures/gui/image/donate.png");
	public static Map<String, Integer> loadedPicture = new HashMap<String, Integer>();
	public static Map<String, EnumFailLoadImage> fildToLoadPicture = new HashMap<String, EnumFailLoadImage>();
	public static Set<String> lodingPicture = new HashSet<String>();

	private PictureFactory() {
	}

	public static BufferedImage FormattingPicture(BufferedImage imageIn) {

		BufferedImage image = new BufferedImage(854, 480, BufferedImage.TYPE_INT_BGR);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.drawImage(imageIn, 0, 0, 854, 480, null);
		return image;
	}

	@SideOnly(Side.CLIENT)
	public static BufferedImage getScreenshot() {
		BufferedImage image = ScreenShotHelper.createScreenshot(Minecraft.getMinecraft().displayWidth,
				Minecraft.getMinecraft().displayHeight, Minecraft.getMinecraft().getFramebuffer());
		return image;
	}

	@SideOnly(Side.CLIENT)
	public static BufferedImage getFormattingScreenshot() {
		BufferedImage image = ScreenShotHelper.createScreenshot(Minecraft.getMinecraft().displayWidth,
				Minecraft.getMinecraft().displayHeight, Minecraft.getMinecraft().getFramebuffer());
		return FormattingPicture(image);
	}

	public static EnumFailLoadImage isFailedToLoad(String imagename) {
		if (fildToLoadPicture.containsKey(imagename)) {
			return fildToLoadPicture.get(imagename);
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	public static int loadTexture(String imagename, BufferedImage image) {

		int BYTES_PER_PIXEL = 4;
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL);

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
				buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green component
				buffer.put((byte) (pixel & 0xFF)); // Blue component
				buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component.
															// Only for RGBA
			}
		}

		buffer.flip(); // FOR THE LOVE OF GOD DO NOT FORGET THIS

		// You now have a ByteBuffer filled with the color data of each pixel.
		// Now just create a texture ID and bind it. Then you can load it using
		// whatever OpenGL method you want, for example:

		int textureID = GL11.glGenTextures(); // Generate texture ID
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID); // Bind texture ID
		// Setup wrap mode
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		// Setup texture scaling filtering
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

		// Send texel data to OpenGL
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA,
				GL11.GL_UNSIGNED_BYTE, buffer);

		// Return the texture ID so we can bind it later again
		loadedPicture.put(imagename, textureID);
		return textureID;
	}

	@SideOnly(Side.CLIENT)
	public static void SendImageToServer(BufferedImage image, String filename) {
		// max packdata 32767 bytes
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "jpg", out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("照片长度：" + out.toByteArray().length);
		if (out.toByteArray().length <= 30000) {
			MessageImage message = new MessageImage();
			message.image = image;
			message.imageName = filename;
			NetworkLoader.instance.sendToServer(message);
		} else {
			int packMax = out.toByteArray().length / 30000 + 1;
			System.out.println("拆分为：" + packMax + "个包");
			byte[][] splited = split_bytes(out.toByteArray(), 30000);
			for (int i = 1; i <= packMax; i++) {
				MessageImage message = new MessageImage();
				message.packMax = packMax;
				message.packIndex = i;
				message.imageName = filename;
				message.imageData = splited[i - 1];
				NetworkLoader.instance.sendToServer(message);
			}
		}
	}

	public static byte[] byteMerger(byte[] bt1, byte[] bt2) {
		byte[] bt3 = new byte[bt1.length + bt2.length];
		int i = 0;
		for (byte bt : bt1) {
			bt3[i] = bt;
			i++;
		}

		for (byte bt : bt2) {
			bt3[i] = bt;
			i++;
		}
		return bt3;
	}

	public static byte[][] split_bytes(byte[] bytes, int copies) {

		double split_length = Double.parseDouble(copies + "");

		int array_length = (int) Math.ceil(bytes.length / split_length);
		byte[][] result = new byte[array_length][];

		int from, to;

		for (int i = 0; i < array_length; i++) {

			from = (int) (i * split_length);
			to = (int) (from + split_length);

			if (to > bytes.length)
				to = bytes.length;

			result[i] = Arrays.copyOfRange(bytes, from, to);
		}

		return result;
	}
}
