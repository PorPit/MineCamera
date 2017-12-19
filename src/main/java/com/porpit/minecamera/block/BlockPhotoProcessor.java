package com.porpit.minecamera.block;

import javax.annotation.Nullable;

import com.porpit.minecamera.MineCamera;
import com.porpit.minecamera.creativetab.CreativeTabsLoader;
import com.porpit.minecamera.inventory.GuiElementLoader;
import com.porpit.minecamera.tileentity.TileEntityPhotoProcessor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class BlockPhotoProcessor extends BlockContainer {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockPhotoProcessor() {
		super(Material.GROUND);
		this.setUnlocalizedName("photoprocessor");
		this.setHardness(0.5F);
		this.setSoundType(SoundType.ANVIL);
		this.setCreativeTab(CreativeTabsLoader.tabMineCamera);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityPhotoProcessor();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getHorizontal(meta & 3);
		return this.getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntityPhotoProcessor te = (TileEntityPhotoProcessor) worldIn.getTileEntity(pos);

		IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);

		for (int i = inv.getSlots() - 1; i >= 0; --i) {
			if (inv.getStackInSlot(i) != null) {
				Block.spawnAsEntity(worldIn, pos, inv.getStackInSlot(i));
				((IItemHandlerModifiable) inv).setStackInSlot(i, ItemStack.EMPTY);
			}
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int facing = state.getValue(FACING).getHorizontalIndex();
		// System.out.println(facing);
		return facing;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			int id = GuiElementLoader.GUI_PHOTOPROCESSOR;
			playerIn.openGui(MineCamera.instance, id, worldIn, pos.getX(), pos.getY(), pos.getZ());
			/*
			 * Entity entity = new EntityTripod(worldIn);
			 * entity.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 0.5,
			 * pos.getZ() + 0.5); worldIn.spawnEntityInWorld(entity);
			 */
		}
		return true;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
		IBlockState origin = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
        return origin.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
	
	/*@Override
	public IBlockState onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		IBlockState origin = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
		return origin.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}*/

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
}
