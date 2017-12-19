package com.porpit.minecamera.transform;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class CamEventHandler {

	public static boolean shouldPlayerTakeInput() {
		return true;
	}

	public static Minecraft mc = Minecraft.getMinecraft();

	public static Entity camera = null;

	public static void setupMouseHandlerBefore() {
		if (mc.player != null && mc.player.getEntityData().hasKey("renderViewCamera")) {
			camera = mc.getRenderViewEntity();
			mc.setRenderViewEntity(mc.player);
		}
	}

	public static void setupMouseHandlerAfter() {
		if (mc.player != null && mc.player.getEntityData().hasKey("renderViewCamera")) {
			mc.setRenderViewEntity(camera);
			// camera = null;
		}
	}
}
