package com.porpit.minecamera.client;

import com.porpit.minecamera.client.entity.EntityRenderLoader;
import com.porpit.minecamera.client.tilerender.PictureFrameTileRenderer;
import com.porpit.minecamera.client.tilerender.TileRenderLoader;
import com.porpit.minecamera.common.CommonProxy;
import com.porpit.minecamera.tileentity.TileEntityPictureFrame;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		new ItemRenderLoader();
		//ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPictureFrame.class, new PictureFrameTileRenderer());
		new TileRenderLoader();
		new EntityRenderLoader();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		new KeyLoader();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}
}
