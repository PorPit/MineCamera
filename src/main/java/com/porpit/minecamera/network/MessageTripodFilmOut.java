package com.porpit.minecamera.network;

import java.util.List;

import com.porpit.minecamera.entity.EntityTripod;
import com.porpit.minecamera.item.ItemLoader;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class MessageTripodFilmOut implements IMessage {

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<MessageTripodFilmOut, IMessage> {
        @Override
        public IMessage onMessage(MessageTripodFilmOut message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                EntityPlayerMP player = ctx.getServerHandler().player;
                Entity entity = player.getEntityWorld()
                        .getEntityByID(player.getEntityData().getInteger("renderViewCamera"));
                if (player != null && entity != null && entity instanceof EntityTripod) {
                    IItemHandler items = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                            player.getHorizontalFacing());
                    if (items.getStackInSlot(3) != null && !items.getStackInSlot(3).isEmpty()) {
                        player.sendMessage(new TextComponentTranslation("chat.minecamera.isouting"));
                        return null;
                    }
                    if (items.getStackInSlot(0) == null || items.getStackInSlot(0).isEmpty()) {
                        player.sendMessage(new TextComponentTranslation("chat.minecamera.nobettery"));
                        return null;
                    }
                    if (items.getStackInSlot(1) == null || items.getStackInSlot(1).isEmpty()) {
                        player.sendMessage(new TextComponentTranslation("chat.minecamera.nofilm"));
                        return null;
                    }
                    if (items.getStackInSlot(2) != null && !items.getStackInSlot(2).isEmpty()) {
                        player.sendMessage(new TextComponentTranslation("chat.minecamera.hasfilmout"));
                        return null;
                    }
                    if (items.getStackInSlot(1).hasTagCompound()
                            && items.getStackInSlot(1).getTagCompound().hasKey("pid")) {
                        player.sendMessage((new TextComponentTranslation("chat.minecamera.filmcantwrite")));
                        return null;
                    }
                    items.getStackInSlot(0).damageItem(1, player);
                    if (items.getStackInSlot(0).getCount() == 0) {
                        items.extractItem(0, 1, false);
                    }
                    items.extractItem(1, 64, false);
                    String imagename = ctx.getServerHandler().player.getName() + "%_%"
                            + System.currentTimeMillis();
                    ItemStack itemfilm = new ItemStack(ItemLoader.itemFilm, 1);
                    NBTTagCompound nbt = new NBTTagCompound();
                    nbt.setString("pid", imagename);
                    itemfilm.setTagCompound(nbt);
                    /*
                     * if(!(items.getStackInSlot(3)==null)){
                     * items.getStackInSlot(3).stackSize=0; }
                     */
                    items.insertItem(3, itemfilm, false);
                    ((EntityTripod) entity).setBurnTime(0);
                    player.sendMessage((new TextComponentTranslation("chat.minecamera.success")));
                    // effect


                    ctx.getServerHandler().player.getServer().addScheduledTask(new Runnable() {
                        @Override
                        public void run() {
                            double particlePosX = entity.posX, particlePosY = entity.posY + 1.35, particlePosZ = entity.posZ;
                            particlePosX = particlePosX + (-Math.sin(Math.toRadians(entity.rotationYaw + 15)) * 0.7);
                            particlePosZ = particlePosZ + (Math.cos(Math.toRadians(entity.rotationYaw + 15)) * 0.7);
                            List<EntityPlayer> listentity = entity.getEntityWorld().getEntitiesWithinAABB(EntityPlayer.class,
                                    new AxisAlignedBB(entity.posX - 16, entity.posY - 16, entity.posZ - 16, entity.posX + 16,
                                            entity.posY + 16, entity.posZ + 16));
                            // System.out.println("listsize:"+listentity.size());
                            if (listentity != null) {
                                for (EntityPlayer i : listentity) {
                                    EntityPlayerMP entityplayermp = (EntityPlayerMP) i;
                                    entityplayermp.connection.sendPacket(new SPacketCustomSound("minecamera:minecamera.kacha",
                                            SoundCategory.BLOCKS, player.posX, player.posY, player.posZ, 1.0F, 1.0F));
                                    MessageSpawnParticle message2 = new MessageSpawnParticle();
                                    message2.delay = 200;
                                    message2.typeid = EnumParticleTypes.FIREWORKS_SPARK.getParticleID();
                                    message2.PosX = particlePosX;
                                    message2.PosY = particlePosY;
                                    message2.PosZ = particlePosZ;
                                    message2.SpeedX = 0;
                                    message2.SpeedY = 0;
                                    message2.SpeedZ = 0;
                                    NetworkLoader.instance.sendTo(message2, entityplayermp);
                                }
                            }
                        }
                    });


                    MessageImageSyncSave message2 = new MessageImageSyncSave();
                    message2.imageName = imagename;
                    NetworkLoader.instance.sendTo(message2, player);
                    return null;
                }
            }
            return null;
        }
    }
}
