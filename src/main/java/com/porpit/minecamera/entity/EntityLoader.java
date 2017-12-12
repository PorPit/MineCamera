package com.porpit.minecamera.entity;

import com.porpit.minecamera.MineCamera;
import com.porpit.minecamera.client.entity.EntityRenderFactory;
import com.porpit.minecamera.client.entity.render.RenderEntityTripod;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityLoader {
	private static int nextID = 0;

	public EntityLoader() {
		registerEntity(EntityTripod.class, "Tripod", 80, 3, true);
	}

	private static void registerEntity(Class<? extends Entity> entityClass, String name, int trackingRange,
			int updateFrequency, boolean sendsVelocityUpdates) {
		EntityRegistry.registerModEntity(entityClass, name, nextID++, MineCamera.instance, trackingRange,
				updateFrequency, sendsVelocityUpdates);
	}

	@SideOnly(Side.CLIENT)
	public static void registerRenders() {
		registerEntityRender(EntityTripod.class, RenderEntityTripod.class);
	}

	@SideOnly(Side.CLIENT)
	private static <T extends Entity> void registerEntityRender(Class<T> entityClass,
			Class<? extends Render<T>> render) {
		RenderingRegistry.registerEntityRenderingHandler(entityClass, new EntityRenderFactory<T>(render));
	}
}