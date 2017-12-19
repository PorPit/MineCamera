package com.porpit.minecamera;

import com.porpit.minecamera.common.CommonProxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.reflect.internal.Trees.This;

@Mod(modid = MineCamera.MODID, name = MineCamera.NAME, version = MineCamera.VERSION, dependencies="after:MineCameraDummy;", acceptedMinecraftVersions = "1.10.2")
public class MineCamera {
	public static final String MODID = "minecamera";
	public static final String NAME = "MineCamera";
	public static final String VERSION = "alpha_1.1.0";

	public static String ServerCatchFile;
	public static String ClientCatchFile ;

	@SidedProxy(clientSide = "com.porpit.minecamera.client.ClientProxy", serverSide = "com.porpit.minecamera.common.CommonProxy")
	public static CommonProxy proxy;
	@Instance(MineCamera.MODID)
	public static MineCamera instance;

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		proxy.serverStarting(event);
	}
 
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ServerCatchFile=event.getSourceFile().getParentFile().toString()+"\\minecamera\\serverdata\\";
		ClientCatchFile=event.getSourceFile().getParentFile().toString()+"\\minecamera\\catchdata\\";
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
}
