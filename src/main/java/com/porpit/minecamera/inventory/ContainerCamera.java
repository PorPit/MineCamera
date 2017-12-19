package com.porpit.minecamera.inventory;

import javax.annotation.Nullable;

import com.porpit.minecamera.entity.EntityTripod;
import com.porpit.minecamera.item.ItemLoader;
import com.porpit.minecamera.network.MessageCameraDelaySet;
import com.porpit.minecamera.network.NetworkLoader;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCamera extends Container {
	protected EntityPlayer player;
	protected Slot betterySlot;
	protected Slot filmSlot;
	protected Slot filmOutSlot;
	protected ItemStack filmOutCatch;
	public static int totalBurnTime = 100;
	protected int burnTime = totalBurnTime;
	private int delay = 0;
	protected IItemHandler items;// = new ItemStackHandler(3);
	private int type;

	public ContainerCamera(EntityPlayer player, int type) {
		super();
		this.player = player;
		this.type = type;
		if (type == 0) {
			items = new ItemStackHandler(3);
		} else if (type == 1
				&& player.getEntityWorld().getEntityByID(player.getEntityData().getInteger("usingGui")) != null) {
			Entity entity = player.getEntityWorld().getEntityByID(player.getEntityData().getInteger("usingGui"));
			items = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, player.getHorizontalFacing());
		}
		this.addSlotToContainer(this.betterySlot = new SlotItemHandler(items, 0, 26, 20) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack != null && stack.getItem() == ItemLoader.itemBattery && super.isItemValid(stack);
			}

			@Override
			public int getItemStackLimit(ItemStack stack) {
				return 1;
			}
		});

		this.addSlotToContainer(this.filmSlot = new SlotItemHandler(items, 1, 62, 20) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack != null && stack.getItem() == ItemLoader.itemFilm && super.isItemValid(stack);
			}

			@Override
			public int getItemStackLimit(ItemStack stack) {
				return 1;
			}
		});

		this.addSlotToContainer(this.filmOutSlot = new SlotItemHandler(items, 2, 134, 20) {

			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}
		});
		if (!player.getEntityWorld().isRemote) {
			if (type == 0) {
				ItemStack cameraStack = player.getActiveItemStack();
				if (!cameraStack.hasTagCompound()) {
					NBTTagCompound newnbt = new NBTTagCompound();
					cameraStack.setTagCompound(newnbt);
				} else {
					if (cameraStack.getTagCompound().hasKey("betterySlot")) {
						/*ItemStack betteryStack = ItemStack
								.loadItemStackFromNBT(cameraStack.getTagCompound().getCompoundTag("betterySlot"));*/
						ItemStack betteryStack=new ItemStack(ItemLoader.itemBattery);
						betteryStack.deserializeNBT(cameraStack.getTagCompound().getCompoundTag("betterySlot"));
						this.betterySlot.putStack(betteryStack);
					}
					if (cameraStack.getTagCompound().hasKey("filmSlot")) {
						/*ItemStack filmStack = ItemStack
								.loadItemStackFromNBT(cameraStack.getTagCompound().getCompoundTag("filmSlot"));*/
						ItemStack filmStack=new ItemStack(ItemLoader.itemFilm);
						filmStack.deserializeNBT(cameraStack.getTagCompound().getCompoundTag("filmSlot"));
						this.filmSlot.putStack(filmStack);
					}
					if (cameraStack.getTagCompound().hasKey("filmOutSlot")) {
						/*ItemStack filmStackStack = ItemStack
								.loadItemStackFromNBT(cameraStack.getTagCompound().getCompoundTag("filmOutSlot"));*/
						ItemStack filmStackStack=new ItemStack(ItemLoader.itemFilm);
						filmStackStack.deserializeNBT(cameraStack.getTagCompound().getCompoundTag("filmOutSlot"));
						this.filmOutSlot.putStack(filmStackStack);
					}
					System.out.println(cameraStack.getTagCompound());
					if (cameraStack.getTagCompound().hasKey("filmOutCatch")) {
						/*filmOutCatch = ItemStack
								.loadItemStackFromNBT(cameraStack.getTagCompound().getCompoundTag("filmOutCatch"));*/
						filmOutCatch=new ItemStack(ItemLoader.itemFilm);
						filmOutCatch.deserializeNBT(cameraStack.getTagCompound().getCompoundTag("filmOutCatch"));
					}
					if (cameraStack.getTagCompound().hasKey("burnTime")) {
						this.burnTime = cameraStack.getTagCompound().getInteger("burnTime");
					} else {
						cameraStack.getTagCompound().setInteger("burnTime", this.burnTime);
					}
				}
			} else if (type == 1
					&& player.getEntityWorld().getEntityByID(player.getEntityData().getInteger("usingGui")) != null) {
				EntityTripod entity = (EntityTripod) player.getEntityWorld()
						.getEntityByID(player.getEntityData().getInteger("usingGui"));
				this.filmOutCatch = items.getStackInSlot(3);
				this.burnTime = entity.getBurnTime();

				//System.out.println("recodeid:" + entity.getEntityId());
			}
		}
		//System.out.println("time" + burnTime);

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 51 + i * 18));
			}
		}

		for (int i = 0; i < 9; ++i) {
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 109));
		}

	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		// System.out.println(player.getEntityWorld().isRemote);
		for (IContainerListener i : this.listeners) {
			if (this.burnTime < this.totalBurnTime) {
				this.burnTime++;
				if (this.burnTime == this.totalBurnTime) {
					
					if (filmOutCatch != null&&!filmOutCatch.isEmpty()) {
						System.out.println(filmOutCatch);
						filmOutSlot.putStack(filmOutCatch);
						if (type == 0) {
							NBTTagCompound itemTag = new NBTTagCompound();
							filmOutCatch.writeToNBT(itemTag);
							player.getActiveItemStack().getTagCompound().setTag("filmOutSlot", itemTag);
							player.getActiveItemStack().getTagCompound().removeTag("filmOutCatch");
						}
						if (type == 1) {
							items.extractItem(3, 64, false);
						}
						filmOutCatch = null;
					}
				}
				i.sendWindowProperty(this, 0, this.burnTime);
				if (type == 1 && ((EntityTripod) player.getEntityWorld()
						.getEntityByID(player.getEntityData().getInteger("usingGui"))) != null)
					((EntityTripod) player.getEntityWorld()
							.getEntityByID(player.getEntityData().getInteger("usingGui"))).setBurnTime(this.burnTime);
			}
			if (type == 1 && ((EntityTripod) player.getEntityWorld()
					.getEntityByID(player.getEntityData().getInteger("usingGui")) != null)) {
				i.sendWindowProperty(this, 1, ((EntityTripod) player.getEntityWorld()
						.getEntityByID(player.getEntityData().getInteger("usingGui"))).getDelay());
			}
		}

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data) {
		super.updateProgressBar(id, data);
		// System.out.println(this.delay);
		switch (id) {
		case 0:
			this.burnTime = data;
			if (type == 1 && ((EntityTripod) player.getEntityWorld()
					.getEntityByID(player.getEntityData().getInteger("usingGui")) != null)) {
				((EntityTripod) player.getEntityWorld().getEntityByID(player.getEntityData().getInteger("usingGui")))
						.setBurnTime(data);
			}
			break;
		case 1:
			this.delay = data;
			if (type == 1 && ((EntityTripod) player.getEntityWorld()
					.getEntityByID(player.getEntityData().getInteger("usingGui")) != null)) {
				((EntityTripod) player.getEntityWorld().getEntityByID(player.getEntityData().getInteger("usingGui")))
						.setDelay(data);
			}
			break;
		default:
			break;
		}
	}

	public int getBurnTime() {
		return this.burnTime;
	}

	public int getTotalBurnTime() {
		return totalBurnTime;
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
		if(!playerIn.getEntityWorld().isRemote){
			saveToNBT(playerIn);
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
	        
	        if (index >= 0 && index <= 2)
	        {
	            isMerged = mergeItemStack(newStack, 3, 39, true);
	        }else if (index >= 3 && index < 30)
	        {
	            isMerged = ! betterySlot.getHasStack() && newStack.getCount() <= 1 && mergeItemStack(newStack, 0, 1, false)
	                    || !filmSlot.getHasStack() && mergeItemStack(newStack, 1, 2, false)
	                    || mergeItemStack(newStack, 30, 39, false);
	        }else if (index >= 30 && index < 39)
	        {
	            isMerged = !betterySlot.getHasStack()&& newStack.getCount() <= 1&& mergeItemStack(newStack, 0, 1, false)
	                    || !filmSlot.getHasStack() && mergeItemStack(newStack, 1, 2, false)
	                    || mergeItemStack(newStack, 3, 30, false);
	        }
	        
	        if (!isMerged)
	        {
	            return null;
	        }

	        if (newStack.getMaxStackSize() == 0)
	        {
	            slot.putStack(null);
	        }
	        else
	        {
	            slot.onSlotChanged();
	        }
	        //slot.onPickupFromSlot(playerIn, newStack);
	        slot.onSlotChanged();
	        return oldStack;
	}

	@Override
	@Nullable
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		ItemStack i = super.slotClick(slotId, dragType, clickTypeIn, player);
		saveToNBT(player);
		return i;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		if (type == 0) {
			return new ItemStack(ItemLoader.itemCamera).isItemEqual(playerIn.getActiveItemStack());
		} else {
			return true;
		}
	}

	private void saveToNBT(EntityPlayer playerIn) {
		if (!playerIn.getEntityWorld().isRemote) {
			ItemStack cameraStack = playerIn.getActiveItemStack();
			if (cameraStack == null) {
				return;
			}
			ItemStack betteryStack = this.betterySlot.getStack();
			if (cameraStack.getTagCompound() == null) {
				NBTTagCompound newnbt = new NBTTagCompound();
				cameraStack.setTagCompound(newnbt);
			}
			if (betteryStack != null&&!betteryStack.isEmpty()) {
				NBTTagCompound itemTag = new NBTTagCompound();
				betteryStack.writeToNBT(itemTag);
				cameraStack.getTagCompound().setTag("betterySlot", itemTag);
			} else {
				cameraStack.getTagCompound().removeTag("betterySlot");
			}
			ItemStack filmStack = this.filmSlot.getStack();
			if (filmStack != null&&!filmStack.isEmpty()) {
				NBTTagCompound itemTag = new NBTTagCompound();
				filmStack.writeToNBT(itemTag);
				cameraStack.getTagCompound().setTag("filmSlot", itemTag);
			} else {
				cameraStack.getTagCompound().removeTag("filmSlot");
			}
			ItemStack filmOutStack = this.filmOutSlot.getStack();
			if (filmOutStack != null&&!filmOutStack.isEmpty()) {
				NBTTagCompound itemTag = new NBTTagCompound();
				filmOutStack.writeToNBT(itemTag);
				cameraStack.getTagCompound().setTag("filmOutSlot", itemTag);
			} else {
				cameraStack.getTagCompound().removeTag("filmOutSlot");
			}
			cameraStack.getTagCompound().setInteger("burnTime", this.burnTime);
			if (filmOutCatch != null&&!filmOutCatch.isEmpty()) {
				NBTTagCompound itemTag = new NBTTagCompound();
				filmOutCatch.writeToNBT(itemTag);
				cameraStack.getTagCompound().setTag("filmOutCatch", itemTag);
			} else {
				cameraStack.getTagCompound().removeTag("filmOutCatch");
			}
		}
	}

	public int getType() {
		return type;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		if (delay <= 30 && delay >= 0) {
			MessageCameraDelaySet message = new MessageCameraDelaySet();
			message.delay = delay;
			NetworkLoader.instance.sendToServer(message);
		}
	}
}
