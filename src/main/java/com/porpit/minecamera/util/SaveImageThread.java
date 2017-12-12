package com.porpit.minecamera.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.porpit.minecamera.MineCamera;

import net.minecraft.client.Minecraft;

public class SaveImageThread extends Thread {
	String filename;
	BufferedImage screenshot;

	public SaveImageThread(String filename) {
		this.filename = filename;
	}

	public void run() {
		if (!Minecraft.getMinecraft().gameSettings.hideGUI) {
			Minecraft.getMinecraft().gameSettings.hideGUI = true;
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					screenshot = PictureFactory.getFormattingScreenshot();
					Minecraft.getMinecraft().gameSettings.hideGUI = false;
					File file = new File(MineCamera.ClientCatchFile + filename + ".jpeg");
					try {
						if (!file.getParentFile().exists()) {
							file.getParentFile().mkdirs();
						}
						if (!file.exists()) {
							file.createNewFile();
						}
						ImageIO.write(screenshot, "jpg", file);
					} catch (IOException e) {
						e.printStackTrace();
					}
					PictureFactory.SendImageToServer(screenshot, filename);
				}
			});
		} else {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					try {
						screenshot = PictureFactory.getFormattingScreenshot();
						File file = new File(MineCamera.ClientCatchFile + filename + ".jpeg");
						if (!file.getParentFile().exists()) {
							file.getParentFile().mkdirs();
						}
						if (!file.exists()) {
							file.createNewFile();
						}
						ImageIO.write(screenshot, "jpg", file);
					} catch (Exception e) {
						e.printStackTrace();
					}
					PictureFactory.SendImageToServer(screenshot, filename);
				}
			});
		}
	}
}