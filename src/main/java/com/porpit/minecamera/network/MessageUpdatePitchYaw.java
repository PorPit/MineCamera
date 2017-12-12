package com.porpit.minecamera.network;

import com.porpit.minecamera.entity.EntityTripod;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageUpdatePitchYaw implements IMessage {
	public float rotationYaw;
	public float rotationPitch;
	public String playername;

	@Override
	public void fromBytes(ByteBuf buf) {
		rotationYaw = buf.readFloat();
		rotationPitch = buf.readFloat();
		int playernamelength = buf.readInt();
		byte playernamebyte[] = new byte[playernamelength];
		buf.readBytes(playernamebyte);
		playername = new String(playernamebyte);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(rotationYaw);
		buf.writeFloat(rotationPitch);
		buf.writeInt(playername.getBytes().length);
		buf.writeBytes(playername.getBytes());
	}

	public static class Handler implements IMessageHandler<MessageUpdatePitchYaw, IMessage> {
		@Override
		public IMessage onMessage(MessageUpdatePitchYaw message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				EntityPlayerMP player = DimensionManager.getWorld(0).getMinecraftServer().getPlayerList()
						.getPlayerByUsername(message.playername);
				EntityTripod entity = (EntityTripod) player.getEntityWorld()
						.getEntityByID(player.getEntityData().getInteger("renderViewCamera"));
				// entity.setDelay(message.delay);
				if (player != null && entity != null) {
					entity.rotationYaw = message.rotationYaw;
					entity.rotationPitch = message.rotationPitch;
				}
			}
			return null;
		}
	}
}
