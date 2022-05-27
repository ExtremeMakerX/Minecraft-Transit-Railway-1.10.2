package MTR.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStationNameN extends BlockStationNameBase {

	private static final String name = "BlockStationNameFOT";

	public BlockStationNameN() {
		super(name);
	}

	@Override
	public int damageDropped(IBlockState arg0) {
		return 13;
	}

	public static String getName() {
		return name;
	}
}