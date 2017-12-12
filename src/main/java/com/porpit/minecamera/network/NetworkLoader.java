package com.porpit.minecamera.network;

import com.porpit.minecamera.MineCamera;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkLoader {
	public static SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(MineCamera.MODID);

	private static int nextID = 0;

	public NetworkLoader(FMLPreInitializationEvent event) {
		registerMessage(MessageImageSyncSave.Handler.class, MessageImageSyncSave.class, Side.CLIENT);
		registerMessage(MessageImage.Handler.class, MessageImage.class, Side.CLIENT);
		registerMessage(MessageImage.Handler.class, MessageImage.class, Side.SERVER);
		registerMessage(MessageImageRequest.Handler.class, MessageImageRequest.class, Side.SERVER);
		registerMessage(MessagePhotoProcessorStart.Handler.class, MessagePhotoProcessorStart.class, Side.SERVER);
		registerMessage(MessagePlayerViewRender.Handler.class, MessagePlayerViewRender.class, Side.SERVER);
		registerMessage(MessageCameraDelaySet.Handler.class, MessageCameraDelaySet.class, Side.SERVER);
		registerMessage(MessageUpdatePitchYaw.Handler.class, MessageUpdatePitchYaw.class, Side.SERVER);
		registerMessage(MessageTripodFilmOut.Handler.class, MessageTripodFilmOut.class, Side.SERVER);
		registerMessage(MessageSpawnParticle.Handler.class, MessageSpawnParticle.class, Side.CLIENT);
		registerMessage(MessageFailLoadImage.Handler.class, MessageFailLoadImage.class, Side.CLIENT);
	}

	private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(
			Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side) {
		instance.registerMessage(messageHandler, requestMessageType, nextID++, side);
	}
}
