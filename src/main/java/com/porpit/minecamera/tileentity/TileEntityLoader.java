package com.porpit.minecamera.tileentity;

import com.porpit.minecamera.MineCamera;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityLoader {
	public TileEntityLoader(FMLPreInitializationEvent event) {
		registerTileEntity(TileEntityPictureFrame.class, "PictureFrame");
		registerTileEntity(TileEntityPhotoProcessor.class, "PhotoProcessor");
		registerTileEntity(TileEntityPictureFrameMultiple.class, "PictureFrameMultiple");
	}

	public void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String id) {
		GameRegistry.registerTileEntity(tileEntityClass, MineCamera.MODID + ":" + id);
	}
}
