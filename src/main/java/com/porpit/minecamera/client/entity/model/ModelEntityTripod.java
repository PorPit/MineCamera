package com.porpit.minecamera.client.entity.model;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelEntityTripod extends ModelBase {
	public ModelRenderer top1;
	public ModelRenderer top2;
	public ModelRenderer top3;
	public ModelRenderer leg1;
	public ModelRenderer leg2;
	public ModelRenderer leg3;

	public ModelEntityTripod() {
		this.textureHeight = 64;
		this.textureWidth = 512;
		System.out.println("12312312312123123");
		this.top1 = new ModelRenderer(this, 0, 0);
		this.top1.addBox(-25F, 0F, -5F, 50, 30, 10, 0F);
		this.top1.setRotationPoint(0F, 0F, 0F);
		this.top1.setTextureSize(120, 40);
		setRotation(top1, 0F, 0F, 0F);
		this.top2 = new ModelRenderer(this, 141, 0);
		this.top2.addBox(-15F, 30F, -15F, 30, 10, 30, 0F);
		this.top2.setRotationPoint(0F, 0F, 0F);
		this.top2.setTextureSize(120, 40);
		setRotation(top2, 0F, 0F, 0F);
		this.top3 = new ModelRenderer(this, 120, 0);
		this.top3.addBox(-15F, -5F, -5F, 15, 5, 10, 0F);
		this.top3.setRotationPoint(0F, 0F, 0F);
		this.top3.setTextureSize(50, 15);
		setRotation(top3, 0F, 0F, 0F);
		this.leg1 = new ModelRenderer(this, 0, 40);
		this.leg1.addBox(0F, 0F, 0F, 246, 10, 10, 0F);
		this.leg1.offsetY = 15;
		this.leg1.setRotationPoint(0F, 0F, 0F);
		this.leg1.setTextureSize(512, 20);
		setRotation(leg1, 0F, 0.4F, 1.2F);
		this.leg2 = new ModelRenderer(this, 0, 40);
		this.leg2.addBox(0F, 0F, 0F, 246, 10, 10, 0F);
		this.leg2.offsetY = 15;
		this.leg2.setRotationPoint(0F, 0F, 0F);
		this.leg2.setTextureSize(512, 20);
		setRotation(leg2, 0F, 0.4F, 1.9F);
		this.leg3 = new ModelRenderer(this, 0, 40);
		this.leg3.offsetY = 5;
		this.leg3.addBox(0F, 0F, 0F, 246, 10, 10, 0F);
		this.leg3.setRotationPoint(0F, 0F, 0F);
		this.leg3.setTextureSize(512, 20);
		setRotation(leg3, 0F, -0.4F, 1.6F);
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float rotateFloat, float rotateYaw,
			float rotatePitch, float scale) {
		GL11.glScalef(0.014F, 0.014F, 0.014F);
		GL11.glTranslatef(0F, -165F, 0F);
		GlStateManager.disableLighting();
		this.setRotationAngles(limbSwing, limbSwingAmount, rotateFloat, rotateYaw, rotatePitch, scale, entity);
		this.top1.render(scale);
		this.top2.render(scale);
		this.top3.render(scale);
		// this.top3.setTextureOffset(120, 0);
		// this.top2.offsetY=0F;
		// this.top1.rotationPointX=0F;
		// this.leg1.setRotationPoint(0, 0, 0);
		// setRotation(leg1, 0F,0.4F,1.2F);
		// leg1.mirror = true;
		this.leg1.render(scale);
		this.leg1.offsetX = 3F;
		this.leg2.offsetX = 3F;
		this.leg3.offsetX = 3F;
		this.leg1.offsetY = 18F;
		this.leg2.offsetY = 18F;
		this.leg3.offsetY = 18F;
		this.leg1.offsetZ = -2F;
		this.leg2.offsetZ = -2F;
		this.leg3.offsetZ = -2F;
		// this.leg2.setRotationPoint(0, 0, 0);
		// setRotation(leg2, 0F, 0.4F, 1.9F);
		this.leg2.render(scale);
		// this.leg3.setRotationPoint(0, 0, 0);
		// setRotation(leg3, 0F,-0.4F, 1.6F);
		this.leg3.render(scale);
		GlStateManager.enableLighting();

	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float rotateFloat, float rotateYaw,
			float rotatePitch, float scale, Entity entity) {
		this.top1.rotateAngleY = (float) ((rotateYaw % 360) / 360 * 2 * Math.PI);
		this.top2.rotateAngleY = (float) ((rotateYaw % 360) / 360 * 2 * Math.PI);
		this.top3.rotateAngleY = (float) ((rotateYaw % 360) / 360 * 2 * Math.PI);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
