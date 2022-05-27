package MTR.items;

import java.util.List;

import MTR.ItemBase;
import MTR.MTRBlocks;
import MTR.blocks.BlockAPGDoorClosed;
import MTR.blocks.BlockAPGGlassBottom;
import MTR.blocks.BlockAPGGlassTop;
import MTR.blocks.BlockPSD;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemAPG extends ItemBase {

	public static final String[] name = { "ItemAPG", "ItemAPGDoor" };

	public ItemAPG() {
		super(name);
		setHasSubtypes(true);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		if (stack.getMetadata() == 0)
			list.add(I18n.format("gui.glass", new Object[0]));
		else
			list.add(I18n.format("gui.door", new Object[0]));
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState var9 = worldIn.getBlockState(pos);
		Block var10 = var9.getBlock();
		if (!var10.isReplaceable(worldIn, pos))
			pos = pos.offset(facing);
		if (worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)
				&& worldIn.getBlockState(pos.up()).getBlock().isReplaceable(worldIn, pos.up())) {
			EnumFacing var3 = EnumFacing.fromAngle(playerIn.rotationYaw).rotateY();
			BlockPos pos2 = pos;
			switch (var3) {
			case NORTH:
				pos2 = pos.add(0, 0, 1);
				break;
			case SOUTH:
				pos2 = pos.add(0, 0, -1);
				break;
			case EAST:
				pos2 = pos.add(-1, 0, 0);
				break;
			case WEST:
				pos2 = pos.add(1, 0, 0);
				break;
			default:
			}
			IBlockState blockState = worldIn.getBlockState(pos2);
			boolean side2 = false;
			if (blockState.getBlock() instanceof BlockAPGDoorClosed)
				side2 = blockState.getValue(BlockPSD.SIDE) == false;
			if (blockState.getBlock() instanceof BlockAPGGlassBottom)
				side2 = blockState.getValue(BlockAPGGlassBottom.SIDE) == false;
			if (stack.getMetadata() == 1) {
				worldIn.setBlockState(pos,
						MTRBlocks.blockapgdoorclosed.getDefaultState().withProperty(BlockPSD.FACING, var3)
								.withProperty(BlockPSD.SIDE, side2).withProperty(BlockPSD.TOP, false));
				worldIn.setBlockState(pos.up(),
						MTRBlocks.blockapgdoorclosed.getDefaultState().withProperty(BlockPSD.FACING, var3)
								.withProperty(BlockPSD.SIDE, side2).withProperty(BlockPSD.TOP, true));
			} else if (playerIn.isSneaking()) {
				worldIn.setBlockState(pos,
						MTRBlocks.blockapgglassmiddle.getDefaultState().withProperty(BlockPSD.FACING, var3)
								.withProperty(BlockPSD.SIDE, true).withProperty(BlockPSD.TOP, false));
				worldIn.setBlockState(pos.up(),
						MTRBlocks.blockapgglassmiddle.getDefaultState().withProperty(BlockPSD.FACING, var3)
								.withProperty(BlockPSD.SIDE, true).withProperty(BlockPSD.TOP, true));
			} else {
				worldIn.setBlockState(pos.up(),
						MTRBlocks.blockapgglasstop.getDefaultState().withProperty(BlockAPGGlassTop.FACING, var3));
				worldIn.setBlockState(pos, MTRBlocks.blockapgglassbottom.getDefaultState()
						.withProperty(BlockAPGGlassBottom.FACING, var3).withProperty(BlockAPGGlassBottom.SIDE, side2));
			}
			--stack.stackSize;
			return EnumActionResult.SUCCESS;
		} else
			return EnumActionResult.FAIL;
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		for (int var4 = 0; var4 < 2; ++var4)
			subItems.add(new ItemStack(itemIn, 1, var4));
	}
}
