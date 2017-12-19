package com.porpit.minecamera.tileentity;

import net.minecraft.nbt.NBTTagCompound;
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
}
