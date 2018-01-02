package com.porpit.minecamera.tileentity;

import javax.annotation.Nullable;

import com.porpit.minecamera.util.LoadImageFileThread;
import com.porpit.minecamera.util.PictureFactory;
import com.porpit.minecamera.util.VideoMemoryCleaner;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent.Tick;

public class TileEntityPictureFrame extends TileEntity {

	private int timer;
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
		timer++;
		boolean isLoaded=PictureFactory.loadedPicture.containsKey(imagename);
		if(isLoaded){
			textureID=PictureFactory.loadedPicture.get(imagename);
			if(timer>=Minecraft.getDebugFPS()){
				timer=0;
				VideoMemoryCleaner.activeTexture.add(PictureFactory.loadedPicture.get(imagename));
			}
		}else{
			textureID=-1;
		}
		return isLoaded;
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
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("imagename", this.imagename);
		return compound;
	}

	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		//System.out.println("update");
		return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}
}
