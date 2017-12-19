package com.porpit.minecamera.tileentity;

import javax.annotation.Nullable;

import com.porpit.minecamera.util.LoadImageFileThread;
import com.porpit.minecamera.util.PictureFactory;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TileEntityPictureFrame extends TileEntity {

	public int textureID;
	public String imagename;

	public TileEntityPictureFrame() {
		textureID = -1;
		imagename = "";
	}

	public boolean hasImageName() {
		return !imagename.equals("");
	}

	public boolean canLoadImage() {
		return !PictureFactory.fildToLoadPicture.containsKey(imagename)
				&& !PictureFactory.lodingPicture.contains(imagename);
	}

	public boolean isFailed(){
		return PictureFactory.fildToLoadPicture.containsKey(imagename);
	}
	
	public boolean istextureLoaded() {
		if (PictureFactory.loadedPicture.containsKey(imagename)) {
			textureID = PictureFactory.loadedPicture.get(imagename);
		}
		return PictureFactory.loadedPicture.containsKey(imagename);
	}

	public void loadImage() {
		PictureFactory.lodingPicture.add(imagename);
		LoadImageFileThread thread = new LoadImageFileThread(imagename);
		thread.start();
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.imagename = compound.getString("imagename");
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("imagename", this.imagename);
	}

/*	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		//System.out.println("update");
		return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}*/
}
