package com.porpit.minecamera.inventory;

import com.porpit.minecamera.item.ItemLoader;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerPictureBook extends Container{

	protected Slot pictureInSlot;
	protected Slot pictureOutSlot;
	IItemHandler items=new ItemStackHandler(102);
	public ContainerPictureBook(EntityPlayer player) {
		// TODO 自动生成的构造函数存根
		super();
		this.addSlotToContainer(this.pictureInSlot= new SlotItemHandler(items, 0, 224, 10) {

			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack != null && stack.getItem() == ItemLoader.itemPicture&& super.isItemValid(stack);
			}
			
			@Override
			public int getItemStackLimit(ItemStack stack) {
				return 64;
			}
		});

		this.addSlotToContainer(this.pictureOutSlot = new SlotItemHandler(items, 1, 224, 62) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}
			@Override
			public int getItemStackLimit(ItemStack stack) {
				return 64;
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
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		 Slot slot = inventorySlots.get(index);

	        if (slot == null || !slot.getHasStack())
	        {
	            return null;
	        }

	        ItemStack newStack = slot.getStack(), oldStack = newStack.copy();
	        boolean isMerged = false;
	        
	        if (index >= 0 && index <= 1)
	        {
	            isMerged = mergeItemStack(newStack, 3, 39, true);
	        }else if (index >= 2 && index < 29)
	        {
	        	isMerged = ! pictureInSlot.getHasStack() && newStack.stackSize <= 1 && mergeItemStack(newStack, 0, 1, false)
		                    || mergeItemStack(newStack, 29, 38, false);
	        }else if (index >= 29 && index < 38)
	        {
	        	isMerged = !pictureInSlot.getHasStack()&& newStack.stackSize <= 1&& mergeItemStack(newStack, 0, 1, false)
	                    || mergeItemStack(newStack, 2, 29, false);
	        }
	        
	        if (!isMerged)
	        {
	            return null;
	        }

	        if (newStack.stackSize == 0)
	        {
	            slot.putStack(null);
	        }
	        else
	        {
	            slot.onSlotChanged();
	        }
	        slot.onPickupFromSlot(playerIn, newStack);
	        return oldStack;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		// TODO 自动生成的方法存根
		return true;
	}

}
