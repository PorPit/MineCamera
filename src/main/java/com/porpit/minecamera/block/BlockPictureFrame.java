package com.porpit.minecamera.block;

import java.util.List;

import javax.annotation.Nullable;

import com.porpit.minecamera.creativetab.CreativeTabsLoader;
import com.porpit.minecamera.item.ItemLoader;
import com.porpit.minecamera.tileentity.TileEntityPictureFrame;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPictureFrame extends BlockContainer {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyEnum<EnumStateType> STATETYPE = PropertyEnum.create("statetype", EnumStateType.class);
	public AxisAlignedBB FACING_NORTH = new AxisAlignedBB(0.0D, 0.1D, 0.95D, 1.0D, 0.9D, 1.0D);
	
	public BlockPictureFrame() {
		super(Material.GROUND);
		this.setUnlocalizedName("pictureframe");
		this.setHardness(0.5F);
		this.setSoundType(SoundType.WOOD);
		this.setCreativeTab(CreativeTabsLoader.tabMineCamera);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH)
				.withProperty(STATETYPE, EnumStateType.STANDING));
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		// System.out.println("123");
		if (!worldIn.isRemote) {
			TileEntityPictureFrame te = (TileEntityPictureFrame) worldIn.getTileEntity(pos);
			if (!te.imagename.equals("")) {
				ItemStack picture = new ItemStack(ItemLoader.itemPicture);
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("pid", te.imagename);
				picture.setTagCompound(nbt);
				// worldIn.spawnEntityInWorld(new EntityItem(worldIn,
				// pos.getX(), pos.getY(), pos.getZ(), picture));
				Block.spawnAsEntity(worldIn, pos, picture);
			}
		}
		super.breakBlock(worldIn, pos, state);

	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (state.getValue(STATETYPE).equals(EnumStateType.HANDGING)) {
			switch (state.getValue(FACING).getIndex()) {
			// north
			case 2:
				return new AxisAlignedBB(0.0D, 0.1D, 0.95D, 1.0D, 0.9D, 1.0D);
			// south
			case 3:
				return new AxisAlignedBB(0.0D, 0.1D, 0.0D, 1.0D, 0.9D, 0.05D);
			// west
			case 4:
				return new AxisAlignedBB(0.95D, 0.1D, 0.0D, 1.0D, 0.9D, 1.0D);
			// east
			case 5:
				return new AxisAlignedBB(0.0D, 0.1D, 0.0D, 0.05D, 0.9D, 1.0D);
			default:
				return new AxisAlignedBB(0.0D, 0.1D, 0.95D, 1.0D, 0.9D, 1.0D);
			}
		} else {
			switch (state.getValue(FACING).getIndex()) {
			// north
			case 2:
				return new AxisAlignedBB(0.0D, 0.0D, 0.35D, 1.0D, 0.75D, 0.8D);
			// south
			case 3:
				return new AxisAlignedBB(0.0D, 0.0D, 0.20D, 1.0D, 0.75D, 0.65D);
			// west
			case 4:
				return new AxisAlignedBB(0.35D, 0.0D, 0.0D, 0.8D, 0.75D, 1.0D);
			// east
			case 5:
				return new AxisAlignedBB(0.2D, 0.0D, 0.0D, 0.65D, 0.75D, 1.0D);
			default:
				return new AxisAlignedBB(0.0D, 0.0D, 0.35D, 1.0D, 0.75D, 0.8D);
			}
		}
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, STATETYPE);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityPictureFrame();
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos){
		return true;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		// System.out.println(((TileEntityPictureFrame)
		// worldIn.getTileEntity(pos)).imagename);
		if (heldItem != null && heldItem.getItem().equals(ItemLoader.itemPicture) && heldItem.hasTagCompound()&&heldItem.getTagCompound().hasKey("pid")) {
			//System.out.println(hand);
			String imagename = heldItem.getTagCompound().getString("pid");
			TileEntityPictureFrame te = (TileEntityPictureFrame) worldIn.getTileEntity(pos);
			if (!worldIn.isRemote && !te.imagename.equals("")) {
				ItemStack picture = new ItemStack(ItemLoader.itemPicture);
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("pid", te.imagename);
				picture.setTagCompound(nbt);
				Block.spawnAsEntity(worldIn, pos, picture);
				//worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), ));
			}
			te.imagename = imagename;
			heldItem.stackSize--;
		}
		// playerIn.addChatComponentMessage((new
		// TextComponentTranslation("test")));
		return true;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getHorizontal(meta & 3);
		EnumStateType statetype = EnumStateType.values()[meta >> 2];
		return this.getDefaultState().withProperty(FACING, facing).withProperty(STATETYPE, statetype);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int facing = state.getValue(FACING).getHorizontalIndex();
		// System.out.println(facing);
		int statetype = state.getValue(STATETYPE).ordinal() << 2;
		return facing | statetype;
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {
		// System.out.println("hitX:=" + hitX + ",hitY:" + hitY + ",hitZ:" +
		// hitZ);
		IBlockState origin = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
		EnumStateType enumState;
		if (hitY == 1.0) {
			enumState = EnumStateType.STANDING;
		} else {
			enumState = EnumStateType.HANDGING;
		}

		return origin.withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(STATETYPE,
				enumState);
	}

	public static enum EnumStateType implements IStringSerializable {
		HANDGING("hanging"), STANDING("standing");

		private String name;

		private EnumStateType(String material) {
			this.name = material;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add(TextFormatting.BLUE+ I18n.format("lore.pictureframe.info"));
	}
}
