package com.porpit.minecamera.tileentity;

import javax.annotation.Nullable;

import com.porpit.minecamera.util.LoadImageFileThread;
import com.porpit.minecamera.util.PictureFactory;
import com.porpit.minecamera.util.VideoMemoryCleaner;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("imagename", this.imagename);
	}
	 
	@Override
	public Packet getDescriptionPacket(){
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("imagename", this.imagename);
        return new S35PacketUpdateTileEntity(pos, getBlockMetadata(), nbt);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
		this.imagename = pkt.getNbtCompound().getString("imagename");
		worldObj.markBlockRangeForRenderUpdate(pos, pos);
    }
	
	public void updateBlock()
	{
		if(!worldObj.isRemote)
		{
			IBlockState state = worldObj.getBlockState(pos);
			worldObj.markBlockForUpdate(pos);
			markDirty();
		}
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
