package com.porpit.minecamera.block;

import java.util.List;

import javax.annotation.Nullable;

import com.porpit.minecamera.block.BlockPictureFrame.EnumStateType;
import com.porpit.minecamera.creativetab.CreativeTabsLoader;
import com.porpit.minecamera.item.ItemLoader;
import com.porpit.minecamera.tileentity.TileEntityPictureFrame;
import com.porpit.minecamera.tileentity.TileEntityPictureFrameMultiple;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.collection.parallel.ParIterableLike.Forall;
import scala.reflect.internal.util.TableDef.Column;

public class BlockPictureFrameMultiple extends BlockContainer {
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	// public static final PropertyBool ISHEAD = PropertyBool.create("ishead");
	// public static final PropertyInteger LINE =
	// PropertyInteger.create("line",0,8);
	// public static final PropertyInteger COLUMN =
	// PropertyInteger.create("column",0,5);
	public static final PropertyInteger RENDERTYPE = PropertyInteger.create("rendertype", 0, 8);

	public BlockPictureFrameMultiple() {
		super(Material.GROUND);
		this.setUnlocalizedName("pictureframe_multiple");
		this.setHardness(0.5F);
		this.setSoundType(SoundType.WOOD);
		this.setCreativeTab(CreativeTabsLoader.tabMineCamera);
		/*
		 * this.setDefaultState(
		 * this.blockState.getBaseState().withProperty(FACING,
		 * EnumFacing.NORTH).withProperty(ISHEAD, false).withProperty(LINE,
		 * 1).withProperty(COLUMN, 1).withProperty(RENDERTYPE, 0));
		 */
		this.setDefaultState(
				this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(RENDERTYPE, 0));
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		// System.out.println("1");
		int hasleft = 0;
		int hasright = 0;
		int hasup = 0;
		int hasdown = 0;
		boolean islevelhead = false;
		boolean isverticalhead = false;
		int width = 1;
		int height = 1;
		EnumFacing facing = state.getValue(FACING).getOpposite().rotateY();
		EnumFacing facingfather = facing.getOpposite();
		// 左下查询
		// if(state.getValue(ISHEAD) !=true){
		if (!worldIn.isAirBlock(pos.offset(facingfather))
				&& worldIn.getBlockState(pos.offset(facingfather)).getBlock() instanceof BlockPictureFrameMultiple) {
			hasleft = 1;
			// System.out.println(worldIn.getBlockState(pos.offset(facingfather,1)).getActualState(worldIn,
			// pos.offset(facingfather,1)).getValue(LINE)+","+pos.offset(facingfather));
			/*
			 * if(worldIn.getBlockState(pos.offset(facingfather,1)).
			 * getActualState(worldIn,
			 * pos.offset(facingfather,1)).getValue(LINE)!=0) state =
			 * state.withProperty(LINE,
			 * worldIn.getBlockState(pos.offset(facingfather)).getActualState(
			 * worldIn, pos.offset(facingfather,1)).getValue(LINE)+1); }else{
			 * //System.out.println("1"); state = state.withProperty(LINE, 1);
			 */
		} else {
			islevelhead = true;
		}
		if (!worldIn.isAirBlock(pos.down())
				&& worldIn.getBlockState(pos.down()).getBlock() instanceof BlockPictureFrameMultiple) {
			hasdown = 1;
			/*
			 * if(worldIn.getBlockState(pos.down()).getActualState(worldIn,
			 * pos.down()).getValue(COLUMN)!=0) state =
			 * state.withProperty(COLUMN,
			 * worldIn.getBlockState(pos.down()).getActualState(worldIn,
			 * pos.down()).getValue(COLUMN)+1); }else{ state =
			 * state.withProperty(COLUMN, 1);
			 */
		} else {
			isverticalhead = true;
		}
		// }else{
		// state = state.withProperty(RENDERTYPE, 1);
		// }
		// 右上查询
		if (!worldIn.isAirBlock(pos.offset(facing))
				&& worldIn.getBlockState(pos.offset(facing)).getBlock() instanceof BlockPictureFrameMultiple) {
			hasright = 1;
			if (islevelhead && isverticalhead) {
				width++;
				// max loop 100
				for (int i = 2; i < 100; i++) {
					if (!worldIn.isAirBlock(pos.offset(facing, i)) && worldIn.getBlockState(pos.offset(facing, i))
							.getBlock() instanceof BlockPictureFrameMultiple) {
						width++;
					} else {
						break;
					}
				}
			}
		}
		if (!worldIn.isAirBlock(pos.up())
				&& worldIn.getBlockState(pos.up()).getBlock() instanceof BlockPictureFrameMultiple) {
			hasup = 1;
			if (islevelhead && isverticalhead) {
				height++;
				for (int i = 2; i < 100; i++) {
					if (!worldIn.isAirBlock(pos.up(i))
							&& worldIn.getBlockState(pos.up(i)).getBlock() instanceof BlockPictureFrameMultiple) {
						height++;
					} else {
						break;
					}
				}
			}
		}
		int out = (hasleft << 3) | (hasright << 2) | (hasup << 1) | hasdown;
		// System.out.println(pos+""+hasleft+""+hasright+""+hasup+""+hasdown+"");
		// System.out.println("height:" + height);
		// System.out.println("width:" + width);
		switch (out) {
		case 15:
			state = state.withProperty(RENDERTYPE, 0);
			break;
		case 7:
			state = state.withProperty(RENDERTYPE, 5);
			break;
		case 11:
			state = state.withProperty(RENDERTYPE, 7);
			break;
		case 13:
			state = state.withProperty(RENDERTYPE, 8);
			break;
		case 14:
			state = state.withProperty(RENDERTYPE, 6);
			break;
		case 10:
			state = state.withProperty(RENDERTYPE, 2);
			break;
		case 6:
			state = state.withProperty(RENDERTYPE, 1);
			break;
		case 5:
			state = state.withProperty(RENDERTYPE, 4);
			break;
		case 9:
			state = state.withProperty(RENDERTYPE, 3);
			break;
		default:
			break;
		}
		return state;
	}

