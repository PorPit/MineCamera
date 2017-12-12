package com.porpit.minecamera.item;

import java.util.List;

import com.porpit.minecamera.MineCamera;
import com.porpit.minecamera.creativetab.CreativeTabsLoader;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGlassesHelmet extends ItemArmor
{
    public static final ItemArmor.ArmorMaterial GALSSES_ARMOR = EnumHelper.addArmorMaterial("GLASSES",
            MineCamera.MODID + ":" + "glasses", 10, new int[]
            { 2, 6, 4, 2 }, 10,SoundEvent.REGISTRY.getObject(new ResourceLocation("item.armor.equip_iron")),10);

    public ItemGlassesHelmet()
    {
        super(GALSSES_ARMOR, GALSSES_ARMOR.ordinal(), EntityEquipmentSlot.HEAD);
        this.setUnlocalizedName("glasses_helmet");
        this.setCreativeTab(CreativeTabsLoader.tabMineCamera);
        this.setMaxDamage(500);
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add(TextFormatting.BLUE + I18n.format("lore.glasses_helmet.info"));
		tooltip.add(TextFormatting.DARK_GRAY + I18n.format("lore.glasses_helmet.info2"));
	}
}