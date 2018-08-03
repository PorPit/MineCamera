package com.porpit.minecamera.inventory;

import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Set;

public class ContainerFix extends net.minecraft.inventory.Container {

    private int dragMode = -1;
    private int dragEvent;
    /** The list of slots where the itemstack holds will be distributed */
    private final Set<Slot> dragSlots = Sets.<Slot>newHashSet();
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return false;
    }

    @Override
    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;

        if (reverseDirection)
        {
            i = endIndex - 1;
        }

        if (stack.isStackable())
        {
            while (stack.stackSize > 0 && (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex))
            {
                Slot slot = (Slot)this.inventorySlots.get(i);
                ItemStack itemstack = slot.getStack();

                if (itemstack != null && this.areItemStacksEqual(stack, itemstack))
                {
                    int j = itemstack.stackSize + stack.stackSize;
                    int maxSize = Math.min(slot.getSlotStackLimit(), stack.getMaxStackSize());

                    if (j <= maxSize)
                    {
                        stack.stackSize = 0;
                        itemstack.stackSize = j;
                        slot.onSlotChanged();
                        flag = true;
                    }
                    else if (itemstack.stackSize < maxSize)
                    {
                        stack.stackSize -= maxSize - itemstack.stackSize;
                        itemstack.stackSize = maxSize;
                        slot.onSlotChanged();
                        flag = true;
                    }
                }

                if (reverseDirection)
                {
                    --i;
                }
                else
                {
                    ++i;
                }
            }
        }

        if (stack.stackSize > 0)
        {
            if (reverseDirection)
            {
                i = endIndex - 1;
            }
            else
            {
                i = startIndex;
            }

            while (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex)
            {
                Slot slot1 = (Slot)this.inventorySlots.get(i);
                ItemStack itemstack1 = slot1.getStack();

                if (itemstack1 == null && slot1.isItemValid(stack)) // Forge: Make sure to respect isItemValid in the slot.
                {
                    if (stack.stackSize > slot1.getSlotStackLimit())
                    {
                        slot1.putStack(stack.splitStack(slot1.getSlotStackLimit()));
                    }
                    else
                    {
                        slot1.putStack(stack.splitStack(stack.stackSize));
                    }

                    slot1.onSlotChanged();
                    flag = true;
                    break;
                }

                if (reverseDirection)
                {
                    --i;
                }
                else
                {
                    ++i;
                }
            }
        }
        return flag;
    }

    @Nullable
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        ItemStack itemstack = null;
        InventoryPlayer inventoryplayer = player.inventory;

        if (clickTypeIn == ClickType.QUICK_CRAFT)
        {
            int i = this.dragEvent;
            this.dragEvent = getDragEvent(dragType);

            if ((i != 1 || this.dragEvent != 2) && i != this.dragEvent)
            {
                this.resetDrag();
            }
            else if (inventoryplayer.getItemStack() == null)
            {
                this.resetDrag();
            }
            else if (this.dragEvent == 0)
            {
                this.dragMode = extractDragMode(dragType);

                if (isValidDragMode(this.dragMode, player))
                {
                    this.dragEvent = 1;
                    this.dragSlots.clear();
                }
                else
                {
                    this.resetDrag();
                }
            }
            else if (this.dragEvent == 1)
            {
                Slot slot = (Slot)this.inventorySlots.get(slotId);

                if (slot != null && canAddItemToSlot(slot, inventoryplayer.getItemStack(), true) && slot.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize > this.dragSlots.size() && this.canDragIntoSlot(slot))
                {
                    this.dragSlots.add(slot);
                }
            }
            else if (this.dragEvent == 2)
            {
                if (!this.dragSlots.isEmpty())
                {
                    ItemStack itemstack3 = inventoryplayer.getItemStack().copy();
                    int j = inventoryplayer.getItemStack().stackSize;

                    for (Slot slot1 : this.dragSlots)
                    {
                        if (slot1 != null && canAddItemToSlot(slot1, inventoryplayer.getItemStack(), true) && slot1.isItemValid(inventoryplayer.getItemStack()) && inventoryplayer.getItemStack().stackSize >= this.dragSlots.size() && this.canDragIntoSlot(slot1))
                        {
                            ItemStack itemstack1 = itemstack3.copy();
                            int k = slot1.getHasStack() ? slot1.getStack().stackSize : 0;
                            computeStackSize(this.dragSlots, this.dragMode, itemstack1, k);

                            int k3 = Math.min(itemstack1.getMaxStackSize(), slot1.getItemStackLimit(itemstack1));

                            if (itemstack1.stackSize > k3)
                            {
                                itemstack1.stackSize=(k3);
                            }

                            j -= itemstack1.stackSize - k;
                            slot1.putStack(itemstack1);
                        }
                    }

                    itemstack3.stackSize = j;

                    if (itemstack3.stackSize <= 0)
                    {
                        itemstack3 = null;
                    }

                    inventoryplayer.setItemStack(itemstack3);
                }

                this.resetDrag();
            }
            else
            {
                this.resetDrag();
            }
        }
        else if (this.dragEvent != 0)
        {
            this.resetDrag();
        }
        else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1))
        {
            if (slotId == -999)
            {
                if (inventoryplayer.getItemStack() != null)
                {
                    if (dragType == 0)
                    {
                        player.dropItem(inventoryplayer.getItemStack(), true);
                        inventoryplayer.setItemStack((ItemStack)null);
                    }

                    if (dragType == 1)
                    {
                        player.dropItem(inventoryplayer.getItemStack().splitStack(1), true);

                        if (inventoryplayer.getItemStack().stackSize == 0)
                        {
                            inventoryplayer.setItemStack((ItemStack)null);
                        }
                    }
                }
            }
            else if (clickTypeIn == ClickType.QUICK_MOVE)
            {
                if (slotId < 0)
                {
                    return null;
                }

                Slot slot6 = (Slot)this.inventorySlots.get(slotId);

                if (slot6 != null && slot6.canTakeStack(player))
                {
                    ItemStack itemstack8 = slot6.getStack();

                    if (itemstack8 != null && itemstack8.stackSize <= 0)
                    {
                        itemstack = itemstack8.copy();
                        slot6.putStack((ItemStack)null);
                    }

                    ItemStack itemstack11 = this.transferStackInSlot(player, slotId);

                    if (itemstack11 != null)
                    {
                        Item item = itemstack11.getItem();
                        itemstack = itemstack11.copy();

                        if (slot6.getStack() != null && slot6.getStack().getItem() == item)
                        {
                            this.retrySlotClick(slotId, dragType, true, player);
                        }
                    }
                }
            }
            else
            {
                if (slotId < 0)
                {
                    return null;
                }

                Slot slot7 = (Slot)this.inventorySlots.get(slotId);

                if (slot7 != null)
                {
                    ItemStack itemstack9 = slot7.getStack();
                    ItemStack itemstack12 = inventoryplayer.getItemStack();

                    if (itemstack9 != null)
                    {
                        itemstack = itemstack9.copy();
                    }

                    if (itemstack9 == null)
                    {
                        if (itemstack12 != null && slot7.isItemValid(itemstack12))
                        {
                            int l2 = dragType == 0 ? itemstack12.stackSize : 1;

                            if (l2 > slot7.getItemStackLimit(itemstack12))
                            {
                                l2 = slot7.getItemStackLimit(itemstack12);
                            }

                            slot7.putStack(itemstack12.splitStack(l2));

                            if (itemstack12.stackSize == 0)
                            {
                                inventoryplayer.setItemStack((ItemStack)null);
                            }
                        }
                    }
                    else if (slot7.canTakeStack(player))
                    {
                        if (itemstack12 == null)
                        {
                            if (itemstack9.stackSize > 0)
                            {
                                int k2 = dragType == 0 ? itemstack9.stackSize : (itemstack9.stackSize + 1) / 2;
                                inventoryplayer.setItemStack(slot7.decrStackSize(k2));

                                if (itemstack9.stackSize <= 0)
                                {
                                    slot7.putStack((ItemStack)null);
                                }

                                slot7.onPickupFromSlot(player, inventoryplayer.getItemStack());
                            }
                            else
                            {
                                slot7.putStack((ItemStack)null);
                                inventoryplayer.setItemStack((ItemStack)null);
                            }
                        }
                        else if (slot7.isItemValid(itemstack12))
                        {
                            if (itemstack9.getItem() == itemstack12.getItem() && itemstack9.getMetadata() == itemstack12.getMetadata() && ItemStack.areItemStackTagsEqual(itemstack9, itemstack12))
                            {
                                int j2 = dragType == 0 ? itemstack12.stackSize : 1;

                                if (j2 > slot7.getItemStackLimit(itemstack12) - itemstack9.stackSize)
                                {
                                    j2 = slot7.getItemStackLimit(itemstack12) - itemstack9.stackSize;
                                }

                                if (j2 > itemstack12.getMaxStackSize() - itemstack9.stackSize)
                                {
                                    j2 = itemstack12.getMaxStackSize() - itemstack9.stackSize;
                                }

                                itemstack12.splitStack(j2);

                                if (itemstack12.stackSize == 0)
                                {
                                    inventoryplayer.setItemStack((ItemStack)null);
                                }

                                itemstack9.stackSize += j2;
                            }
                            else if (itemstack12.stackSize <= slot7.getItemStackLimit(itemstack12))
                            {
                                slot7.putStack(itemstack12);
                                inventoryplayer.setItemStack(itemstack9);
                            }
                        }
                        else if (itemstack9.getItem() == itemstack12.getItem() && itemstack12.getMaxStackSize() > 1 && (!itemstack9.getHasSubtypes() || itemstack9.getMetadata() == itemstack12.getMetadata()) && ItemStack.areItemStackTagsEqual(itemstack9, itemstack12))
                        {
                            int i2 = itemstack9.stackSize;

                            if (i2 > 0 && i2 + itemstack12.stackSize <= itemstack12.getMaxStackSize())
                            {
                                itemstack12.stackSize += i2;
                                itemstack9 = slot7.decrStackSize(i2);

                                if (itemstack9.stackSize == 0)
                                {
                                    slot7.putStack((ItemStack)null);
                                }

                                slot7.onPickupFromSlot(player, inventoryplayer.getItemStack());
                            }
                        }
                    }

                    slot7.onSlotChanged();
                }
            }
        }
        else if (clickTypeIn == ClickType.SWAP && dragType >= 0 && dragType < 9)
        {
            Slot slot5 = (Slot)this.inventorySlots.get(slotId);
            ItemStack itemstack7 = inventoryplayer.getStackInSlot(dragType);

            if (itemstack7 != null && itemstack7.stackSize <= 0)
            {
                itemstack7 = null;
                inventoryplayer.setInventorySlotContents(dragType, (ItemStack)null);
            }

            ItemStack itemstack10 = slot5.getStack();

            if (itemstack7 != null || itemstack10 != null)
            {
                if (itemstack7 == null)
                {
                    if (slot5.canTakeStack(player))
                    {
                        inventoryplayer.setInventorySlotContents(dragType, itemstack10);
                        slot5.putStack((ItemStack)null);
                        slot5.onPickupFromSlot(player, itemstack10);
                    }
                }
                else if (itemstack10 == null)
                {
                    if (slot5.isItemValid(itemstack7))
                    {
                        int k1 = slot5.getItemStackLimit(itemstack7);

                        if (itemstack7.stackSize > k1)
                        {
                            slot5.putStack(itemstack7.splitStack(k1));
                        }
                        else
                        {
                            slot5.putStack(itemstack7);
                            inventoryplayer.setInventorySlotContents(dragType, (ItemStack)null);
                        }
                    }
                }
                else if (slot5.canTakeStack(player) && slot5.isItemValid(itemstack7))
                {
                    int l1 = slot5.getItemStackLimit(itemstack7);

                    if (itemstack7.stackSize > l1)
                    {
                        slot5.putStack(itemstack7.splitStack(l1));
                        slot5.onPickupFromSlot(player, itemstack10);

                        if (!inventoryplayer.addItemStackToInventory(itemstack10))
                        {
                            player.dropItem(itemstack10, true);
                        }
                    }
                    else
                    {
                        slot5.putStack(itemstack7);
                        inventoryplayer.setInventorySlotContents(dragType, itemstack10);
                        slot5.onPickupFromSlot(player, itemstack10);
                    }
                }
            }
        }
        else if (clickTypeIn == ClickType.CLONE && player.capabilities.isCreativeMode && inventoryplayer.getItemStack() == null && slotId >= 0)
        {
            Slot slot4 = (Slot)this.inventorySlots.get(slotId);

            if (slot4 != null && slot4.getHasStack())
            {
                if (slot4.getStack().stackSize > 0)
                {
                    ItemStack itemstack6 = slot4.getStack().copy();
                    itemstack6.stackSize = itemstack6.getMaxStackSize();
                    inventoryplayer.setItemStack(itemstack6);
                }
                else
                {
                    slot4.putStack((ItemStack)null);
                }
            }
        }
        else if (clickTypeIn == ClickType.THROW && inventoryplayer.getItemStack() == null && slotId >= 0)
        {
            Slot slot3 = (Slot)this.inventorySlots.get(slotId);

            if (slot3 != null && slot3.getHasStack() && slot3.canTakeStack(player))
            {
                ItemStack itemstack5 = slot3.decrStackSize(dragType == 0 ? 1 : 1);
                slot3.onPickupFromSlot(player, itemstack5);
                player.dropItem(itemstack5, true);
            }
        }
        else if (clickTypeIn == ClickType.PICKUP_ALL && slotId >= 0)
        {
            Slot slot2 = (Slot)this.inventorySlots.get(slotId);
            ItemStack itemstack4 = inventoryplayer.getItemStack();

            if (itemstack4 != null && (slot2 == null || !slot2.getHasStack() || !slot2.canTakeStack(player)))
            {
                int i1 = dragType == 0 ? 0 : this.inventorySlots.size() - 1;
                int j1 = dragType == 0 ? 1 : -1;

                for (int i3 = 0; i3 < 2; ++i3)
                {
                    for (int j3 = i1; j3 >= 0 && j3 < this.inventorySlots.size() && itemstack4.stackSize < itemstack4.getMaxStackSize(); j3 += j1)
                    {
                        Slot slot8 = (Slot)this.inventorySlots.get(j3);

                        if (slot8.getHasStack() && canAddItemToSlot(slot8, itemstack4, true) && slot8.canTakeStack(player) && this.canMergeSlot(itemstack4, slot8) && (i3 != 0 || slot8.getStack().stackSize != slot8.getStack().getMaxStackSize()))
                        {
                            int l = Math.min(itemstack4.getMaxStackSize() - itemstack4.stackSize, slot8.getStack().stackSize);
                            ItemStack itemstack2 = slot8.decrStackSize(l);
                            itemstack4.stackSize += l;

                            if (itemstack2.stackSize <= 0)
                            {
                                slot8.putStack((ItemStack)null);
                            }

                            slot8.onPickupFromSlot(player, itemstack2);
                        }
                    }
                }
            }

            this.detectAndSendChanges();
        }

        return itemstack;
    }
    @Override
    protected void resetDrag()
    {
        this.dragEvent = 0;
        this.dragSlots.clear();
    }
    private static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB)
    {
        return stackB.getItem() == stackA.getItem() && (!stackA.getHasSubtypes() || stackA.getMetadata() == stackB.getMetadata()) && ItemStack.areItemStackTagsEqual(stackA, stackB);
    }
}
