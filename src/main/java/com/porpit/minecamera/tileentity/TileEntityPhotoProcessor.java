package com.porpit.minecamera.tileentity;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityPhotoProcessor extends TileEntity implements ITickable {
	public static int totalburnTime = 440;
	protected int burnTime = totalburnTime;
	protected ItemStackHandler Inventory = new ItemStackHandler(6);

	public TileEntityPhotoProcessor() {
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(capability)) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(capability)) {
			@SuppressWarnings("unchecked")
			T result = (T) Inventory;
			return result;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.Inventory.deserializeNBT(compound.getCompoundTag("Inventory"));
		this.burnTime = compound.getInteger("BurnTime");
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("Inventory", this.Inventory.serializeNBT());
		compound.setInteger("BurnTime", this.burnTime);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

	public int getBurnTime() {
		return burnTime;
	}

	@Override
	public void update() {

		if (!this.getWorld().isRemote) {
			if (this.burnTime < this.totalburnTime) {
				this.burnTime++;
				if (this.burnTime == this.totalburnTime) {
					Inventory.setStackInSlot(4, Inventory.getStackInSlot(5));
					Inventory.extractItem(5, 64, false);
				}
			}
		}
	}

/*	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}*/

	public void setBurnTime(int i) {
		this.burnTime = i;
	}

}
