package com.porpit.minecamera.util;

import com.porpit.minecamera.network.MessageTripodFilmOut;
import com.porpit.minecamera.network.NetworkLoader;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentTranslation;

public class TripodActiveThread extends Thread {
	public static boolean isshooting = false;
	private String playername;
	private int delay;

	public TripodActiveThread(String playername, int delay) {
		this.playername = playername;
		this.delay = delay;
	}

	public void run() {
		isshooting = true;
		try {
			for (int i = delay; i > 0; i--) {
				Minecraft.getMinecraft().thePlayer
						.addChatComponentMessage(new TextComponentTranslation("chat.minecamera.delayinfo", i));

				Thread.sleep(1000);

				if (!Minecraft.getMinecraft().thePlayer.getEntityData().hasKey("renderViewCamera")) {
					Minecraft.getMinecraft().thePlayer
							.addChatComponentMessage(new TextComponentTranslation("chat.minecamera.leavetripod"));
					isshooting = false;
					return;
				}
			}
			Minecraft.getMinecraft().thePlayer
					.addChatComponentMessage(new TextComponentTranslation("chat.minecamera.trytoshoot"));
		} catch (InterruptedException e) {
			isshooting = false;
			e.printStackTrace();
		}
		MessageTripodFilmOut message = new MessageTripodFilmOut();
		NetworkLoader.instance.sendToServer(message);

		isshooting = false;
	}
}
