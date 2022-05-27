package MTR.blocks;

import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;

import MTR.EntityTrainBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRailDetector2 extends BlockRailBase2 {

	private static final String name = "BlockRailDetector";
	public static final PropertyBool POWERED = PropertyBool.create("powered");

	public BlockRailDetector2() {
		super(name);
		setDefaultState(blockState.getBaseState().withProperty(POWERED, false).withProperty(ROTATION, 0));
	}

	public void customCollide(World worldIn, BlockPos pos) {
		if (!worldIn.isRemote) {
			IBlockState state = worldIn.getBlockState(pos);
			if (!state.getValue(POWERED).booleanValue())
				updatePoweredState(worldIn, pos, state);
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!worldIn.isRemote && state.getValue(POWERED).booleanValue())
			updatePoweredState(worldIn, pos, state);
	}

	private void updatePoweredState(World worldIn, BlockPos pos, IBlockState state) {
		boolean var4 = state.getValue(POWERED).booleanValue();
		boolean var5 = false;
		List var6 = findTrains(worldIn, pos, EntityTrainBase.class, new Predicate[0]);

		if (!var6.isEmpty())
			var5 = true;

		if (var5 && !var4) {
			worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(true)), 3);
			worldIn.notifyNeighborsOfStateChange(pos, this);
			worldIn.notifyNeighborsOfStateChange(pos.down(), this);
			worldIn.markBlockRangeForRenderUpdate(pos, pos);
		}

		if (!var5 && var4) {
			worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(false)), 3);
			worldIn.notifyNeighborsOfStateChange(pos, this);
			worldIn.notifyNeighborsOfStateChange(pos.down(), this);
			worldIn.markBlockRangeForRenderUpdate(pos, pos);
		}

		if (var5)
			worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}

	protected List findTrains(World worldIn, BlockPos pos, Class c, Predicate... p) {
		AxisAlignedBB var5 = new AxisAlignedBB(pos.getX() + 0.2F, pos.getY(), pos.getZ() + 0.2F, pos.getX() + 1 - 0.2F,
				pos.getY() + 1 - 0.2F, pos.getZ() + 1 - 0.2F);
		return p.length != 1 ? worldIn.getEntitiesWithinAABB(c, var5) : worldIn.getEntitiesWithinAABB(c, var5, p[0]);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
		updatePoweredState(worldIn, pos, state);
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return state.getValue(POWERED) ? 15 : 0;
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(ROTATION, meta % 4).withProperty(POWERED, meta >= 4);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(ROTATION) + (state.getValue(POWERED) ? 4 : 0);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { POWERED, ROTATION });
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 3;
	}
}
