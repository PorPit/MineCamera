package com.porpit.minecamera.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessagePlayerViewRender implements IMessage {

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class Handler implements IMessageHandler<MessagePlayerViewRender, IMessage> {
		@Override
		public IMessage onMessage(MessagePlayerViewRender message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				if(ctx.getServerHandler().player!=null){
					ctx.getServerHandler().player.getEntityData().removeTag("renderViewCamera");
				}
			}
			return null;
		}
	}
}
