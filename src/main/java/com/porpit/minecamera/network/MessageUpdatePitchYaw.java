package com.porpit.minecamera.network;

import com.porpit.minecamera.entity.EntityTripod;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageUpdatePitchYaw implements IMessage {
	public float rotationYaw;
	public float rotationPitch;

	@Override
	public void fromBytes(ByteBuf buf) {
		rotationYaw = buf.readFloat();
		rotationPitch = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(rotationYaw);
		buf.writeFloat(rotationPitch);
	}

	public static class Handler implements IMessageHandler<MessageUpdatePitchYaw, IMessage> {
		@Override
		public IMessage onMessage(MessageUpdatePitchYaw message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				EntityPlayerMP player = ctx.getServerHandler().playerEntity;
				Entity entity =player.getEntityWorld()
						.getEntityByID(player.getEntityData().getInteger("renderViewCamera"));
				// entity.setDelay(message.delay);
				if (player != null && entity != null&&entity instanceof EntityTripod) {
					entity.rotationYaw = message.rotationYaw;
					entity.rotationPitch = message.rotationPitch;
				}
			}
			return null;
		}
	}
}
