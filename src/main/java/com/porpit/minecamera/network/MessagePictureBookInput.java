package com.porpit.minecamera.network;

import com.porpit.minecamera.inventory.ContainerPictureBook;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessagePictureBookInput implements IMessage {

	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void toBytes(ByteBuf buf) {
		// TODO 自动生成的方法存根

	}

	public static class Handler implements IMessageHandler<MessagePictureBookInput, IMessage> {
		@Override
		public IMessage onMessage(MessagePictureBookInput message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				if (ctx.getServerHandler().playerEntity != null) {
					EntityPlayerMP player = ctx.getServerHandler().playerEntity;
					if (player.openContainer instanceof ContainerPictureBook) {
						ContainerPictureBook contaner = (ContainerPictureBook) player.openContainer;
						if (contaner.getSlot(0).getHasStack() && contaner.getSlot(0).getStack().hasTagCompound()
								&& contaner.getSlot(0).getStack().getTagCompound().hasKey("pid")) {
							ItemStack itemStack = contaner.getSlot(0).getStack();
							String pid = itemStack.getTagCompound().getString("pid");
							if (contaner.getTotalPictureNum() <= 100) {
								contaner.getListPid().add(pid);
								contaner.setTotalPictureNum(contaner.getTotalPictureNum() + 1);
								if (contaner.getTotalPictureNum() == 1) {
									contaner.setIndex(0);
								}
							}
						}
						contaner.getSlot(0).decrStackSize(64);
					}
				}
			}
			return null;
		}
	}
}
