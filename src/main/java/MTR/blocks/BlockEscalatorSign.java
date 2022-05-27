package MTR.blocks;

import java.util.Random;

import MTR.MTRBlocks;
import MTR.items.ItemBrush;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEscalatorSign extends BlockWithDirection {

	private static final String name = "BlockEscalatorSign";
	public static final PropertyInteger SIGN = PropertyInteger.create("sign", 0, 2);
	public static final PropertyInteger TOP = PropertyInteger.create("top", 0, 2);
	public static final PropertyBool TYPE = PropertyBool.create("type");

	public BlockEscalatorSign() {
		super(name);
		setCreativeTab(null);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SIGN, 0)
				.withProperty(TOP, 0).withProperty(TYPE, false));
		setLightLevel(0.3125F);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		int sign = 0;
		int top = 0;
		try {
			IBlockState stateSide = worldIn.getBlockState(pos.down(2));
			boolean type = state.getValue(TYPE);
			EnumFacing facingThis = state.getValue(FACING);
			EnumFacing facingSide = stateSide.getValue(BlockEscalatorSideLanding.FACING);

			Block block = worldIn.getBlockState(pos.offset(facingThis.getOpposite())).getBlock();
			if (block instanceof BlockMTRSignPost || block instanceof BlockEscalatorSign)
				top = 1;
			if (facingThis == facingSide && worldIn.getBlockState(pos.up()).getMaterial().isOpaque())
				top = 2;
			if (facingThis != facingSide && worldIn.getBlockState(pos.offset(facingThis).up()).getMaterial().isOpaque())
				top = 0;

			IBlockState stateStep = worldIn.getBlockState(pos.down(3).offset(facingSide.rotateYCCW()));
			EnumFacing facingStep = stateStep.getValue(BlockEscalatorStep.FACING);
			int direction = stateStep.getValue(BlockEscalatorStep.UP);
			if (direction == 1 && facingSide != facingStep || direction == 2 && facingSide == facingStep)
				sign = type ? 2 : 1;
		} catch (Exception e) {
		}
		return state.withProperty(SIGN, sign).withProperty(TOP, top);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		BlockPos posSide = pos.offset(state.getValue(FACING));
		if (!(worldIn.getBlockState(posSide).getBlock() instanceof BlockEscalatorSign
				&& worldIn.getBlockState(pos.down(2)).getBlock() instanceof BlockEscalatorSideLanding
				&& worldIn.getBlockState(posSide.down(2)).getBlock() instanceof BlockEscalatorSideLanding)) {
			worldIn.setBlockToAir(pos);
			worldIn.setBlockToAir(pos.down(2));
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing var3 = state.getValue(FACING);
		switch (var3.getHorizontalIndex()) {
		case 0:
			return new AxisAlignedBB(0.4375F, 0.0F, 0.46875F, 0.5625F, 0.5625F, 1.0F);
		case 1:
			return new AxisAlignedBB(0.0F, 0.0F, 0.4375F, 0.53125F, 0.5625F, 0.5625F);
		case 2:
			return new AxisAlignedBB(0.4375F, 0.0F, 0.0F, 0.5625F, 0.5625F, 0.53125F);
		case 3:
			return new AxisAlignedBB(0.46875F, 0.0F, 0.4375F, 1.0F, 0.5625F, 0.5625F);
		default:
			return NULL_AABB;
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack itemStack = playerIn.inventory.getCurrentItem();
		if (itemStack != null && itemStack.getItem() instanceof ItemBrush) {
			EnumFacing facing = state.getValue(FACING);
			worldIn.setBlockState(pos.offset(facing), worldIn.getBlockState(pos.offset(facing)).cycleProperty(TYPE));
			worldIn.setBlockState(pos,
					state.withProperty(TYPE, worldIn.getBlockState(pos.offset(facing)).getValue(TYPE)));
			return true;
		} else
			return false;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4)).withProperty(TYPE,
				(meta & 4) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int var3 = state.getValue(FACING).getHorizontalIndex();
		if (state.getValue(TYPE))
			var3 = var3 + 4;
		return var3;
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, SIGN, TOP, TYPE });
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(MTRBlocks.blockescalatorlanding);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(Item.getItemFromBlock(MTRBlocks.blockescalatorlanding));
	}
}
