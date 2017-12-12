package com.porpit.minecamera.client.gui;

import java.io.IOException;

import com.porpit.minecamera.MineCamera;
import com.porpit.minecamera.inventory.ContainerPhotoProcessor;
import com.porpit.minecamera.network.MessagePhotoProcessorStart;
import com.porpit.minecamera.network.NetworkLoader;
import com.porpit.minecamera.tileentity.TileEntityPhotoProcessor;
import com.porpit.minecamera.util.EnumFailLoadImage;
import com.porpit.minecamera.util.LoadImageFileThread;
import com.porpit.minecamera.util.PictureFactory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPhotoProcessor extends GuiContainer {
	private static final int BUTTON_START = 0;
	private static final String TEXTURE_PATH = MineCamera.MODID + ":" + "textures/gui/container/gui_photoprocessor.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);
	private float imageRelativeX, imageRelativeY, imageW, imageH;
	private String info = I18n.format("container.photoprocessor.text.default");
	private boolean infolock = false;
	private ContainerPhotoProcessor container;

	public GuiPhotoProcessor(ContainerPhotoProcessor inventorySlotsIn) {
		super(inventorySlotsIn);
		container = inventorySlotsIn;
		this.xSize = 176;
		this.ySize = 206;
		this.imageW = 129;
		this.imageH = 73;
		this.imageRelativeX = 33;
		this.imageRelativeY = 16;
	}

	@Override
	public void initGui() {
		super.initGui();
		final FontRenderer fr = this.fontRendererObj;
		int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiButton(BUTTON_START, offsetX + 11, offsetY + 101, 25, 16, "") {
			@Override
			public void drawButton(Minecraft mc, int mouseX, int mouseY) {
				if (this.visible) {
					GlStateManager.color(1.0F, 1.0F, 1.0F);

					mc.getTextureManager().bindTexture(TEXTURE);
					int x = mouseX - this.xPosition, y = mouseY - this.yPosition;

					if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 50, 206, this.width, this.height);
					} else {
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 24, 206, this.width, this.height);
					}
				}
			}

			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				imageW = 129;
				imageH = 73;
				imageRelativeX = 33;
				imageRelativeY = 16;
				infolock = true;
				int lightlevel=container.getTileEntity().getWorld().getLight(container.getTileEntity().getPos());
				if (!container.getSlot(0).getHasStack()) {
					info = TextFormatting.RED + I18n.format("container.photoprocessor.text.nonectl");
					return;
				} else if (!container.getSlot(1).getHasStack()) {
					info = TextFormatting.RED + I18n.format("container.photoprocessor.text.noneda");
					return;
				} else if (!container.getSlot(2).getHasStack()) {
					info = TextFormatting.RED + I18n.format("container.photoprocessor.text.nonefilm");
					return;
				} else if (!container.getSlot(3).getHasStack()) {
					info = TextFormatting.RED + I18n.format("container.photoprocessor.text.nonepaper");
					return;
				} else if (container.getSlot(4).getHasStack()) {
					info = TextFormatting.RED + I18n.format("container.photoprocessor.text.hasphotoout");
					return;
				} else if (container.getBurnTime() != container.totalBurnTime) {
					return;
				}else if(lightlevel>5){
					info = TextFormatting.RED + I18n.format("container.photoprocessor.text.lightleveltoohigh");
					return;
				}
				if (container.getSlot(2).getStack().hasTagCompound()
						&& container.getSlot(2).getStack().getTagCompound().hasKey("pid")) {
					String imagename = container.getSlot(2).getStack().getTagCompound().getString("pid");
					EnumFailLoadImage flag = PictureFactory.isFailedToLoad(imagename);
					if (flag != null) {
						info = TextFormatting.RED
								+ I18n.format("container.photoprocessor.text.failed." + flag.toString());
						return;
					}
					MessagePhotoProcessorStart message = new MessagePhotoProcessorStart();
					message.dimid = container.getTileEntity().getWorld().provider.getDimension();
					message.bp = container.getTileEntity().getPos();
					message.playername = container.getPlayer().getName();
					NetworkLoader.instance.sendToServer(message);
				}
			}
		});

	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (this.getSlotUnderMouse() != null
				&& (this.getSlotUnderMouse().getSlotIndex() == 2 || this.getSlotUnderMouse().getSlotIndex() == 4)) {
			infolock = false;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURE);
		int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(offsetX, offsetY, 0, 0, this.xSize, this.ySize);
		this.fontRendererObj.drawString(info, offsetX + 31, offsetY + 4, 0xD3D3D3);
		this.fontRendererObj.drawString(I18n.format("container.photoprocessor.text.lightlevel"), offsetX + 7, offsetY + 7, 0x6495ED);
		int lightlevel=this.container.getTileEntity().getWorld().getLight(this.container.getTileEntity().getPos());
		this.fontRendererObj.drawString(I18n.format(lightlevel+""), offsetX + 12, offsetY + 15, 0x6495ED);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.mc.getTextureManager().bindTexture(TEXTURE);
		String button = I18n.format("container.photoprocessor.button.start");
		int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;
		this.fontRendererObj.drawString(button, 15, 105, 0xD3D3D3);
		int burnTime = this.container.getBurnTime();
		int textureWidth;
		if (burnTime >= TileEntityPhotoProcessor.totalburnTime) {
			textureWidth = 0;
		} else {
			info = TextFormatting.GREEN + I18n.format("container.photoprocessor.text.outing");
			textureWidth = (int) (25.0 * burnTime / TileEntityPhotoProcessor.totalburnTime);
			Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
			this.drawTexturedModalRect(114, 101, 0, 206, textureWidth, 15);
			if (textureWidth == 24) {
				info = TextFormatting.GREEN + I18n.format("container.photoprocessor.text.success");
			}
		}
		// speed
		float test = 100F / (float) Minecraft.getDebugFPS();
		if (mouseX > offsetX + 33 && mouseX < offsetX + 33 + 129 && mouseY > offsetY + 16
				&& mouseY < offsetY + 16 + 73) {
			System.out.println("test:" + test);
			System.out.println("MinecraftDisplayWidth:" + Minecraft.getMinecraft().displayWidth);
			System.out.println("MinecraftDisplayHeight:" + Minecraft.getMinecraft().displayHeight);
			if (imageRelativeX > Minecraft.getMinecraft().displayWidth / 2 * 0.15 - offsetX) {
				imageRelativeX -= 8 * test;
				System.out.println("imageRelativeX:" + imageRelativeX);
				if (imageRelativeX <= Minecraft.getMinecraft().displayWidth / 2 * 0.15 - offsetX) {
					imageRelativeX = (float) (Minecraft.getMinecraft().displayWidth / 2 * 0.15 - offsetX);
				}
			}
			if (imageRelativeY > Minecraft.getMinecraft().displayHeight / 2 * 0.1 - offsetY) {
				imageRelativeY -= 2 * test;
				if (imageRelativeY <= Minecraft.getMinecraft().displayHeight / 2 * 0.1 - offsetY) {
					imageRelativeY = (float) (Minecraft.getMinecraft().displayHeight / 2 * 0.1 - offsetY);
				}
			}
			if (imageW < Minecraft.getMinecraft().displayWidth / 2 * 0.7) {
				imageW += 12 * test;
				if (imageW >= Minecraft.getMinecraft().displayWidth / 2 * 0.7) {
					imageW = (float) (Minecraft.getMinecraft().displayWidth / 2 * 0.7);
				}
			}
			imageH=imageW*0.6F;
			/*if (imageH < Minecraft.getMinecraft().displayHeight / 2 * 0.8) {
				imageH += 9 * test;
				if (imageH >= Minecraft.getMinecraft().displayHeight / 2 * 0.8) {
					imageH = (float) (Minecraft.getMinecraft().displayHeight / 2 * 0.8);
				}
			}*/
		} else {
			if (imageRelativeX < 33) {
				imageRelativeX += 8 * test;
				if (imageRelativeX >= 33) {
					imageRelativeX = 33;
				}
			}
			if (imageRelativeY < 16) {
				imageRelativeY += 2 * test;
				if (imageRelativeY >= 16) {
					imageRelativeY = 16;
				}
			}
			if (imageW > 129) {
				imageW -= 12 * test;
				if (imageW <= 129) {
					imageW = 129;
				}
			}
			imageH=imageW*73/129;
		}
		int imageX = (int) imageRelativeX;
		int imageY = (int) imageRelativeY;

		if (container.getSlot(2).getStack() != null) {
			String imagename;
			if (container.getSlot(2).getStack().hasTagCompound()
					&& container.getSlot(2).getStack().getTagCompound().hasKey("pid")) {
				imagename = container.getSlot(2).getStack().getTagCompound().getString("pid");
			} else {
				return;
			}
			if (PictureFactory.loadedPicture.containsKey(imagename)) {
				// System.out.println("图片存在");
				GlStateManager.bindTexture(PictureFactory.loadedPicture.get(imagename));
				if (imagename.split("%_%").length == 2) {
					GlStateManager.disableDepth();
					this.drawModalRectWithCustomSizedTexture(imageX, imageY, 0F, 0F, (int) imageW, (int) imageH, imageW,
							imageH);
					GlStateManager.enableDepth();
					if (infolock == false) {
						info = TextFormatting.GREEN + I18n.format("container.photoprocessor.text.readsuccess")
								+ imagename.split("%_%")[0];
					}
				}
			} else if (!PictureFactory.fildToLoadPicture.containsKey(imagename)) {
				// 图片不存在需要加载
				Minecraft.getMinecraft().getTextureManager().bindTexture(PictureFactory.LODINGIMAGE);
				GlStateManager.disableDepth();
				this.drawModalRectWithCustomSizedTexture(imageX, imageY, 0F, 0F, (int) imageW, (int) imageH, imageW,
						imageH);
				GlStateManager.enableDepth();
				if (infolock == false) {
					info = TextFormatting.GREEN + I18n.format("container.photoprocessor.text.lodingfilm")
							+ imagename.split("%_%")[0];
				}
				if (!PictureFactory.lodingPicture.contains(imagename)) {
					System.out.println("需要加载");
					PictureFactory.lodingPicture.add(imagename);
					LoadImageFileThread thread = new LoadImageFileThread(imagename);
					thread.start();
				}
			} else {
				// 图片加载失败
				Minecraft.getMinecraft().getTextureManager().bindTexture(PictureFactory.FailToLoadImage);
				GlStateManager.disableDepth();
				this.drawModalRectWithCustomSizedTexture(imageX, imageY, 0, 0, (int) imageW, (int) imageH, imageW,
						imageH);
				GlStateManager.enableDepth();
				if (infolock == false) {
					info = TextFormatting.RED
							+ I18n.format("text.info." + PictureFactory.fildToLoadPicture.get(imagename).toString());
				}
			}
		} else {
			info = I18n.format("container.photoprocessor.text.default");
		}
	}
}
