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
    	                "!@#", "$%@", "@@@", '@', Blocks.OBSIDIAN, '!', Blocks.GLOWSTONE , '#',Blocks.STONE_BUTTON,'$',Items.ENDER_EYE,'%',Blocks.REDSTONE_BLOCK
    	        });
    	GameRegistry.addShapelessRecipe(new ItemStack(ItemLoader.itemCamera),ItemLoader.itemTripod);
    	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockLoader.pictureFrame), new Object[]{
                "!!", "@@", '!', "plankWood", '@', Blocks.WOOL
        }));
    	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BlockLoader.pictureFrameMultiple), new Object[]{
                "!!!", "@@@","@@@", '!', "plankWood", '@', Blocks.WOOL
        }));
    	GameRegistry.addRecipe(new ItemStack(ItemLoader.itemBattery), new Object[]{
                "!#!", "!@!","!@!", '!', Blocks.REDSTONE_BLOCK, '@', Blocks.OBSIDIAN,'#',Blocks.STONE_BUTTON
        });
    	GameRegistry.addRecipe(new ItemStack(ItemLoader.itemFilm), new Object[]{
                "!!!", "@@@","!!!", '!', Items.ENDER_PEARL, '@', Blocks.GLASS_PANE
        });
    	GameRegistry.addRecipe(new ItemStack(BlockLoader.photoprocessor), new Object[]{
                "!@!", "!#!","!$!", '!', Blocks.IRON_BLOCK, '@', Items.DIAMOND,'#',Items.ENDER_PEARL,'$',Blocks.REDSTONE_BLOCK
        });
    	GameRegistry.addShapelessRecipe(new ItemStack(ItemLoader.itemPhotoPaper), Items.MILK_BUCKET,Items.PAPER);
    	GameRegistry.addRecipe(new ItemStack(ItemLoader.itemCntsTempLiquid), new Object[]{
                "!!!", "@!@","@#@", '!', Items.BLAZE_ROD, '@', Blocks.GLASS_PANE,'#',Items.WATER_BUCKET
        });
    	GameRegistry.addRecipe(new ItemStack(ItemLoader.itemDevelopingAgent), new Object[]{
                "!@#", "$%$","^&^", '!', new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()), '@',  new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()),'#', new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()),'$',Items.GOLD_INGOT,'%',Items.ENDER_PEARL,'^',Blocks.GLASS_PANE,'&',Items.WATER_BUCKET
        });
    	GameRegistry.addRecipe(new ItemStack(ItemLoader.itemTripod), new Object[]{
    			" ! ", " @ ","@#@", '!', ItemLoader.itemCamera, '@', Blocks.IRON_BLOCK,'#',Items.ENDER_EYE
        });
    	GameRegistry.addRecipe(new ItemStack(ItemLoader.itemGlassesHelmet), new Object[]{
    			"!$!", "@#@","! !", '!', Items.STICK, '@', Blocks.GLASS_PANE,'#',Blocks.OBSIDIAN,'$',Items.ENDER_EYE
        });
    }

    private static void registerSmelting()
    {

    }

    private static void registerFuel()
    {

    }
}
