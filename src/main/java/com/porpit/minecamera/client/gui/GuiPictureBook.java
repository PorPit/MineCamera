package com.porpit.minecamera.client.gui;

import java.text.SimpleDateFormat;

import com.porpit.minecamera.MineCamera;
import com.porpit.minecamera.inventory.ContainerPictureBook;
import com.porpit.minecamera.network.MessagePhotoProcessorStart;
import com.porpit.minecamera.network.MessagePictureBookIndex;
import com.porpit.minecamera.network.MessagePictureBookInput;
import com.porpit.minecamera.network.NetworkLoader;
import com.porpit.minecamera.util.EnumFailLoadImage;
import com.porpit.minecamera.util.LoadImageFileThread;
import com.porpit.minecamera.util.PictureFactory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;

public class GuiPictureBook extends GuiContainer {
	private static final int BUTTON_UP = 0;
	private static final int BUTTON_DOWN = 1;
	private static final int BUTTON_CONFIRM = 2;
	private static final String TEXTURE_PATH = MineCamera.MODID + ":" + "textures/gui/container/gui_picture_book.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);
	private float imageRelativeX, imageRelativeY, imageW, imageH;
	private ContainerPictureBook inventory;

	public GuiPictureBook(ContainerPictureBook inventorySlotsIn) {
		super(inventorySlotsIn);
		this.inventory = inventorySlotsIn;
		this.xSize = 250;
		this.ySize = 220;
		this.imageW = 153;
		this.imageH = 85;
		this.imageRelativeX = 57;
		this.imageRelativeY = 25;
		// TODO 自动生成的构造函数存根
	}

