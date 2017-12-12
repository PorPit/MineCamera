package com.porpit.minecamera.client.tilerender;

import com.porpit.minecamera.client.entity.render.RenderEntityTripod;
import com.porpit.minecamera.entity.EntityTripod;
import com.porpit.minecamera.tileentity.TileEntityPictureFrame;
import com.porpit.minecamera.tileentity.TileEntityPictureFrameMultiple;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileRenderLoader {
	public TileRenderLoader() {
		registerRenders();
	}

	@SideOnly(Side.CLIENT)
	public static void registerRenders() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPictureFrame.class, new PictureFrameTileRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPictureFrameMultiple.class, new PictureFrameMultipleTileRenderer());
	}
}
