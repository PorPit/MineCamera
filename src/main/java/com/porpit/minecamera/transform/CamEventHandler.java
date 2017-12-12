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
		if (mc.thePlayer != null && mc.thePlayer.getEntityData().hasKey("renderViewCamera")) {
			camera = mc.getRenderViewEntity();
			mc.setRenderViewEntity(mc.thePlayer);
		}
	}

	public static void setupMouseHandlerAfter() {
		if (mc.thePlayer != null && mc.thePlayer.getEntityData().hasKey("renderViewCamera")) {
			mc.setRenderViewEntity(camera);
			// camera = null;
		}
	}
}
