package com.porpit.minecamera.inventory;

import javax.annotation.Nullable;

import com.porpit.minecamera.item.ItemLoader;
import com.porpit.minecamera.tileentity.TileEntityPhotoProcessor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerPhotoProcessor extends Container {

	protected IItemHandler AllSlot;
	public static int totalBurnTime = TileEntityPhotoProcessor.totalburnTime;
	protected int burnTime = totalBurnTime;
	// private ItemStackHandler items = new ItemStackHandler(5);
	protected EntityPlayer player;
	protected TileEntityPhotoProcessor tileEntity;

	public ContainerPhotoProcessor(EntityPlayer player, TileEntity tileEntity) {
		super();
		this.tileEntity = (TileEntityPhotoProcessor) tileEntity;
		this.player = player;
		this.burnTime = this.tileEntity.getBurnTime();
		// System.out.println(player.getEntityWorld().provider.getDimension());
		AllSlot = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, player.getHorizontalFacing());
		this.addSlotToContainer(new SlotItemHandler(AllSlot, 0, 9, 29) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack != null && stack.getItem() == ItemLoader.itemCntsTempLiquid && super.isItemValid(stack);
			}

			@Override
			public int getItemStackLimit(ItemStack stack) {
				return 10;
			}
		});
		this.addSlotToContainer(new SlotItemHandler(AllSlot, 1, 9, 59) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack != null && stack.getItem() == ItemLoader.itemDevelopingAgent && super.isItemValid(stack);
			}

			@Override
			public int getItemStackLimit(ItemStack stack) {
				return 10;
			}
		});
		this.addSlotToContainer(new SlotItemHandler(AllSlot, 2, 44, 101) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack != null && stack.getItem() == ItemLoader.itemFilm && super.isItemValid(stack);
			}

			@Override
			public int getItemStackLimit(ItemStack stack) {
				return 1;
			}
		});
		this.addSlotToContainer(new SlotItemHandler(AllSlot, 3, 80, 101) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack != null && stack.getItem() == ItemLoader.itemPhotoPaper && super.isItemValid(stack);
			}

			@Override
			public int getItemStackLimit(ItemStack stack) {
				return 64;
			}
		});
		this.addSlotToContainer(new SlotItemHandler(AllSlot, 4, 152, 101) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}
		});
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 124 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 182));
		}
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		// System.out.println(tileEntity.getBurnTime());
		for (IContainerListener i : this.listeners) {
			i.sendProgressBarUpdate(this, 0, tileEntity.getBurnTime());
		}

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data) {
		super.updateProgressBar(id, data);

		switch (id) {
		case 0:
			this.burnTime = data;
			break;
		default:
			break;
		}
	}

	public int getBurnTime() {
		return this.burnTime;
	}

	public TileEntityPhotoProcessor getTileEntity() {
		return tileEntity;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
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
	        
	        if (index >= 0 && index <= 4)
	        {
	            isMerged = mergeItemStack(newStack, 3, 39, true);
	        }else if (index >= 5 && index < 32)
	        {
	            isMerged = AllSlot.getStackInSlot(0)==null && newStack.stackSize <= 10 && mergeItemStack(newStack, 0, 1, false)
	                    || AllSlot.getStackInSlot(1)==null && newStack.stackSize <= 10 && mergeItemStack(newStack, 1, 2, false)
	                    || AllSlot.getStackInSlot(2)==null && newStack.stackSize <= 1 && mergeItemStack(newStack, 2, 3, false)
	                    || mergeItemStack(newStack, 3, 4, false)
	                    || mergeItemStack(newStack, 32, 41, false);
	        }else if (index >= 32 && index < 41)
	        {
	        	isMerged = AllSlot.getStackInSlot(0)==null && newStack.stackSize <= 10 && mergeItemStack(newStack, 0, 1, false)
	                    || AllSlot.getStackInSlot(1)==null && newStack.stackSize <= 10 && mergeItemStack(newStack, 1, 2, false)
	                    || AllSlot.getStackInSlot(2)==null && newStack.stackSize <= 1 && mergeItemStack(newStack, 2, 3, false)
	                    || mergeItemStack(newStack, 3, 4, false)
	                    || mergeItemStack(newStack, 5, 32, false);
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
	@Nullable
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		// System.out.println(slotId);
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

	public EntityPlayer getPlayer() {
		return player;
	}
}
