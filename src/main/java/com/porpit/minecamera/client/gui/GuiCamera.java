package com.porpit.minecamera.client.gui;

import com.porpit.minecamera.MineCamera;
import com.porpit.minecamera.inventory.ContainerCamera;
import com.porpit.minecamera.util.PictureFactory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCamera extends GuiContainer {
	private static final int BUTTON_PLUS = 0;
	private static final int BUTTON_MINUS = 1;
	private static final String TEXTURE_PATH = MineCamera.MODID + ":" + "textures/gui/container/gui_camera.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);
	protected int totalBurnTime;
	private String info;
	private ContainerCamera inventory;
	private int type;

	public GuiCamera(ContainerCamera inventorySlotsIn) {
		super(inventorySlotsIn);
		this.type = inventorySlotsIn.getType();
		this.xSize = 176;
		this.ySize = 133;
		this.inventory = inventorySlotsIn;
		this.totalBurnTime = inventorySlotsIn.getTotalBurnTime();
	}

	@Override
	public void initGui() {
		super.initGui();
		// System.out.println("gui init");
		if (type == 1) {
			int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;
			this.buttonList.add(new GuiButton(BUTTON_PLUS, offsetX + 29, offsetY + 5, 15, 13, "") {
				@Override
				public void drawButton(Minecraft mc, int mouseX, int mouseY) {
					if (this.visible) {
						GlStateManager.color(1.0F, 1.0F, 1.0F);
						mc.getTextureManager().bindTexture(TEXTURE);
						int x = mouseX - this.xPosition, y = mouseY - this.yPosition;
						if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
							this.drawTexturedModalRect(this.xPosition, this.yPosition, 39, 133, this.width,
									this.height);
						} else {
							this.drawTexturedModalRect(this.xPosition, this.yPosition, 24, 133, this.width,
									this.height);
						}
					}
				}

				@Override
				public void mouseReleased(int mouseX, int mouseY) {
					if (inventory.getDelay() >= 5)
						inventory.setDelay(inventory.getDelay() + 5);
					else {
						inventory.setDelay(inventory.getDelay() + 1);
					}
				}
			});
			this.buttonList.add(new GuiButton(BUTTON_MINUS, offsetX + 61, offsetY + 5, 15, 13, "") {
				@Override
				public void drawButton(Minecraft mc, int mouseX, int mouseY) {
					if (this.visible) {
						GlStateManager.color(1.0F, 1.0F, 1.0F);
						mc.getTextureManager().bindTexture(TEXTURE);
						int x = mouseX - this.xPosition, y = mouseY - this.yPosition;

						if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
							this.drawTexturedModalRect(this.xPosition, this.yPosition, 69, 133, this.width,
									this.height);
						} else {
							this.drawTexturedModalRect(this.xPosition, this.yPosition, 54, 133, this.width,
									this.height);
						}
					}
				}

				@Override
				public void mouseReleased(int mouseX, int mouseY) {
					if (inventory.getDelay() > 5) {
						inventory.setDelay(inventory.getDelay() - 5);
					} else {
						inventory.setDelay(inventory.getDelay() - 1);
					}
				}
			});
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURE);
		int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(offsetX, offsetY, 0, 0, this.xSize, this.ySize);

		PictureFactory.drawDonateImage(this, mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.mc.getTextureManager().bindTexture(TEXTURE);
		int burnTime = this.inventory.getBurnTime();
		int textureWidth;
		if (burnTime >= this.totalBurnTime) {
			textureWidth = 0;
			info = null;
		} else {
			textureWidth = (int) (25.0 * burnTime / this.totalBurnTime);
			this.drawTexturedModalRect(94, 20, 0, 133, textureWidth, 15);
			info = I18n.format("container.camera.text.outing");
		}
		if (type == 1) {
			this.drawTexturedModalRect(47, 6, 84, 133, 11, 11);
			this.fontRendererObj.drawString("¡ì2" + I18n.format("container.camera.text.delay"), 6, 7,
					0x191970);
			int delaysecond = inventory.getDelay();
			this.fontRendererObj.drawString(delaysecond + "s", 48, 7, 0x191970);
		}
		if (info != null) {
			this.fontRendererObj.drawString(info, 94, 39, 0xD3D3D3);
		}
	}
}
