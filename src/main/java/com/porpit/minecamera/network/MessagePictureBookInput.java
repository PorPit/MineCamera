package com.porpit.minecamera.network;

import com.porpit.minecamera.inventory.ContainerPictureBook;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessagePictureBookInput implements IMessage {

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {

	}

	public static class Handler implements IMessageHandler<MessagePictureBookInput, IMessage> {
		@Override
		public IMessage onMessage(MessagePictureBookInput message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				EntityPlayerMP player = ctx.getServerHandler().playerEntity;
				if (player != null && player.openContainer != null
						&& player.openContainer instanceof ContainerPictureBook) {
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
						} else {
							player.addChatComponentMessage(new TextComponentTranslation("chat.picturebook.tomany"));
							return null;
						}
					}
					contaner.getSlot(0).decrStackSize(64);
				}
			}
			return null;
		}
	}
}
