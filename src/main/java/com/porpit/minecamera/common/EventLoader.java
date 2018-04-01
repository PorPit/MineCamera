package com.porpit.minecamera.common;

import org.lwjgl.input.Keyboard;

import com.porpit.minecamera.MineCamera;
import com.porpit.minecamera.achievement.AchievementLoader;
import com.porpit.minecamera.block.BlockPhotoProcessor;
import com.porpit.minecamera.block.BlockPictureFrame;
import com.porpit.minecamera.block.BlockPictureFrameMultiple;
import com.porpit.minecamera.client.KeyLoader;
import com.porpit.minecamera.entity.EntityTripod;
import com.porpit.minecamera.inventory.GuiElementLoader;
import com.porpit.minecamera.item.ItemCamera;
import com.porpit.minecamera.item.ItemFilm;
import com.porpit.minecamera.item.ItemGlassesHelmet;
import com.porpit.minecamera.item.ItemLoader;
import com.porpit.minecamera.item.ItemPhotoPaper;
import com.porpit.minecamera.item.ItemPictureBook;
import com.porpit.minecamera.item.ItemTripod;
import com.porpit.minecamera.network.MessagePlayerViewRender;
import com.porpit.minecamera.network.NetworkLoader;
import com.porpit.minecamera.util.PictureFactory;
import com.porpit.minecamera.util.TripodActiveThread;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventLoader {

	private static final String TEXTURE_PATH = MineCamera.MODID + ":" + "textures/gui/container/gui_camera.png";
	private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);
	float roll = 0f;
	private Entity renderEntity;
	public static final EventBus EVENT_BUS = new EventBus();

	public EventLoader() {
		MinecraftForge.EVENT_BUS.register(this);
		EventLoader.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void AttackEntity(AttackEntityEvent event) {
		if (event.entityPlayer.getEntityData().hasKey("renderViewCamera"))
			event.setCanceled(true);
	}

	private boolean rcicanactive = false;

	@SubscribeEvent
	public void PlayerInteract(PlayerInteractEvent event) {
		System.out.println("active camera");
		if (rcicanactive == true && event.isCancelable()
				&& event.entityPlayer.getEntityData().hasKey("renderViewCamera")) {
			if (event.world.isRemote && !event.action.equals(Action.RIGHT_CLICK_BLOCK)) {
				ActiveTripod(Minecraft.getMinecraft().thePlayer.getName(),
						((EntityTripod) event.world
								.getEntityByID(event.entityPlayer.getEntityData().getInteger("renderViewCamera")))
										.getDelay());
			}
			event.setCanceled(true);
		}
		if (event.entityPlayer.getEntityData().hasKey("renderViewCamera")) {
			rcicanactive = true;
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderPlayerPre(RenderPlayerEvent.Pre event) {
		Minecraft mc = Minecraft.getMinecraft();
		renderEntity = mc.getRenderViewEntity();
		mc.setRenderViewEntity(mc.thePlayer);
		// mc.entityRenderer.loadEntityShader(mc.thePlayer);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderPlayerPost(RenderPlayerEvent.Post event) {
		Minecraft mc = Minecraft.getMinecraft();
		// System.out.println(Minecraft.getMinecraft().thePlayer.getMirroredYaw(Mirror.LEFT_RIGHT));
		mc.setRenderViewEntity(renderEntity);
	}

	private void ActiveTripod(String playername, int delay) {

		if (!TripodActiveThread.isshooting) {
			TripodActiveThread thread = new TripodActiveThread(playername, delay);
			thread.start();
		} else {
			Minecraft.getMinecraft().thePlayer
					.addChatComponentMessage(new ChatComponentTranslation("chat.minecamera.isshooting"));
		}
	}

	// 处理
	/*
	 * @SideOnly(Side.CLIENT)
	 * 
	 * @SubscribeEvent public void rightClickBlock(RightClickBlock event) { if
	 * (event.entityPlayer.getEntityData().hasKey("renderViewCamera") &&
	 * event.getItemStack() == null && event.getSide().isClient() &&
	 * event.getHand().equals(EnumHand.MAIN_HAND)) {
	 * System.out.println("RightClickBlock,HandType=" + event.getHand());
	 * ActiveTripod(Minecraft.getMinecraft().thePlayer.getName(),
	 * ((EntityTripod) event.getWorld()
	 * .getEntityByID(event.entityPlayer.getEntityData().getInteger(
	 * "renderViewCamera"))) .getDelay()); event.setCanceled(true); } }
	 */

	// 处理

	/*
	 * 
	 * @SideOnly(Side.CLIENT)
	 * 
	 * @SubscribeEvent public void rightClickItem(RightClickItem event) { if
	 * (event.getSide().isClient() &&
	 * event.getHand().equals(EnumHand.MAIN_HAND)) { if (rcicanactive == true) {
	 * System.out.println("RightClickItem,HandType=" + event.getHand());
	 * ActiveTripod(Minecraft.getMinecraft().thePlayer.getName(),
	 * ((EntityTripod) event.getWorld()
	 * .getEntityByID(event.entityPlayer.getEntityData().getInteger(
	 * "renderViewCamera"))) .getDelay()); event.setCanceled(true); } if
	 * (event.entityPlayer.getEntityData().hasKey("renderViewCamera")) {
	 * rcicanactive = true; }
	 * 
	 * } }
	 */

	/*
	 * // 处理
	 * 
	 * @SideOnly(Side.CLIENT)
	 * 
	 * @SubscribeEvent public void rightClickEmpty(RightClickEmpty event) { //
	 * System.out.println("RightClickEmpty,HandType="+event.getHand()); if
	 * (event.entityPlayer.getEntityData().hasKey("renderViewCamera") &&
	 * event.getSide().isClient() && event.getHand().equals(EnumHand.MAIN_HAND))
	 * { System.out.println("delay=" + ((EntityTripod)
	 * Minecraft.getMinecraft().theWorld
	 * .getEntityByID(Minecraft.getMinecraft().thePlayer.getEntityData().
	 * getInteger("renderViewCamera"))) .getDelay());
	 * System.out.println("RightClickEmpty,HandType=" + event.getHand());
	 * ActiveTripod(Minecraft.getMinecraft().thePlayer.getName(),
	 * ((EntityTripod) event.getWorld()
	 * .getEntityByID(event.entityPlayer.getEntityData().getInteger(
	 * "renderViewCamera"))) .getDelay()); } }
	 */

	// 处理
	@SubscribeEvent
	public void entityInteract(EntityInteractEvent event) {
		if (event.entityPlayer.getEntityData().hasKey("renderViewCamera")) {
			event.setCanceled(true);
			if (!event.entityPlayer.isServerWorld() && event.entityPlayer.getHeldItem() == null) {
				ActiveTripod(Minecraft.getMinecraft().thePlayer.getName(),
						((EntityTripod) event.entityPlayer.getEntityWorld()
								.getEntityByID(event.entityPlayer.getEntityData().getInteger("renderViewCamera")))
										.getDelay());
			}
			return;
		}
		if (event.target instanceof EntityTripod) {
			Entity target = event.target;
			EntityPlayer player = event.entityPlayer;
			if (!player.isSneaking()) {
				if (player.inventory.armorInventory[3] != null
						&& player.inventory.armorInventory[3].getItem() instanceof ItemGlassesHelmet) {
					if (player.getEntityWorld().isRemote) {
						// System.out.println("123");
						Minecraft.getMinecraft().setRenderViewEntity(target);
						Minecraft.getMinecraft().ingameGUI
								.setRecordPlaying(new ChatComponentTranslation("chat.tripod.info"), false);
					}
					player.getEntityData().setInteger("renderViewCamera", target.getEntityId());
				} else if (!event.entityPlayer.getEntityWorld().isRemote) {
					player.addChatComponentMessage(new ChatComponentTranslation("chat.tripod.mustuseglass"));
				}
			} else {
				System.out.println("id:" + target.getEntityId());
				System.out.println(event.entityPlayer.getEntityWorld().getEntityByID(target.getEntityId()));

				player.getEntityData().setInteger("usingGui", target.getEntityId());
				player.openGui(MineCamera.instance, GuiElementLoader.GUI_TRIPOD_CAMERA, target.getEntityWorld(),
						(int) target.posX, (int) target.posY, (int) target.posZ);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		// System.out.println(event.toString());
		if (Minecraft.getMinecraft().gameSettings.keyBindSneak.isPressed()) {
			if (!EntityTripod.islock) {
				Minecraft.getMinecraft().setRenderViewEntity(mc.thePlayer);
				// Minecraft.getMinecraft().entityRenderer.loadEntityShader(Minecraft.getMinecraft().thePlayer);
				if (mc.thePlayer.getEntityData().hasKey("renderViewCamera")) {

					/*
					 * MessageUpdatePitchYaw messagepy =new
					 * MessageUpdatePitchYaw(); messagepy.playername=
					 * mc.thePlayer.getName();
					 * messagepy.rotationYaw=mc.theWorld.getEntityByID(
					 * mc.thePlayer.getEntityData().getInteger(
					 * "renderViewCamera")).rotationYaw;
					 * messagepy.rotationPitch=mc.theWorld.getEntityByID(
					 * mc.thePlayer.getEntityData().getInteger(
					 * "renderViewCamera")).rotationPitch;
					 * NetworkLoader.instance.sendToServer(messagepy);
					 */
					rcicanactive = false;
					mc.thePlayer.getEntityData().removeTag("renderViewCamera");
					/*
					 * AxisAlignedBB axisalignedbb=
					 * mc.thePlayer.getEntityBoundingBox();
					 * mc.thePlayer.setEntityBoundingBox(new
					 * AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY,
					 * axisalignedbb.minZ, axisalignedbb.minX +
					 * (double)Minecraft.getMinecraft().thePlayer.width,
					 * axisalignedbb.minY +
					 * (double)Minecraft.getMinecraft().thePlayer.height,
					 * axisalignedbb.minZ +
					 * (double)Minecraft.getMinecraft().thePlayer.width));
					 */
					MessagePlayerViewRender message = new MessagePlayerViewRender();
					NetworkLoader.instance.sendToServer(message);
				}
			}
		}
		if (Keyboard.isKeyDown(KeyLoader.cameralock.getKeyCode())) {
			EntityTripod.islock = !EntityTripod.islock;
			/*
			 * if(EntityTripod.islock==true&&mc.thePlayer.getEntityData().hasKey
			 * ("renderViewCamera")){ MessageUpdatePitchYaw messagepy =new
			 * MessageUpdatePitchYaw(); messagepy.playername=
			 * mc.thePlayer.getName();
			 * messagepy.rotationYaw=mc.theWorld.getEntityByID(
			 * mc.thePlayer.getEntityData().getInteger("renderViewCamera")).
			 * rotationYaw; messagepy.rotationPitch=mc.theWorld.getEntityByID(
			 * mc.thePlayer.getEntityData().getInteger("renderViewCamera")).
			 * rotationPitch; NetworkLoader.instance.sendToServer(messagepy); }
			 */
			/*
			 * Minecraft.getMinecraft().theWorld.playSound(Minecraft.
			 * getMinecraft().thePlayer.posX,
			 * Minecraft.getMinecraft().thePlayer.posY,
			 * Minecraft.getMinecraft().thePlayer.posZ, new SoundEvent(new
			 * ResourceLocation("minecamera:minecamera.kacha")),
			 * SoundCategory.PLAYERS, 1.0F, 1.0F, false);
			 */
			// File f=new File("c:\\");
			// ScreenShotHelper.saveScreenshot(f,
			// Minecraft.getMinecraft().displayWidth,
			// Minecraft.getMinecraft().displayHeight,
			// Minecraft.getMinecraft().getFramebuffer());
			/*
			 * roll+=0.5; Minecraft.getMinecraft().gameSettings.hideGUI=true;
			 * 
			 */
			// Minecraft.getMinecraft().setRenderViewEntity(Minecraft.getMinecraft().thePlayer.getRidingEntity());

		}
	}

	@SubscribeEvent
	public void EntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityPlayer) {
			event.entity.getEntityData().removeTag("renderViewCamera");
			event.entity.getEntityData().removeTag("usingGui");

		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderHand(RenderHandEvent event) {
		if (Minecraft.getMinecraft().thePlayer.getEntityData().hasKey("renderViewCamera")) {
			event.setCanceled(true);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderGameOverlay(RenderGameOverlayEvent event) {
		if ((Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityTripod)) {
			if (event.type.equals(ElementType.HOTBAR)) {
				String TEXTURE_PATH = MineCamera.MODID + ":" + "textures/gui/image/cameragui.png";
				ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);
				GlStateManager.color(1F, 1F, 1F);
				Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
				Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 0, 0, 82, 82);
				Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(PictureFactory.getMcScaledWidth() - 81,
						0, 82, 0, 81, 81);
				Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0,
						PictureFactory.getMcScaledHeight() - 81, 0, 82, 81, 81);
				Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(PictureFactory.getMcScaledWidth() - 81,
						PictureFactory.getMcScaledHeight() - 82, 82, 82, 81, 81);
				Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(
						PictureFactory.getMcScaledWidth() / 2 - 46,
						PictureFactory.getMcScaledHeight() / 2 - 46, 163, 0, 93, 93);
				String locked = I18n.format("gui.camgameoverlay.locked");
				String unlocked = I18n.format("gui.camgameoverlay.unlocked");
				Minecraft.getMinecraft().ingameGUI.drawString(Minecraft.getMinecraft().ingameGUI.getFontRenderer(),
						"§2§l" + (EntityTripod.islock ? locked : unlocked), 82,
						PictureFactory.getMcScaledHeight() - 40, 0x99FFFF);
			}
			if (event.type.equals(ElementType.EXPERIENCE)) {
				event.setCanceled(true);
			}
		}
		// GlStateManager.pushMatrix();
		// GlStateManager.disableDepth();
		// GlStateManager.enableBlend();
		// String TEXTURE_PATH = MineCamera.MODID + ":" +
		// "textures/gui/container/gui_camera.png";
		// ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);
		// GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		// Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
		// GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
		// GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
		// GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		// e
		// GlStateManager.popMatrix();
		// Minecraft.getMinecraft().displayGuiScreen(new GuiScreenDemo());
		// OpenGlHelper.renderDirections(10);

	}

	@SubscribeEvent
	public void onPlayerItemCrafted(ItemCraftedEvent event) {
		if (Block.getBlockFromItem(event.crafting.getItem()) instanceof BlockPhotoProcessor
				&& !event.player.getEntityWorld().isRemote) {
			event.player.triggerAchievement(AchievementLoader.craftprocessor);
		}
		if (Block.getBlockFromItem(event.crafting.getItem()) instanceof BlockPictureFrame
				&& !event.player.getEntityWorld().isRemote) {
			event.player.triggerAchievement(AchievementLoader.craftpictureframe);
		}
		if (Block.getBlockFromItem(event.crafting.getItem()) instanceof BlockPictureFrameMultiple
				&& !event.player.getEntityWorld().isRemote) {
			event.player.triggerAchievement(AchievementLoader.craftpictureframe_multiple);
		}
		if (event.crafting.getItem() instanceof ItemPhotoPaper & !event.player.getEntityWorld().isRemote) {
			event.player.triggerAchievement(AchievementLoader.craftphoto_paper);
		}
		if (event.crafting.getItem() instanceof ItemGlassesHelmet & !event.player.getEntityWorld().isRemote) {
			event.player.triggerAchievement(AchievementLoader.craftglasses);
		}
		if (event.crafting.getItem() instanceof ItemFilm && !event.player.getEntityWorld().isRemote) {
			event.player.triggerAchievement(AchievementLoader.craftfilm);
		}
		if (event.crafting.getItem() instanceof ItemTripod) {
			event.player.triggerAchievement(AchievementLoader.crafttripod);
		}
		if (event.crafting.getItem() instanceof ItemPictureBook) {
			event.player.triggerAchievement(AchievementLoader.craftpicture_book);
		}
		if (event.crafting.getItem() instanceof ItemCamera) {
			if (!event.player.getEntityWorld().isRemote) {
				System.out.println("has no stat");
				event.player.triggerAchievement(AchievementLoader.craftcamera);
			}
			for (int i = 0; i < 9; i++) {
				if (event.craftMatrix.getStackInSlot(i) != null
						&& event.craftMatrix.getStackInSlot(i).getItem() instanceof ItemTripod) {
					// if (!event.player.inventory.addItemStackToInventory(new
					// ItemStack(ItemLoader.itemCamera))) {
					// if (!event.player.getEntityWorld().isRemote) {
					// Block.spawnAsEntity(event.player.getEntityWorld(),
					// event.player.getPosition(),
					// new ItemStack(ItemLoader.itemCamera));
					// }
					// }
					if (!event.player.inventory.addItemStackToInventory(new ItemStack(Blocks.iron_block, 3))) {
						if (!event.player.getEntityWorld().isRemote) {
							Block.spawnAsEntity(event.player.getEntityWorld(), event.player.getPosition(),
									new ItemStack(Blocks.iron_block, 3));
						}
					}
					if (!event.player.inventory.addItemStackToInventory(new ItemStack(Items.ender_eye))) {
						if (!event.player.getEntityWorld().isRemote) {
							Block.spawnAsEntity(event.player.getEntityWorld(), event.player.getPosition(),
									new ItemStack(Items.ender_eye));
						}
					}
					break;
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void cameraSetup(CameraSetup event) {
		// System.out.println("11");
		// Minecraft.getMinecraft().entityRenderer.loadEntityShader(Minecraft.getMinecraft().thePlayer);
	}
}