	@Override
	public boolean isFullyOpaque(IBlockState state) {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityPictureFrameMultiple();
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	/*
	 * @SideOnly(Side.CLIENT)
	 * 
	 * @Override public BlockRenderLayer getBlockLayer() { return
	 * BlockRenderLayer.CUTOUT; }
	 */

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing facing = EnumFacing.getHorizontal(meta & 3);
		Boolean ishead = Boolean.valueOf((meta & 4) != 0);
		// return this.getDefaultState().withProperty(FACING,
		// facing).withProperty(ISHEAD, ishead);
		return this.getDefaultState().withProperty(FACING, facing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int facing = state.getValue(FACING).getHorizontalIndex();
		// System.out.println(facing);
		// int ishead = state.getValue(ISHEAD).booleanValue() ? 4 : 0;
		// return facing | ishead;
		return facing;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see net.minecraft.block.Block#onBlockPlacedBy(net.minecraft.world.World,
	 * net.minecraft.util.math.BlockPos, net.minecraft.block.state.IBlockState,
	 * net.minecraft.entity.EntityLivingBase, net.minecraft.item.ItemStack)
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		// IBlockState newstate =state.withProperty(ISHEAD, false);
		if (worldIn.getBlockState(pos.down()).getBlock() instanceof BlockPictureFrameMultiple) {
			state = state.withProperty(FACING, worldIn.getBlockState(pos.down()).getValue(FACING));
		} else if (worldIn.getBlockState(pos.up()).getBlock() instanceof BlockPictureFrameMultiple) {
			state = state.withProperty(FACING, worldIn.getBlockState(pos.up()).getValue(FACING));
		} else if (worldIn.getBlockState(pos.east()).getBlock() instanceof BlockPictureFrameMultiple) {
			state = state.withProperty(FACING, worldIn.getBlockState(pos.east()).getValue(FACING));
		} else if (worldIn.getBlockState(pos.west()).getBlock() instanceof BlockPictureFrameMultiple) {
			state = state.withProperty(FACING, worldIn.getBlockState(pos.west()).getValue(FACING));
		} else if (worldIn.getBlockState(pos.north()).getBlock() instanceof BlockPictureFrameMultiple) {
			state = state.withProperty(FACING, worldIn.getBlockState(pos.north()).getValue(FACING));
		} else if (worldIn.getBlockState(pos.south()).getBlock() instanceof BlockPictureFrameMultiple) {
			state = state.withProperty(FACING, worldIn.getBlockState(pos.south()).getValue(FACING));
		}
		// resetfacing
		worldIn.setBlockState(pos, state);
		EnumFacing blockfacing = state.getValue(FACING);
		EnumFacing facing = state.getValue(FACING).getOpposite().rotateY();
		int width = 1;
		int height = 1;
		for (int i = 1; i < 50; i++) {
			if (worldIn.getBlockState(pos.offset(facing.getOpposite(), i))
					.getBlock() instanceof BlockPictureFrameMultiple) {
				width++;
			} else {
				break;
			}
		}
		for (int i = 1; i < 50; i++) {
			if (worldIn.getBlockState(pos.offset(facing, i)).getBlock() instanceof BlockPictureFrameMultiple) {
				width++;
			} else {
				break;
			}
		}
		for (int i = 1; i < 50; i++) {
			if (worldIn.getBlockState(pos.up(i)).getBlock() instanceof BlockPictureFrameMultiple) {
				height++;
			} else {
				break;
			}
		}
		for (int i = 1; i < 50; i++) {
			if (worldIn.getBlockState(pos.down(i)).getBlock() instanceof BlockPictureFrameMultiple) {
				height++;
			} else {
				break;
			}
		}
		//System.out.println("width:" + width);
		if (width > 16) {
			worldIn.setBlockToAir(pos);
			placer.addChatMessage(new TextComponentTranslation("chat.framemultiple.widthbuildtoomore", 16));
			return;
		}
		if (height > 16) {
			worldIn.setBlockToAir(pos);
			placer.addChatMessage(new TextComponentTranslation("chat.framemultiple.heightbuildtoomore", 16));
			return;
		}
		if (placer.isSneaking() && stack.stackSize >= 6) {
			if (width + 2 > 16) {
				worldIn.setBlockToAir(pos);
				placer.addChatMessage(new TextComponentTranslation("chat.framemultiple.widthbuildtoomore", 16));
				return;
			}
			if (height + 1 > 16) {
				worldIn.setBlockToAir(pos);
				placer.addChatMessage(new TextComponentTranslation("chat.framemultiple.heightbuildtoomore", 16));
				return;
			}
			stack.stackSize -= 5;
			// 右方
			for (int i = 1; i < 3; i++) {
				if (!worldIn.isAirBlock(pos.offset(facing, i)) || !worldIn.isAirBlock(pos.up().offset(facing, i))) {
					worldIn.setBlockToAir(pos);
					if (!worldIn.isRemote) {
						placer.addChatMessage(new TextComponentTranslation("chat.framemultiple.failmultipleplace"));
					}
					return;
				}
			}
			// 右边界检查
			if (worldIn.getBlockState(pos.offset(facing, 3)).getBlock() instanceof BlockPictureFrameMultiple || worldIn
					.getBlockState(pos.offset(facing, 3).up()).getBlock() instanceof BlockPictureFrameMultiple) {
				worldIn.setBlockToAir(pos);
				if (!worldIn.isRemote) {
					placer.addChatMessage(new TextComponentTranslation("chat.framemultiple.failmultipleplace"));
				}
				return;
			}
			// 上方&&前后&上边界检查
			for (int i = 0; i <= 3; i++) {
				if (!worldIn.isAirBlock(pos.offset(facing, i).up(1))
						|| !worldIn.isAirBlock(pos.up().offset(facing, i).up(2))
						|| worldIn.getBlockState(pos.up().offset(facing, i).up(3))
								.getBlock() instanceof BlockPictureFrameMultiple) {
					worldIn.setBlockToAir(pos);
					if (!worldIn.isRemote) {
						placer.addChatMessage(new TextComponentTranslation("chat.framemultiple.failmultipleplace"));
					}
					return;
				}
				if (worldIn.getBlockState(pos.offset(facing, i).offset(blockfacing, 1))
						.getBlock() instanceof BlockPictureFrameMultiple
						|| worldIn.getBlockState(pos.offset(facing, i).offset(blockfacing, 1).up())
								.getBlock() instanceof BlockPictureFrameMultiple
						|| (worldIn.getBlockState(pos.offset(facing, i).offset(blockfacing, -1))
								.getBlock() instanceof BlockPictureFrameMultiple
								|| worldIn.getBlockState(pos.offset(facing, i).offset(blockfacing, -1).up())
										.getBlock() instanceof BlockPictureFrameMultiple)) {
					worldIn.setBlockToAir(pos);
					if (!worldIn.isRemote) {
						placer.addChatMessage(new TextComponentTranslation("chat.framemultiple.failmultipleplace"));
					}
					return;
				}
			}
			worldIn.setBlockState(pos.up(), state);
			worldIn.setBlockState(pos.up().offset(facing, 1), state);
			worldIn.setBlockState(pos.up().offset(facing, 2), state);
			worldIn.setBlockState(pos.offset(facing, 1), state);
			worldIn.setBlockState(pos.offset(facing, 2), state);

			worldIn.setBlockState(pos.up(), state);
			worldIn.setBlockState(pos.up().offset(facing, 1), state);
			worldIn.setBlockState(pos.up().offset(facing, 2), state);
			worldIn.setBlockState(pos.offset(facing, 1), state);
			worldIn.setBlockState(pos.offset(facing, 2), state);
		}
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos){
		return true;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		// return new BlockStateContainer(this, FACING,
		// ISHEAD,LINE,COLUMN,RENDERTYPE);
		return new BlockStateContainer(this, FACING, RENDERTYPE);
	}

