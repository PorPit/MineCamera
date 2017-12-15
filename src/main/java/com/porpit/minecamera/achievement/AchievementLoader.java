package com.porpit.minecamera.achievement;

import com.porpit.minecamera.MineCamera;
import com.porpit.minecamera.block.BlockLoader;
import com.porpit.minecamera.item.ItemLoader;
import com.porpit.minecamera.item.ItemTripod;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class AchievementLoader {
	public static Achievement craftcamera = new Achievement("achievement.minecamera.craftcamera",
            "minecamera.craftcamera", 0, 0, ItemLoader.itemCamera,null);
	public static Achievement craftprocessor = new Achievement("achievement.minecamera.craftprocessor",
            "minecamera.craftprocessor", 2, -2, BlockLoader.photoprocessor,craftcamera);
	public static Achievement crafttripod = new Achievement("achievement.minecamera.crafttripod",
            "minecamera.crafttripod", 2, 2, ItemLoader.itemTripod,craftcamera);
	public static Achievement craftglasses = new Achievement("achievement.minecamera.craftglasses",
            "minecamera.craftglasses", 2, 0, ItemLoader.itemGlassesHelmet,craftcamera);
	public static Achievement craftpictureframe = new Achievement("achievement.minecamera.craftpictureframe",
            "minecamera.craftpictureframe", -2, -2, BlockLoader.pictureFrame,craftcamera);
	public static Achievement craftpictureframe_multiple = new Achievement("achievement.minecamera.craftpictureframe_multiple",
            "minecamera.craftpictureframe_multiple", -4, -3, BlockLoader.pictureFrameMultiple,craftpictureframe);
	public static Achievement craftfilm = new Achievement("achievement.minecamera.craftfilm",
            "minecamera.craftfilm", -2, 2, ItemLoader.itemFilm,craftcamera);
	public static Achievement craftphoto_paper = new Achievement("achievement.minecamera.craftphoto_paper",
            "minecamera.craftphoto_paper", -2, 0, ItemLoader.itemPhotoPaper,craftcamera);
	public static Achievement craftpicture_book = new Achievement("achievement.minecamera.picture_book",
            "minecamera.craftpicture_book", -4, 0, ItemLoader.itemPictureBook,craftphoto_paper);
	public static Achievement craftpicture = new Achievement("achievement.minecamera.craftpicture",
            "minecamera.craftcraftpicture", -6, 0, ItemLoader.itemPicture,craftpicture_book);
	public static AchievementPage pageMineCamera = new AchievementPage(MineCamera.NAME,craftcamera,craftprocessor,crafttripod,craftglasses,craftpictureframe,craftpictureframe_multiple,craftfilm,craftphoto_paper,craftpicture_book,craftpicture);
	public AchievementLoader(){
		craftcamera.initIndependentStat().setSpecial().registerStat();
		craftprocessor.setSpecial().registerStat();
		crafttripod.setSpecial().registerStat();
		craftglasses.setSpecial().registerStat();
		craftpictureframe.setSpecial().registerStat();
		craftpictureframe_multiple.setSpecial().registerStat();
		craftfilm.setSpecial().registerStat();
		craftphoto_paper.setSpecial().registerStat();
		craftpicture_book.setSpecial().registerStat();
		craftpicture.setSpecial().registerStat();
		AchievementPage.registerAchievementPage(pageMineCamera);
	}
}
