package com.porpit.minecamera.crafting;

import com.porpit.minecamera.block.BlockLoader;
import com.porpit.minecamera.item.ItemLoader;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class CraftingLoader
{
    public CraftingLoader()
    {
        registerRecipe();
        registerSmelting();
        registerFuel();
    }

    private static void registerRecipe()
    {
    	GameRegistry.addRecipe(new ItemStack(ItemLoader.itemCamera), new Object[]{
    	                "!@#", "$%@", "@@@", '@', Blocks.obsidian, '!', Blocks.glowstone , '#',Blocks.stone_button,'$',Items.ender_eye,'%',Blocks.redstone_block
    	        });
    	GameRegistry.addShapelessRecipe(new ItemStack(ItemLoader.itemCamera),ItemLoader.itemTripod);
    	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockLoader.pictureFrame), new Object[]{
                "!!", "@@", '!', "plankWood", '@', Blocks.wool
        }));
    	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockLoader.pictureFrameMultiple), new Object[]{
                "!!!", "@@@","@@@", '!', "plankWood", '@', Blocks.wool
        }));
    	GameRegistry.addRecipe(new ItemStack(ItemLoader.itemBattery), new Object[]{
                "!#!", "!@!","!@!", '!', Blocks.redstone_block, '@', Blocks.obsidian,'#',Blocks.stone_button
        });
    	GameRegistry.addRecipe(new ItemStack(ItemLoader.itemFilm), new Object[]{
                "!!!", "@@@","!!!", '!', Items.ender_pearl, '@', Blocks.glass_pane
        });
    	GameRegistry.addRecipe(new ItemStack(BlockLoader.photoprocessor), new Object[]{
                "!@!", "!#!","!$!", '!', Blocks.iron_block, '@', Items.diamond,'#',Items.ender_pearl,'$',Blocks.redstone_block
        });
    	GameRegistry.addShapelessRecipe(new ItemStack(ItemLoader.itemPhotoPaper), Items.milk_bucket,Items.paper,Items.iron_ingot,Items.gold_ingot);
    	GameRegistry.addRecipe(new ItemStack(ItemLoader.itemCntsTempLiquid), new Object[]{
                "!!!", "@!@","@#@", '!', Items.blaze_rod, '@', Blocks.glass_pane,'#',Items.water_bucket
        });
    	GameRegistry.addRecipe(new ItemStack(ItemLoader.itemDevelopingAgent), new Object[]{
                "!@#", "$%$","^&^", '!', new ItemStack(Items.dye, 1, EnumDyeColor.RED.getDyeDamage()), '@',  new ItemStack(Items.dye, 1, EnumDyeColor.YELLOW.getDyeDamage()),'#', new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()),'$',Items.gold_ingot,'%',Items.ender_pearl,'^',Blocks.glass_pane,'&',Items.water_bucket
        });
    	GameRegistry.addRecipe(new ItemStack(ItemLoader.itemTripod), new Object[]{
    			" ! ", " @ ","@#@", '!', ItemLoader.itemCamera, '@', Blocks.iron_block,'#',Items.ender_eye
        });
    	GameRegistry.addRecipe(new ItemStack(ItemLoader.itemGlassesHelmet), new Object[]{
    			"!$!", "@#@","! !", '!', Items.stick, '@', Blocks.glass_pane,'#',Blocks.obsidian,'$',Items.ender_eye
        });
    	GameRegistry.addRecipe(new ItemStack(ItemLoader.itemPictureBook), new Object[]{
    			"!!!", "!@#","!##", '!', Items.paper, '@', Items.slime_ball,'#',Items.leather
        });
    }

    private static void registerSmelting()
    {

    }

    private static void registerFuel()
    {

    }
}
