package com.porpit.minecamera.network;

import com.porpit.minecamera.entity.EntityTripod;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageCameraDelaySet implements IMessage {
	public int delay;
	public String playername;

	@Override
	public void fromBytes(ByteBuf buf) {

		this.delay = buf.readInt();
		int playernamelength = buf.readInt();
		byte playernamebyte[] = new byte[playernamelength];
		buf.readBytes(playernamebyte);
		playername = new String(playernamebyte);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(delay);
		buf.writeInt(playername.getBytes().length);
		buf.writeBytes(playername.getBytes());
	}

	public static class Handler implements IMessageHandler<MessageCameraDelaySet, IMessage> {
		@Override
		public IMessage onMessage(MessageCameraDelaySet message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				EntityPlayerMP player = DimensionManager.getWorld(0).getMinecraftServer().getPlayerList()
						.getPlayerByUsername(message.playername);
				EntityTripod entity = (EntityTripod) player.getEntityWorld()
						.getEntityByID(player.getEntityData().getInteger("usingGui"));
				entity.setDelay(message.delay);
			}
			return null;
		}
	}
}
