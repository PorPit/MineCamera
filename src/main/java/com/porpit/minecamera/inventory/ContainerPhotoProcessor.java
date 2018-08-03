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
			i.sendWindowProperty(this, 0, tileEntity.getBurnTime());
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
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index >= 0 && index <= 4)
			{
				if (!this.mergeItemStack(itemstack1, 5, 41, true)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);

			}else if (index >= 5 && index < 32)
			{
				if (!this.mergeItemStack(itemstack1, 0, 4, false)) {
					return ItemStack.EMPTY;
				}
			}else if (index >= 32 && index < 41)
			{
				if (!this.mergeItemStack(itemstack1, 0, 4, false)) {
					return ItemStack.EMPTY;
				}
			}
			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemstack1);
		}
	return itemstack;

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
