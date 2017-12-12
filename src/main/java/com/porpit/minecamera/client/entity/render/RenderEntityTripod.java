package com.porpit.minecamera.client.entity.render;

import com.porpit.minecamera.MineCamera;
import com.porpit.minecamera.client.entity.model.ModelEntityTripod;
import com.porpit.minecamera.entity.EntityTripod;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEntityTripod extends Render<EntityTripod> {
	private static final ResourceLocation TRIPOD_TEXTURE = new ResourceLocation(
			MineCamera.MODID + ":" + "textures/entity/tripod.png");
	private ModelEntityTripod model = new ModelEntityTripod();

	public RenderEntityTripod(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTripod entity) {
		return RenderEntityTripod.TRIPOD_TEXTURE;
	}

	@Override
	public void doRender(EntityTripod entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		this.bindEntityTexture(entity);
		GlStateManager.translate((float) x, (float) y, (float) z);
		// GL11.glScalef(0.1F, 0.1F, 0.1F);
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		model.render(entity, 0F, 0F, 0F, entityYaw, 0F, 0.5F);
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
}