	@Override
	public void initGui() {
		super.initGui();
		int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;
		this.buttonList.add(new GuiButton(BUTTON_CONFIRM, offsetX + 225, offsetY + 30, 14, 8, "") {
			@Override
			public void drawButton(Minecraft mc, int mouseX, int mouseY) {
				if (this.visible) {
					GlStateManager.color(1.0F, 1.0F, 1.0F);
					mc.getTextureManager().bindTexture(TEXTURE);
					int x = mouseX - this.xPosition, y = mouseY - this.yPosition;
					if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 42, 220, this.width, this.height);
					} else {
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 28, 220, this.width, this.height);
					}
				}
			}

			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				NetworkLoader.instance.sendToServer(new MessagePictureBookInput());
			}
		});
		this.buttonList.add(new GuiButton(BUTTON_UP, offsetX + 225, offsetY + 50, 14, 8, "") {
			@Override
			public void drawButton(Minecraft mc, int mouseX, int mouseY) {
				if (this.visible) {
					GlStateManager.color(1.0F, 1.0F, 1.0F);
					mc.getTextureManager().bindTexture(TEXTURE);
					int x = mouseX - this.xPosition, y = mouseY - this.yPosition;
					if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 14, 220, this.width, this.height);
					} else {
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 220, this.width, this.height);
					}
				}
			}

			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				MessagePictureBookIndex message = new MessagePictureBookIndex();
				message.index = inventory.getIndex() + 1;
				NetworkLoader.instance.sendToServer(message);
				Minecraft.getMinecraft().thePlayer
						.playSound(new SoundEvent(new ResourceLocation("minecamera:minecamera.book")), 1F, 1F);
			}
		});
		this.buttonList.add(new GuiButton(BUTTON_DOWN, offsetX + 225, offsetY + 82, 14, 8, "") {
			@Override
			public void drawButton(Minecraft mc, int mouseX, int mouseY) {
				if (this.visible) {
					GlStateManager.color(1.0F, 1.0F, 1.0F);
					mc.getTextureManager().bindTexture(TEXTURE);
					int x = mouseX - this.xPosition, y = mouseY - this.yPosition;
					if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 14, 228, this.width, this.height);
					} else {
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 228, this.width, this.height);
					}
				}
			}

			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				MessagePictureBookIndex message = new MessagePictureBookIndex();
				message.index = inventory.getIndex() - 1;
				NetworkLoader.instance.sendToServer(message);
				Minecraft.getMinecraft().thePlayer
						.playSound(new SoundEvent(new ResourceLocation("minecamera:minecamera.book")), 1F, 1F);
			}
		});
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		// TODO 自动生成的方法存根
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURE);
		int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(offsetX, offsetY, 0, 0, this.xSize, this.ySize);

		PictureFactory.drawDonateImage(this,mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

		this.fontRendererObj.drawString(I18n.format("container.picture_book.text.inputinfo"), 170,
				10, 0x8B4513);
		this.fontRendererObj.drawString(I18n.format("container.picture_book.text.pictureindex")
				+" "+ (inventory.getIndex() + 1), 70, 124, 0x8B4513);
		this.fontRendererObj.drawString(I18n.format("container.picture_book.text.totalpicturenum")
				+" "+ inventory.getTotalPictureNum(), 160, 124, 0x8B4513);

		if (this.inventory.getSlot(1).getHasStack()) {
			ItemStack itemStackPicture = this.inventory.getSlot(1).getStack();
			if (itemStackPicture.hasTagCompound() && itemStackPicture.getTagCompound().hasKey("pid")) {
				String imagename = itemStackPicture.getTagCompound().getString("pid");
				if (!imagename.equals("")&&imagename.contains("%_%")) {
					String d="null";
					String author= "null";
					try {
						String[] data=imagename.split("%_%");
						author = data[0];
						Long time=Long.valueOf(data[1]);
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						d = format.format(time);
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					this.fontRendererObj.drawString(I18n.format("container.picture_book.text.owner") + author, 55,
							10, 0x8B4513);
					this.fontRendererObj.drawString(I18n.format("container.picture_book.text.time") + d, 85,
							115, 0x8B4513);
				}else{
					this.fontRendererObj.drawString(I18n.format("container.picture_book.text.error") , 55,
							10, 0x8B4513);
				}
			}else{
				this.fontRendererObj.drawString(I18n.format("container.picture_book.text.null"), 55,
						10, 0x8B4513);
			}
		}

		int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;
		float speedFloat = 100F / (float) Minecraft.getDebugFPS();
		if (mouseX > offsetX + 57 && mouseX < offsetX + 57 + 153 && mouseY > offsetY + 25
				&& mouseY < offsetY + 25 + 75) {
			if (imageRelativeX > PictureFactory.getMcScaledWidth() * 0.15 - offsetX) {
				imageRelativeX -= 8 * speedFloat;
				if (imageRelativeX <= PictureFactory.getMcScaledWidth() * 0.15 - offsetX) {
					imageRelativeX = (float) (PictureFactory.getMcScaledWidth() * 0.15 - offsetX);
				}
			}
			if (imageRelativeY > PictureFactory.getMcScaledHeight() * 0.1 - offsetY) {
				imageRelativeY -= 2 * speedFloat;
				if (imageRelativeY <= PictureFactory.getMcScaledHeight() * 0.1 - offsetY) {
					imageRelativeY = (float) (PictureFactory.getMcScaledHeight() * 0.1 - offsetY);
				}
			}
			if (imageW < PictureFactory.getMcScaledWidth() * 0.7) {
				imageW += 12 * speedFloat;
				if (imageW >= PictureFactory.getMcScaledWidth() * 0.7) {
					imageW = (float) (PictureFactory.getMcScaledWidth() * 0.7);
				}
			}
			imageH = imageW * 0.6F;
		} else {
			if (imageRelativeX < 57) {
				imageRelativeX += 8 * speedFloat;
				if (imageRelativeX >= 57) {
					imageRelativeX = 57;
				}
			}
			if (imageRelativeY < 25) {
				imageRelativeY += 2 * speedFloat;
				if (imageRelativeY >= 25) {
					imageRelativeY = 25;
				}
			}
			if (imageW > 153) {
				imageW -= 12 * speedFloat;
				if (imageW <= 153) {
					imageW = 153;
				}
			}
			imageH = imageW * 85 / 153;
		}
		int imageX = (int) imageRelativeX;
		int imageY = (int) imageRelativeY;
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		if (inventory.getSlot(1).getHasStack()) {
			ItemStack itemStackPicture = inventory.getSlot(1).getStack();
			if (itemStackPicture.hasTagCompound() && itemStackPicture.getTagCompound().hasKey("pid")) {
				String imagename = itemStackPicture.getTagCompound().getString("pid");
				if (PictureFactory.loadedPicture.containsKey(imagename)) {
					// System.out.println("图片存在");
					GlStateManager.bindTexture(PictureFactory.loadedPicture.get(imagename));
					if (imagename.split("%_%").length == 2) {
						GlStateManager.disableDepth();
						this.drawModalRectWithCustomSizedTexture(imageX, imageY, 0F, 0F, (int) imageW, (int) imageH,
								imageW, imageH);
						GlStateManager.enableDepth();
					}
				} else if (!PictureFactory.fildToLoadPicture.containsKey(imagename)) {
					// 图片不存在需要加载
					Minecraft.getMinecraft().getTextureManager().bindTexture(PictureFactory.LODINGIMAGE);
					GlStateManager.disableDepth();
					this.drawModalRectWithCustomSizedTexture(57, 25, 0F, 0F, (int) 153, (int) 85, 153, 85);
					GlStateManager.enableDepth();
					if (!PictureFactory.lodingPicture.contains(imagename)) {
						// System.out.println("需要加载");
						PictureFactory.lodingPicture.add(imagename);
						LoadImageFileThread thread = new LoadImageFileThread(imagename);
						thread.start();
					}
				} else {
					// 图片加载失败
					Minecraft.getMinecraft().getTextureManager().bindTexture(PictureFactory.FailToLoadImage);
					GlStateManager.disableDepth();
					this.drawModalRectWithCustomSizedTexture(57, 25, 0F, 0F, (int) 153, (int) 85, 153, 85);
					GlStateManager.enableDepth();
				}
			} else {

			}
		}
	}
}
