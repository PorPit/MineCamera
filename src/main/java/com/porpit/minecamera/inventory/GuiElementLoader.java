package com.porpit.minecamera.inventory;

import com.porpit.minecamera.MineCamera;
import com.porpit.minecamera.client.gui.GuiCamera;
import com.porpit.minecamera.client.gui.GuiPhotoProcessor;
import com.porpit.minecamera.client.gui.GuiPictureBook;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import scala.reflect.internal.Trees.Return;

public class GuiElementLoader implements IGuiHandler {
	public static final int GUI_CAMERA = 1;
	public static final int GUI_PHOTOPROCESSOR = 2;
	public static final int GUI_TRIPOD_CAMERA = 3;
	public static final int GUI_PICTURE_BOOK = 4;

	public GuiElementLoader() {
		NetworkRegistry.INSTANCE.registerGuiHandler(MineCamera.instance, this);
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case GUI_CAMERA:
			return new ContainerCamera(player, 0);
		case GUI_PHOTOPROCESSOR:
			return new ContainerPhotoProcessor(player, world.getTileEntity(new BlockPos(x, y, z)));
		case GUI_TRIPOD_CAMERA:
			return new ContainerCamera(player, 1);
		case GUI_PICTURE_BOOK:
			return new ContainerPictureBook(player);
		default:
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case GUI_CAMERA:
			return new GuiCamera(new ContainerCamera(player, 0));
		case GUI_PHOTOPROCESSOR:
			return new GuiPhotoProcessor(
					new ContainerPhotoProcessor(player, world.getTileEntity(new BlockPos(x, y, z))));
		case GUI_TRIPOD_CAMERA:
			return new GuiCamera(new ContainerCamera(player, 1));
		case GUI_PICTURE_BOOK:
			return new GuiPictureBook(new ContainerPictureBook(player));
		default:
			return null;
		}
	}
}
