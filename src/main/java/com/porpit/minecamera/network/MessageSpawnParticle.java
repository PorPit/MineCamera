package com.porpit.minecamera.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSpawnParticle implements IMessage {
	public int delay;
	public int typeid;
	public double PosX;
	public double PosY;
	public double PosZ;
	public double SpeedX;
	public double SpeedY;
	public double SpeedZ;

	@Override
	public void fromBytes(ByteBuf buf) {
		this.delay = buf.readInt();
		this.typeid = buf.readInt();
		this.PosX = buf.readDouble();
		this.PosY = buf.readDouble();
		this.PosZ = buf.readDouble();
		this.SpeedX = buf.readDouble();
		this.SpeedY = buf.readDouble();
		this.SpeedZ = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(delay);
		buf.writeInt(typeid);
		buf.writeDouble(PosX);
		buf.writeDouble(PosY);
		buf.writeDouble(PosZ);
		buf.writeDouble(SpeedX);
		buf.writeDouble(SpeedY);
		buf.writeDouble(SpeedZ);
	}

	public static class Handler implements IMessageHandler<MessageSpawnParticle, IMessage> {
		@Override
		public IMessage onMessage(MessageSpawnParticle message, MessageContext ctx) {
			System.out.println(ctx.side.toString());
			if (ctx.side == Side.CLIENT) {
				SpawnParticleAsyncThread thread = new SpawnParticleAsyncThread(message.delay, message.typeid,
						message.PosX, message.PosY, message.PosZ, message.SpeedX, message.SpeedY, message.SpeedZ);
				thread.start();
			}
			return null;
		}
	}

	private static class SpawnParticleAsyncThread extends Thread {
		private int delay;
		private int typeid;
		private double PosX;
		private double PosY;
		private double PosZ;
		private double SpeedX;
		private double SpeedY;
		private double SpeedZ;

		public SpawnParticleAsyncThread(int delay, int typeid, double posX, double posY, double posZ, double speedX,
				double speedY, double speedZ) {
			this.delay = delay;
			this.typeid = typeid;
			this.PosX = posX;
			this.PosY = posY;
			this.PosZ = posZ;
			this.SpeedX = speedX;
			this.SpeedY = speedY;
			this.SpeedZ = speedZ;
		}

		public void run() {
			if (delay > 0) {
				try {
					currentThread().sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Minecraft.getMinecraft().world.spawnParticle(EnumParticleTypes.getParticleFromId(typeid), this.PosX,
					this.PosY, this.PosZ, this.SpeedX, this.SpeedY, this.SpeedZ, 0);
		}
	}
}
