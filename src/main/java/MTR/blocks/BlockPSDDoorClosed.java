package MTR.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockPSDDoorClosed extends BlockDoorClosedBase {

	private static final String name = "BlockPSDDoorClosed";

	public static final PropertyInteger END = PropertyInteger.create("end", 0, 3);

	public BlockPSDDoorClosed() {
		super(name);
		setDefaultState(blockState.getBaseState().withProperty(END, 0).withProperty(FACING, EnumFacing.NORTH)
				.withProperty(SIDE, false).withProperty(TOP, false));
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		Block block1 = worldIn.getBlockState(pos.north()).getBlock();
		Block block2 = worldIn.getBlockState(pos.east()).getBlock();
		Block block3 = worldIn.getBlockState(pos.south()).getBlock();
		Block block4 = worldIn.getBlockState(pos.west()).getBlock();
		boolean end = block1 instanceof BlockPSDGlassEnd || block2 instanceof BlockPSDGlassEnd
				|| block3 instanceof BlockPSDGlassEnd || block4 instanceof BlockPSDGlassEnd;
		boolean side = state.getValue(SIDE);
		return state.withProperty(END, (side ? 1 : 0) + (end ? 2 : 0));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { END, FACING, SIDE, TOP });
	}
}
