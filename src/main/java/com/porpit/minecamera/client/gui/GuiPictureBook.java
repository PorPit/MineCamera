package com.porpit.minecamera.client.gui;

import com.porpit.minecamera.MineCamera;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class GuiPictureBook extends GuiContainer{
	private static final int BUTTON_UP = 0;
	private static final int BUTTON_DOWN = 1;
	private static final int BUTTON_CONFIRM = 2;
	private static final String TEXTURE_PATH = MineCamera.MODID + ":" + "textures/gui/container/gui_picture_book.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);
	public GuiPictureBook(Container inventorySlotsIn) {
		super(inventorySlotsIn);
		this.xSize = 250;
		this.ySize = 220;
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
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 42, 220, this.width,
								this.height);
					} else {
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 28, 220, this.width,
								this.height);
					}
				}
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
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 14, 220, this.width,
								this.height);
					} else {
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 220, this.width,
								this.height);
					}
				}
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
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 14, 228, this.width,
								this.height);
					} else {
						this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 228, this.width,
								this.height);
					}
				}
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
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRendererObj.drawString(TextFormatting.BOLD+I18n.format("放入相册位置=>>"),140, 10,
				0x8B4513);
		this.fontRendererObj.drawString(TextFormatting.BOLD+I18n.format("当前页:"),68,120,
				0x8B4513);
		this.fontRendererObj.drawString(TextFormatting.BOLD+I18n.format("总页:"),160, 120,
				0x8B4513);
	}
}
