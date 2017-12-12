package com.porpit.minecamera.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessagePlayerViewRender implements IMessage {
	public String playername;

	@Override
	public void fromBytes(ByteBuf buf) {
		int playernamelength = buf.readInt();
		byte playernamebyte[] = new byte[playernamelength];
		buf.readBytes(playernamebyte);
		playername = new String(playernamebyte);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(playername.getBytes().length);
		buf.writeBytes(playername.getBytes());
	}

	public static class Handler implements IMessageHandler<MessagePlayerViewRender, IMessage> {
		@Override
		public IMessage onMessage(MessagePlayerViewRender message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				DimensionManager.getWorld(0).getMinecraftServer().getPlayerList()
						.getPlayerByUsername(message.playername).getEntityData().removeTag("renderViewCamera");
			}
			return null;
		}
	}
}