	@Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
		if (worldIn.getBlockState(pos.down()).getBlock() instanceof BlockPictureFrameMultiple) {
			return this.canPlaceBlockOnSide(worldIn, pos, side);
		}
		if (worldIn.getBlockState(pos.up()).getBlock() instanceof BlockPictureFrameMultiple) {
			EnumFacing facing = worldIn.getBlockState(pos.up()).getValue(FACING).getOpposite().rotateY();
			EnumFacing fatherfacing = facing.getOpposite();
			if (!(worldIn.getBlockState(pos.offset(fatherfacing)).getBlock() instanceof BlockPictureFrameMultiple)) {
				return false;
			}
		}
		if (worldIn.getBlockState(pos.west()).getBlock() instanceof BlockPictureFrameMultiple) {
			EnumFacing facing = worldIn.getBlockState(pos.west()).getValue(FACING).getOpposite().rotateY();
			EnumFacing fatherfacing = facing.getOpposite();
			if (!(worldIn.getBlockState(pos.west()).getBlock() instanceof BlockPictureFrameMultiple)
					|| !worldIn.getBlockState(pos.west()).getValue(FACING).equals(EnumFacing.SOUTH)) {
				return false;
			}
		}
		if (worldIn.getBlockState(pos.east()).getBlock() instanceof BlockPictureFrameMultiple) {
			EnumFacing facing = worldIn.getBlockState(pos.east()).getValue(FACING).getOpposite().rotateY();
			EnumFacing fatherfacing = facing.getOpposite();
			if (!(worldIn.getBlockState(pos.east()).getBlock() instanceof BlockPictureFrameMultiple)
					|| !worldIn.getBlockState(pos.east()).getValue(FACING).equals(EnumFacing.NORTH)) {
				return false;
			}
		}
		if (worldIn.getBlockState(pos.north()).getBlock() instanceof BlockPictureFrameMultiple) {
			EnumFacing facing = worldIn.getBlockState(pos.north()).getValue(FACING).getOpposite().rotateY();
			EnumFacing fatherfacing = facing.getOpposite();
			if (!(worldIn.getBlockState(pos.north()).getBlock() instanceof BlockPictureFrameMultiple)
					|| !worldIn.getBlockState(pos.north()).getValue(FACING).equals(EnumFacing.WEST)) {
				return false;
			}
		}
		if (worldIn.getBlockState(pos.south()).getBlock() instanceof BlockPictureFrameMultiple) {
			EnumFacing facing = worldIn.getBlockState(pos.south()).getValue(FACING).getOpposite().rotateY();
			EnumFacing fatherfacing = facing.getOpposite();
			if (!(worldIn.getBlockState(pos.south()).getBlock() instanceof BlockPictureFrameMultiple)
					|| !worldIn.getBlockState(pos.south()).getValue(FACING).equals(EnumFacing.EAST)) {
				return false;
			}
		}
		return this.canPlaceBlockOnSide(worldIn, pos, side);
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player,
			boolean willHarvest) {
		EnumFacing facing = state.getValue(FACING).getOpposite().rotateY();
		EnumFacing fatherfacing = facing.getOpposite();
		if (player.isSneaking()) {
			int width = 1;
			int height = 1;
			for (int i = 1; i < 50; i++) {
				if (world.getBlockState(pos.offset(facing, i)).getBlock() instanceof BlockPictureFrameMultiple) {
					width++;
					world.setBlockToAir(pos.offset(facing, i));
				} else {
					break;
				}
			}
			for (int i = 1; i < 50; i++) {
				if (world.getBlockState(pos.up(i)).getBlock() instanceof BlockPictureFrameMultiple) {
					height++;
					world.setBlockToAir(pos.up(i));
				} else {
					break;
				}
			}
			for (int i = 1; i < width; i++) {
				for (int j = 1; j < height; j++) {
					if (world.getBlockState(pos.offset(facing, i).up(j))
							.getBlock() instanceof BlockPictureFrameMultiple) {
						world.setBlockToAir(pos.offset(facing, i).up(j));
					}
				}
			}
		}

		for (int i = 1; i < 50; i++) {
			if (!(world.getBlockState(pos.offset(fatherfacing, i)).getBlock() instanceof BlockPictureFrameMultiple)) {
				for (int j = 1; j < 50; j++) {
					if (!(world.getBlockState(pos.offset(fatherfacing, i - 1).down(j))
							.getBlock() instanceof BlockPictureFrameMultiple)) {
						TileEntityPictureFrameMultiple te = (TileEntityPictureFrameMultiple) world
								.getTileEntity(pos.offset(fatherfacing, i - 1).down(j - 1));
						if (te != null) {
							//System.out.println(pos.offset(fatherfacing, i - 1).down(j - 1));
							if (!te.imagename.equals("")) {
								if (!world.isRemote) {
									ItemStack picture = new ItemStack(ItemLoader.itemPicture);
									NBTTagCompound nbt = new NBTTagCompound();
									nbt.setString("pid", te.imagename);
									picture.setTagCompound(nbt);
									Block.spawnAsEntity(world, pos, picture);
									player.addChatComponentMessage(
											new TextComponentTranslation("chat.framemultiple.changeanddrowpicture"));
								}
								te.imagename = "";
								te.shouldrender = false;
							}
						}
						break;
					}
				}
				break;
			}
		}
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		EnumFacing facingfather = state.getValue(FACING).getOpposite().rotateY().getOpposite();
		/*
		 * int line = state.getActualState(worldIn, pos).getValue(LINE); int
		 * column=state.getActualState(worldIn, pos).getValue(COLUMN);
		 * if(line>1){ System.out.println("2");
		 * if(worldIn.getBlockState(pos.offset(facingfather)).getBlock()
		 * instanceof BlockPictrueFrameMultiple){
		 * worldIn.setBlockToAir(pos.offset(facingfather,1)); } } if(column>1){
		 * if(worldIn.getBlockState(pos.down()).getBlock() instanceof
		 * BlockPictrueFrameMultiple){ worldIn.setBlockToAir(pos.down()); } }
		 * if(worldIn.getBlockState(pos.offset(facingfather.getOpposite())).
		 * getBlock() instanceof BlockPictrueFrameMultiple){
		 * worldIn.setBlockToAir(pos.offset(facingfather.getOpposite())); }
		 * if(worldIn.getBlockState(pos.up()).getBlock() instanceof
		 * BlockPictrueFrameMultiple){ worldIn.setBlockToAir(pos.up()); }
		 * for(int i=0;i<line;i++){ for(int j=0;j<column;j++){
		 * worldIn.setBlockToAir(pos.offset(facingfather,i).offset(EnumFacing.
		 * DOWN,j)); } }
		 */
		super.breakBlock(worldIn, pos, state);

	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		// System.out.println("LINE:"+state.getValue(LINE));
		if (heldItem != null && heldItem.getItem().equals(ItemLoader.itemPicture) && heldItem.hasTagCompound()
				&& heldItem.getTagCompound().hasKey("pid")) {
			int width = 1;
			int height = 1;
			EnumFacing buildfacing = state.getValue(FACING).getOpposite().rotateY();
			EnumFacing buildfatherfacing = state.getValue(FACING).getOpposite().rotateY().getOpposite();
			Long timebefore = System.currentTimeMillis();
			// 计算 width & height
			if ((worldIn.isAirBlock(pos.offset(buildfatherfacing)) || !(worldIn
					.getBlockState(pos.offset(buildfatherfacing)).getBlock() instanceof BlockPictureFrameMultiple))
					&& (worldIn.isAirBlock(pos.down())
							|| !(worldIn.getBlockState(pos.down()).getBlock() instanceof BlockPictureFrameMultiple))) {
				for (int i = 1; i < 100; i++) {
					if (!worldIn.isAirBlock(pos.offset(buildfacing, i))
							&& worldIn.getBlockState(pos.offset(buildfacing, i))
									.getBlock() instanceof BlockPictureFrameMultiple) {
						width++;
					} else {
						break;
					}
				}
				for (int i = 1; i < 100; i++) {
					if (!worldIn.isAirBlock(pos.up(i))
							&& worldIn.getBlockState(pos.up(i)).getBlock() instanceof BlockPictureFrameMultiple) {
						height++;
					} else {
						break;
					}
				}
				// 检查是否长宽等于1
				if (width == 1 || height == 1) {
					if (!worldIn.isRemote) {
						playerIn.addChatComponentMessage(
								new TextComponentTranslation("chat.framemultiple.widthorheighttoosmall"));
					}
					TileEntityPictureFrameMultiple te = (TileEntityPictureFrameMultiple) worldIn.getTileEntity(pos);
					te.shouldrender = false;
					return true;
				}
				// System.out.println("width:" + width);
				// System.out.println("height:" + height);
				// 检索中间是否缺失
				for (int i = 1; i < width; i++) {
					for (int j = 1; j < height; j++) {
						if ((worldIn.isAirBlock(pos.offset(buildfacing, i).up(j))
								|| !(worldIn.getBlockState(pos.offset(buildfacing, i).up(j))
										.getBlock() instanceof BlockPictureFrameMultiple))) {
							if (!worldIn.isRemote) {
								playerIn.addChatComponentMessage(
										new TextComponentTranslation("chat.framemultiple.failedtobuild"));
							}
							TileEntityPictureFrameMultiple te = (TileEntityPictureFrameMultiple) worldIn
									.getTileEntity(pos);
							if (!worldIn.isRemote && !te.imagename.equals("")) {
								ItemStack picture = new ItemStack(ItemLoader.itemPicture);
								NBTTagCompound nbt = new NBTTagCompound();
								nbt.setString("pid", te.imagename);
								picture.setTagCompound(nbt);
								Block.spawnAsEntity(worldIn, pos, picture);
								// worldIn.spawnEntityInWorld(new
								// EntityItem(worldIn, pos.getX(), pos.getY(),
								// pos.getZ(), ));
							}
							te.shouldrender = false;
							te.imagename = "";
							return true;
						}
					}
				}
				// 检索边缘是否多余
				boolean hasuselesspart = false;
				for (int i = 0; i < width; i++) {
					if (!worldIn.isAirBlock(pos.offset(buildfacing, i).down())
							&& worldIn.getBlockState(pos.offset(buildfacing, i).down())
									.getBlock() instanceof BlockPictureFrameMultiple) {
						worldIn.setBlockToAir(pos.offset(buildfacing, i).down());
						hasuselesspart = true;
					}
					if (!worldIn.isAirBlock(pos.offset(buildfacing, i).up(height))
							&& worldIn.getBlockState(pos.offset(buildfacing, i).up(height))
									.getBlock() instanceof BlockPictureFrameMultiple) {
						worldIn.setBlockToAir(pos.offset(buildfacing, i).up(height));
						hasuselesspart = true;
					}
				}
				for (int i = 0; i < height; i++) {
					if (!worldIn.isAirBlock(pos.offset(buildfatherfacing).up(i))
							&& worldIn.getBlockState(pos.offset(buildfatherfacing).up(i))
									.getBlock() instanceof BlockPictureFrameMultiple) {
						worldIn.setBlockToAir(pos.offset(buildfatherfacing).up(i));
						hasuselesspart = true;
					}
					if (!worldIn.isAirBlock(pos.offset(buildfacing, width).up(i))
							&& worldIn.getBlockState(pos.offset(buildfacing, width).up(i))
									.getBlock() instanceof BlockPictureFrameMultiple) {
						worldIn.setBlockToAir(pos.offset(buildfacing, width).up(i));
						hasuselesspart = true;
					}
				}
				if (hasuselesspart) {
					if (!worldIn.isRemote) {
						playerIn.addChatComponentMessage(
								new TextComponentTranslation("chat.framemultiple.removeuselesspart"));
					}
				}

				TileEntityPictureFrameMultiple te = (TileEntityPictureFrameMultiple) worldIn.getTileEntity(pos);
				if (!te.imagename.equals("")) {
					if (!te.getWorld().isRemote) {
						ItemStack picture = new ItemStack(ItemLoader.itemPicture);
						NBTTagCompound nbt = new NBTTagCompound();
						nbt.setString("pid", te.imagename);
						picture.setTagCompound(nbt);
						Block.spawnAsEntity(worldIn, pos, picture);
					}
				}

				te.imagename = heldItem.getTagCompound().getString("pid");
				te.shouldrender = true;
				te.width = width;
				te.height = height;
				if(!worldIn.isRemote){
					heldItem.stackSize--;
				}
				//System.out.println("可以放置,消耗时间" + (System.currentTimeMillis() - timebefore));
			} else {
				if (!worldIn.isRemote) {
					playerIn.addChatComponentMessage(new TextComponentTranslation("chat.framemultiple.mustusehead"));
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		// System.out.println("123s");
		int width = 1;
		int height = 1;
		if (source.getTileEntity(pos) != null
				&& ((TileEntityPictureFrameMultiple) source.getTileEntity(pos)).shouldrender) {
			TileEntityPictureFrameMultiple te = (TileEntityPictureFrameMultiple) source.getTileEntity(pos);
			width = te.width;
			height = te.height;
		}
		switch (state.getValue(FACING).getIndex()) {
		// north
		case 2:
			return new AxisAlignedBB(-(width - 1), 0.0D, 1.0D, 1.0D, height, 0.9D);
		// south
		case 3:
			return new AxisAlignedBB(0.0D, 0.0D, 0.0D, width, height, 0.1D);
		// west
		case 4:
			return new AxisAlignedBB(0.9D, 0.0D, 0.0D, 1.0D, height, width);
		// east
		case 5:
			return new AxisAlignedBB(0.0D, 0.0D, -(width - 1), 0.1D, height, 1.0D);
		default:
			return new AxisAlignedBB(0.0D, 0.1D, 0.95D, 1.0D, 0.9D, 1.0D);
		}
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {
		// System.out.println("hitX:=" + hitX + ",hitY:" + hitY + ",hitZ:" +
		// hitZ);
		//System.out.println(worldIn.getLight(pos));
		IBlockState origin = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
		// placer.getHorizontalFacing().
		// return origin.withProperty(FACING,
		// placer.getHorizontalFacing().getOpposite()).withProperty(ISHEAD,
		// true).withProperty(LINE, 1).withProperty(COLUMN, 1);
		return origin.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add(TextFormatting.BLUE + I18n.format("lore.pictureframe_multiple.info"));
		tooltip.add(TextFormatting.BLUE + I18n.format("lore.pictureframe_multiple.info2"));
		tooltip.add(TextFormatting.BLUE + I18n.format("lore.pictureframe_multiple.info3"));
	}
}
