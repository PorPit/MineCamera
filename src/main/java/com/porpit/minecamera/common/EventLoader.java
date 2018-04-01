package com.porpit.minecamera.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.w3c.dom.events.EventException;

import com.porpit.minecamera.MineCamera;
import com.porpit.minecamera.block.BlockPictureFrameMultiple;
import com.porpit.minecamera.block.BlockPhotoProcessor;
import com.porpit.minecamera.block.BlockPictureFrame;
import com.porpit.minecamera.client.KeyLoader;
import com.porpit.minecamera.entity.EntityTripod;
import com.porpit.minecamera.inventory.GuiElementLoader;
import com.porpit.minecamera.item.ItemCamera;
import com.porpit.minecamera.item.ItemFilm;
import com.porpit.minecamera.item.ItemGlassesHelmet;
import com.porpit.minecamera.item.ItemLoader;
import com.porpit.minecamera.item.ItemPhotoPaper;
import com.porpit.minecamera.item.ItemPicture;
import com.porpit.minecamera.item.ItemPictureBook;
import com.porpit.minecamera.item.ItemTripod;
import com.porpit.minecamera.network.MessagePlayerViewRender;
import com.porpit.minecamera.network.MessageUpdatePitchYaw;
import com.porpit.minecamera.network.NetworkLoader;
import com.porpit.minecamera.tileentity.TileEntityPictureFrameMultiple;
import com.porpit.minecamera.util.PictureFactory;
import com.porpit.minecamera.util.SaveImageThread;
import com.porpit.minecamera.util.TripodActiveThread;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockSign;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreenDemo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteractSpecific;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickEmpty;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.reflect.internal.Trees.If;

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
		if (event.getEntityPlayer().getEntityData().hasKey("renderViewCamera"))
			event.setCanceled(true);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderPlayerPre(RenderPlayerEvent.Pre event) {
		Minecraft mc = Minecraft.getMinecraft();
		renderEntity = mc.getRenderManager().renderViewEntity;
		mc.getRenderManager().renderViewEntity = mc.player;
		mc.entityRenderer.loadEntityShader(mc.player);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderPlayerPost(RenderPlayerEvent.Post event) {
		Minecraft mc = Minecraft.getMinecraft();
		// System.out.println(Minecraft.getMinecraft().thePlayer.getMirroredYaw(Mirror.LEFT_RIGHT));
		mc.getRenderManager().renderViewEntity = renderEntity;
	}

	private void ActiveTripod(String playername, int delay) {
		if (!TripodActiveThread.isshooting) {
			TripodActiveThread thread = new TripodActiveThread(playername, delay);
			thread.start();
		} else {
			Minecraft.getMinecraft().player
					.sendMessage(new TextComponentTranslation("chat.minecamera.isshooting"));
		}
	}

	// 处理
	@SubscribeEvent
	public void rightClickBlock(RightClickBlock event) {
		if (event.getEntityPlayer().getEntityData().hasKey("renderViewCamera") && (event.getItemStack() == null||event.getItemStack().isEmpty())
				&& event.getSide().isClient() && event.getHand().equals(EnumHand.MAIN_HAND)) {
			System.out.println("RightClickBlock,HandType=" + event.getHand());
			ActiveTripod(Minecraft.getMinecraft().player.getName(),
					((EntityTripod) event.getWorld()
							.getEntityByID(event.getEntityPlayer().getEntityData().getInteger("renderViewCamera")))
									.getDelay());
			event.setCanceled(true);
		}
	}

	// 处理
	private boolean rcicanactive = false;

	@SubscribeEvent
	public void rightClickItem(RightClickItem event) {
		if (event.getSide().isClient() && event.getHand().equals(EnumHand.MAIN_HAND)) {
			if (rcicanactive == true) {
				System.out.println("RightClickItem,HandType=" + event.getHand());
				ActiveTripod(Minecraft.getMinecraft().player.getName(),
						((EntityTripod) event.getWorld()
								.getEntityByID(event.getEntityPlayer().getEntityData().getInteger("renderViewCamera")))
										.getDelay());
				event.setCanceled(true);
			}
			if (event.getEntityPlayer().getEntityData().hasKey("renderViewCamera")) {
				rcicanactive = true;
			}

		}
	}

	// 处理
	@SubscribeEvent
	public void rightClickEmpty(RightClickEmpty event) {
		// System.out.println("RightClickEmpty,HandType="+event.getHand());
		if (event.getEntityPlayer().getEntityData().hasKey("renderViewCamera") && event.getSide().isClient()
				&& event.getHand().equals(EnumHand.MAIN_HAND)) {
			System.out.println("delay=" + ((EntityTripod) Minecraft.getMinecraft().world
					.getEntityByID(Minecraft.getMinecraft().player.getEntityData().getInteger("renderViewCamera")))
							.getDelay());
			System.out.println("RightClickEmpty,HandType=" + event.getHand());
			ActiveTripod(Minecraft.getMinecraft().player.getName(),
					((EntityTripod) event.getWorld()
							.getEntityByID(event.getEntityPlayer().getEntityData().getInteger("renderViewCamera")))
									.getDelay());
		}
	}

	// 处理
	@SubscribeEvent
	public void entityInteract(EntityInteract event) {
		if (event.getEntityPlayer().getEntityData().hasKey("renderViewCamera")) {
			event.setCanceled(true);
			if (event.getSide().isClient() && event.getHand().equals(EnumHand.MAIN_HAND)
					&& (event.getItemStack() == null||event.getItemStack().isEmpty())) {
				System.out.println("EntityInteract,HandType=" + event.getHand());
				ActiveTripod(Minecraft.getMinecraft().player.getName(),
						((EntityTripod) event.getWorld()
								.getEntityByID(event.getEntityPlayer().getEntityData().getInteger("renderViewCamera")))
										.getDelay());
			}
			return;
		}
		if (event.getTarget() instanceof EntityTripod) {
			Entity target = ((PlayerInteractEvent.EntityInteract) event).getTarget();
			EntityPlayer player = event.getEntityPlayer();
			if (!player.isSneaking()) {
				if (player.inventory.armorInventory.get(3) != null
						&& player.inventory.armorInventory.get(3).getItem() instanceof ItemGlassesHelmet) {
					if (player.getEntityWorld().isRemote) {
						// System.out.println("123");
						Minecraft.getMinecraft().setRenderViewEntity(target);
						Minecraft.getMinecraft().ingameGUI.setOverlayMessage(new TextComponentTranslation("chat.tripod.info"), false);
					}
					player.getEntityData().setInteger("renderViewCamera", target.getEntityId());
				}else if(!event.getWorld().isRemote&&event.getHand().equals(EnumHand.MAIN_HAND)){
					player.sendMessage(new TextComponentTranslation("chat.tripod.mustuseglass"));
				}
			} else {
				player.getEntityData().setInteger("usingGui", target.getEntityId());
				player.openGui(MineCamera.instance, GuiElementLoader.GUI_TRIPOD_CAMERA, target.getEntityWorld(),
						(int) target.posX, (int) target.posY, (int) target.posZ);
			}
		}
	}

	// 只用于阻止脚架拍摄交互其他实体以及对相机实体的操作
	@SubscribeEvent
	public void playerInteract(PlayerInteractEvent event) {
		// System.out.println(event.toString());
		// System.out.println("PlayerInteractEvent,HandType="+event.getHand()+",PlayerAciveHand="+event.getEntityPlayer().getActiveHand());
		if (event.isCancelable() && event.getEntityPlayer().getEntityData().hasKey("renderViewCamera")) {
			event.setCanceled(true);
			return;
		}
		/*
		 * if(event instanceof
		 * PlayerInteractEvent.EntityInteract&&((PlayerInteractEvent.
		 * EntityInteract) event).getTarget() instanceof EntityTripod){ Entity
		 * target=((PlayerInteractEvent.EntityInteract) event).getTarget();
		 * EntityPlayer player =event.getEntityPlayer(); if
		 * (!player.isSneaking()) { if (player.getEntityWorld().isRemote) {
		 * System.out.println("123");
		 * Minecraft.getMinecraft().setRenderViewEntity(target); }
		 * player.getEntityData().setInteger("renderViewCamera",
		 * target.getEntityId()); }else{
		 * player.getEntityData().setInteger("usingGui", target.getEntityId());
		 * player.openGui(MineCamera.instance,
		 * GuiElementLoader.GUI_TRIPOD_CAMERA, target.getEntityWorld(),
		 * (int)target.posX, (int)target.posY, (int)target.posZ); } }
		 */
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		// System.out.println(event.toString());
		if (Minecraft.getMinecraft().gameSettings.keyBindSneak.isPressed()) {
			if (!EntityTripod.islock) {
				Minecraft.getMinecraft().setRenderViewEntity(mc.player);
				// Minecraft.getMinecraft().entityRenderer.loadEntityShader(Minecraft.getMinecraft().thePlayer);
				if (mc.player.getEntityData().hasKey("renderViewCamera")) {

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
					mc.player.getEntityData().removeTag("renderViewCamera");
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
		if (event.getEntity() instanceof EntityPlayer) {
			event.getEntity().getEntityData().removeTag("renderViewCamera");
			event.getEntity().getEntityData().removeTag("usingGui");

		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderHand(RenderHandEvent event) {
		if (Minecraft.getMinecraft().player.getEntityData().hasKey("renderViewCamera")) {
			event.setCanceled(true);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderGameOverlay(RenderGameOverlayEvent event) {
		if ((Minecraft.getMinecraft().getRenderViewEntity() instanceof EntityTripod)) {
			if (event.getType().equals(ElementType.HOTBAR)) {
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
						TextFormatting.GREEN.BOLD + (EntityTripod.islock ? locked : unlocked), 82,
						PictureFactory.getMcScaledHeight() - 40, 0x99FFFF);
			}
			if (event.getType().equals(ElementType.EXPERIENCE)) {
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
		//TODO
/*		if(Block.getBlockFromItem(event.crafting.getItem()) instanceof BlockPhotoProcessor &&!event.player.getEntityWorld().isRemote&&!event.player.hasAchievement(AchievementLoader.craftprocessor)){
			event.player.addStat(AchievementLoader.craftprocessor);
		}
		if(Block.getBlockFromItem(event.crafting.getItem()) instanceof BlockPictureFrame &&!event.player.getEntityWorld().isRemote&&!event.player.hasAchievement(AchievementLoader.craftpictureframe)){
			event.player.addStat(AchievementLoader.craftpictureframe);
		}
		if(Block.getBlockFromItem(event.crafting.getItem()) instanceof BlockPictureFrameMultiple &&!event.player.getEntityWorld().isRemote&&!event.player.hasAchievement(AchievementLoader.craftpictureframe_multiple)){
			event.player.addStat(AchievementLoader.craftpictureframe_multiple);
		}
		if(event.crafting.getItem() instanceof ItemPhotoPaper&!event.player.getEntityWorld().isRemote&&!event.player.hasAchievement(AchievementLoader.craftphoto_paper)){
			event.player.addStat(AchievementLoader.craftphoto_paper);
		}
		if(event.crafting.getItem() instanceof ItemGlassesHelmet&!event.player.getEntityWorld().isRemote&&!event.player.hasAchievement(AchievementLoader.craftglasses)){
			event.player.addStat(AchievementLoader.craftglasses);
		}
		if(event.crafting.getItem() instanceof ItemFilm&&!event.player.getEntityWorld().isRemote&&!event.player.hasAchievement(AchievementLoader.craftfilm)){
			event.player.addStat(AchievementLoader.craftfilm);
		}
		if(event.crafting.getItem() instanceof ItemTripod&&!event.player.getEntityWorld().isRemote&&!event.player.hasAchievement(AchievementLoader.crafttripod)){
			event.player.addStat(AchievementLoader.crafttripod);
		}
		if(event.crafting.getItem() instanceof ItemPictureBook&&!event.player.getEntityWorld().isRemote&&!event.player.hasAchievement(AchievementLoader.craftpicture_book)){
			event.player.addStat(AchievementLoader.craftpicture_book);
		}
		*/
		if (event.crafting.getItem() instanceof ItemCamera) {
			for (int i = 0; i < 9; i++) {
				if (event.craftMatrix.getStackInSlot(i) != null
						&& event.craftMatrix.getStackInSlot(i).getItem() instanceof ItemTripod) {
//					if (!event.player.inventory.addItemStackToInventory(new ItemStack(ItemLoader.itemCamera))) {
//						if (!event.player.getEntityWorld().isRemote) {
//							Block.spawnAsEntity(event.player.getEntityWorld(), event.player.getPosition(),
//									new ItemStack(ItemLoader.itemCamera));
//						}
//					}
					if (!event.player.inventory.addItemStackToInventory(new ItemStack(Blocks.IRON_BLOCK, 3))) {
						if (!event.player.getEntityWorld().isRemote) {
							Block.spawnAsEntity(event.player.getEntityWorld(), event.player.getPosition(),
									new ItemStack(Blocks.IRON_BLOCK, 3));
						}
					}
					if (!event.player.inventory.addItemStackToInventory(new ItemStack(Items.ENDER_EYE))) {
						if (!event.player.getEntityWorld().isRemote) {
							Block.spawnAsEntity(event.player.getEntityWorld(), event.player.getPosition(),
									new ItemStack(Items.ENDER_EYE));
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
