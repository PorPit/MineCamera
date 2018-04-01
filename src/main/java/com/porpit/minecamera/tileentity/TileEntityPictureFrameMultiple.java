package com.porpit.minecamera.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPictureFrameMultiple extends TileEntityPictureFrame {
	
	public boolean shouldrender;
	public int width;
	public int height;
	
	public TileEntityPictureFrameMultiple() {
		shouldrender=false;
		width=2;
		height=2;
	}
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.shouldrender=compound.getBoolean("shouldrender");
		this.width = compound.getInteger("width");
		this.height=compound.getInteger("height");
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setBoolean("shouldrender", shouldrender);
		compound.setInteger("width", width);
		compound.setInteger("height", height);
	}
	
	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("imagename", this.imagename);
		nbt.setBoolean("shouldrender", shouldrender);
		nbt.setInteger("width", width);
		nbt.setInteger("height", height);
        return new S35PacketUpdateTileEntity(pos, getBlockMetadata(), nbt);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
		this.shouldrender=pkt.getNbtCompound().getBoolean("shouldrender");
		this.width = pkt.getNbtCompound().getInteger("width");
		this.height=pkt.getNbtCompound().getInteger("height");
		super.onDataPacket(net, pkt);
    }
}
