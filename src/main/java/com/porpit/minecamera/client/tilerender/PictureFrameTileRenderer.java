package com.porpit.minecamera.client.tilerender;

import org.lwjgl.opengl.GL11;

import com.porpit.minecamera.block.BlockPictureFrame.EnumStateType;
import com.porpit.minecamera.tileentity.TileEntityPictureFrame;
import com.porpit.minecamera.util.PictureFactory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;

public class PictureFrameTileRenderer extends TileEntitySpecialRenderer<TileEntityPictureFrame> {
	@Override
	public void render(TileEntityPictureFrame frame, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		// 1:normal 2:failtoload 3:loding
		int rendertype = 0;
		if (frame.istextureLoaded()) {
			if (frame.textureID != -1) {
				rendertype = 1;
			}
		} else {
			if (frame.hasImageName()) {
				if (frame.canLoadImage()) {
					frame.loadImage();
				}
				if (frame.isFailed()) {
					rendertype = 2;
				} else {
					rendertype = 3;
				}
			}
		}
		// render
		if (rendertype > 0) {
			boolean flippedX = false;
			boolean flippedY = false;
			GlStateManager.enableBlend();
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GlStateManager.disableLighting();
			GlStateManager.color(1, 1, 1, 1);

			switch (rendertype) {
			case 1:
				GlStateManager.bindTexture(frame.textureID);
				break;
			case 2:
				Minecraft.getMinecraft().getTextureManager().bindTexture(PictureFactory.FailToLoadImage);
				break;
			case 3:
				Minecraft.getMinecraft().getTextureManager().bindTexture(PictureFactory.LODINGIMAGE);
				break;
			default:
				return;
			}

			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

			GlStateManager.pushMatrix();
			GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);

			EnumFacing direction = EnumFacing.getFront(frame.getBlockMetadata() & 3);
			EnumStateType stateType = EnumStateType.values()[frame.getBlockMetadata() >> 2];
			applyDirection(direction);
			GlStateManager.enableRescaleNormal();
			if (stateType.equals(EnumStateType.HANDGING)) {
				GL11.glTranslated(-0.93, 0, 0);
				GL11.glScaled(1, 0.60, 0.9);
			} else {
				GL11.glTranslated(-0.50, -0.2, 0);
				GL11.glScaled(1, 0.60, 0.9);
				GL11.glRotatef(14F, 0F, 0F, 1F);
			}

			GL11.glBegin(GL11.GL_POLYGON);
			GL11.glNormal3f(1.0f, 0.0F, 0.0f);

			GL11.glTexCoord3f(flippedY ? 0 : 1, flippedX ? 0 : 1, 0);
			GL11.glVertex3f(0.5F, -0.5f, -0.5f);
			GL11.glTexCoord3f(flippedY ? 0 : 1, flippedX ? 1 : 0, 0);
			GL11.glVertex3f(0.5f, 0.5f, -0.5f);
			GL11.glTexCoord3f(flippedY ? 1 : 0, flippedX ? 1 : 0, 0);
			GL11.glVertex3f(0.5f, 0.5f, 0.5f);
			GL11.glTexCoord3f(flippedY ? 1 : 0, flippedX ? 0 : 1, 0);
			GL11.glVertex3f(0.5f, -0.5f, 0.5f);
			GL11.glEnd();
			GlStateManager.popMatrix();

			GlStateManager.disableRescaleNormal();
			GlStateManager.disableBlend();
			GlStateManager.enableLighting();
		}
	}

	@Override
	public boolean isGlobalRenderer(TileEntityPictureFrame te) {
		return false;
	}

	public static void applyDirection(EnumFacing direction) {
		int rotation = 0;
		switch (direction.getIndex()) {
		case 0:
			rotation = 270;
			break;
		case 1:
			rotation = 180;
			break;
		case 2:
			rotation = 90;
			break;
		case 3:
			rotation = 0;
			break;
		default:
			break;
		}
		GL11.glRotated(rotation, 0, 1, 0);
	}
}
