package com.porpit.minecamera.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.porpit.minecamera.item.ItemLoader;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerPictureBook extends Container {
	protected EntityPlayer player;
	protected Slot pictureInSlot;
	protected Slot pictureOutSlot;
	IItemHandler items = new ItemStackHandler(2);
	protected int index;
	protected int lastindex;
	protected int totalPictureNum;
	protected List<String> listPid = new ArrayList<String>();

	public ContainerPictureBook(EntityPlayer player) {
		super();
		this.player = player;
		this.index = 0;
		this.totalPictureNum = 0;
		this.addSlotToContainer(this.pictureInSlot = new SlotItemHandler(items, 0, 224, 10) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack != null && stack.getItem() == ItemLoader.itemPicture && super.isItemValid(stack);
			}

			@Override
			public int getItemStackLimit(ItemStack stack) {
				return 1;
			}
		});

		this.addSlotToContainer(this.pictureOutSlot = new SlotItemHandler(items, 1, 224, 62) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}

			@Override
			public int getItemStackLimit(ItemStack stack) {
				return 1;
			}
		});
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 60 + j * 18, 138 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(player.inventory, i, 60 + i * 18, 196));
		}
		if (!player.getEntityWorld().isRemote) {
			ItemStack itemStackBook = player.getActiveItemStack();
			if (itemStackBook.hasTagCompound() && itemStackBook.getTagCompound().hasKey("listPid")
					&&!itemStackBook.getTagCompound().getString("listPid").equals("")&& itemStackBook.getTagCompound().hasKey("index")) {
				//System.out.println(itemStackBook.getTagCompound());
				for(String i:itemStackBook.getTagCompound().getString("listPid").split("%,%")){
					listPid.add(i);
				}
				if ( listPid.size() >= 1) {
					totalPictureNum = listPid.size();
					index = itemStackBook.getTagCompound().getInteger("index");
					if (index + 1 > totalPictureNum) {
						index = totalPictureNum-1;
						itemStackBook.getTagCompound().setInteger("index", index);
					}
					String imageName = listPid.get(index);
					ItemStack itemPicture = new ItemStack(ItemLoader.itemPicture);
					NBTTagCompound nbt = new NBTTagCompound();
					nbt.setString("pid", imageName);
					itemPicture.setTagCompound(nbt);
					pictureOutSlot.putStack(itemPicture);
				}
				//System.out.println(totalPictureNum);
			} else {
				totalPictureNum = 0;
				index = -1;
			}
			lastindex=index;
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
		if(!playerIn.getEntityWorld().isRemote){
			saveToNBT(playerIn);
		}
	}
	
	@Nullable
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		if (!player.getEntityWorld().isRemote) {
			if (slotId == 1 && getSlot(1) != null && getSlot(1).getHasStack()) {
				listPid.remove(index);
				if (index +1>=totalPictureNum) {
					index--;
				}
				totalPictureNum--;
				if(totalPictureNum==0){
					index=-1;
				}
			}
		}
		ItemStack i = super.slotClick(slotId, dragType, clickTypeIn, player);
		saveToNBT(player);
		return i;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		if(lastindex!=index){
			pictureOutSlot.decrStackSize(64);
			lastindex=index;
		}
		if(totalPictureNum!=0&&!pictureOutSlot.getHasStack()&&index!=-1){
			String imageName = listPid.get(index);
			ItemStack itemPicture = new ItemStack(ItemLoader.itemPicture);
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("pid", imageName);
			itemPicture.setTagCompound(nbt);
			pictureOutSlot.putStack(itemPicture);
		}
		// System.out.println(tileEntity.getBurnTime());
		for (IContainerListener i : this.listeners) {
			i.sendProgressBarUpdate(this, 0, totalPictureNum);
			i.sendProgressBarUpdate(this, 1, index);
		}

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data) {
		super.updateProgressBar(id, data);
		switch (id) {
		case 0:
			this.totalPictureNum = data;
			break;
		case 1:
			this.index = data;
			break;
		default:
			break;
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		Slot slot = inventorySlots.get(index);
		//System.out.println(index);
		if (slot == null || !slot.getHasStack()) {
			return null;
		}

		ItemStack newStack = slot.getStack(), oldStack = newStack.copy();
		boolean isMerged = false;

		if (index >= 0 && index <= 1) {
			isMerged = mergeItemStack(newStack, 2, 38, true);
		} else if (index >= 2 && index < 29) {
			isMerged = !pictureInSlot.getHasStack() && newStack.getCount() <= 1 && mergeItemStack(newStack, 0, 1, false)
					|| mergeItemStack(newStack, 29, 38, false);
		} else if (index >= 29 && index < 38) {
			isMerged = !pictureInSlot.getHasStack() && newStack.getCount() <= 1 && mergeItemStack(newStack, 0, 1, false)
					|| mergeItemStack(newStack, 2, 29, false);
		}

		if (!isMerged) {
			return null;
		}

		if (newStack.getCount() == 0) {
			slot.putStack(null);
		} else {
			slot.onSlotChanged();
		}
		//slot.onPickupFromSlot(playerIn, newStack);
        slot.onSlotChanged();
		return oldStack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return new ItemStack(ItemLoader.itemPictureBook).isItemEqual(playerIn.getActiveItemStack());
	}

	public int getIndex() {
		return index;
	}

	public int getTotalPictureNum() {
		return totalPictureNum;
	}

	public List<String> getListPid() {
		return listPid;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setTotalPictureNum(int totalPictureNum) {
		this.totalPictureNum = totalPictureNum;
	}

	public void setListPid(List<String> listPid) {
		this.listPid = listPid;
	}
	
	public static int extractDragMode(int eventButton)
    {
		//System.out.println("button"+eventButton);
        return eventButton >> 2 & 3;
    }
	
	private void saveToNBT(EntityPlayer playerIn) {
		if (!playerIn.getEntityWorld().isRemote) {
			String listPidTotal="";
			for(int i=0;i<listPid.size();i++){
				listPidTotal+=listPid.get(i);
				if(i+1<listPid.size()){
					listPidTotal+="%,%";
				}
			}
			//System.out.println(listPid.size());
			ItemStack itemStackBook = playerIn.getActiveItemStack();
			if (itemStackBook != null) {
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("listPid", listPidTotal);
				nbt.setInteger("index", index);
				itemStackBook.setTagCompound(nbt);
			}
		}
	}
}
