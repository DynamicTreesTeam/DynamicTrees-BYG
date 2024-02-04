package maxhyper.dtbyg.genfeatures;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.api.network.NodeInspector;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeature.VinesGenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGrowContext;
import com.ferreusveritas.dynamictrees.systems.nodemapper.FindEndsNode;
import com.ferreusveritas.dynamictreesplus.block.mushroom.DynamicCapBlock;
import com.ferreusveritas.dynamictreesplus.block.mushroom.DynamicCapCenterBlock;
import com.ferreusveritas.dynamictreesplus.systems.mushroomlogic.context.MushroomCapContext;
import com.ferreusveritas.dynamictreesplus.tree.HugeMushroomSpecies;
import maxhyper.dtbyg.init.DTBYGRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class MushroomVinesGenFeature extends GenFeature {

	// Min radius for the flare.
	public static final ConfigurationProperty<Float> VINE_PLACE_CHANCE = ConfigurationProperty.floatProperty("vine_place_chance");
	public static final ConfigurationProperty<Integer> MAX_LENGTH = ConfigurationProperty.integer("max_length");
	public static final ConfigurationProperty<Block> BLOCK = ConfigurationProperty.block("block");
	public static final ConfigurationProperty<Block> TIP_BLOCK = ConfigurationProperty.block("tip_block");

	public MushroomVinesGenFeature(ResourceLocation registryName) {
		super(registryName);
	}

	@Override
	protected void registerProperties() {
		this.register(VINE_PLACE_CHANCE, MAX_LENGTH, BLOCK, TIP_BLOCK);
	}

	@Override
	public GenFeatureConfiguration createDefaultConfiguration() {
		return super.createDefaultConfiguration()
				.with(VINE_PLACE_CHANCE, 0.05f)
				.with(MAX_LENGTH, 8)
				.with(BLOCK, Blocks.WEEPING_VINES_PLANT)
				.with(TIP_BLOCK, Blocks.WEEPING_VINES);
	}

	@Override
	protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
		if (context.species() instanceof HugeMushroomSpecies mushroomSpecies){
			LevelAccessor level = context.level();
			BlockPos rootPos = context.pos();
			FindEndsNode endFinder = new FindEndsNode();
			TreeHelper.startAnalysisFromRoot(level, rootPos, new MapSignal(endFinder));
			List<BlockPos> endPoints = endFinder.getEnds();
			for (BlockPos endPoint : endPoints){
				if (endPoint.equals(BlockPos.ZERO)) continue;
				if (level.getBlockState(endPoint.above()).getBlock() instanceof DynamicCapCenterBlock){
					int age = level.getBlockState(endPoint.above()).getValue(DynamicCapCenterBlock.AGE);
					for (final BlockPos findPos : mushroomSpecies.getMushroomShapeKit().getShapeCluster(new MushroomCapContext(level, endPoint.above(), mushroomSpecies, age))) {
						if (level.getRandom().nextFloat() < configuration.get(VINE_PLACE_CHANCE) && level.getBlockState(findPos.below()).isAir()){
							BlockPos vinePos = findPos.below();
							this.placeVines(
									level, vinePos, configuration.get(BLOCK).defaultBlockState(), configuration.get(MAX_LENGTH),
									configuration.getAsOptional(TIP_BLOCK).map((block) -> block.defaultBlockState().setValue(GrowingPlantHeadBlock.AGE, context.isWorldGen() ? 25 : 0)).orElse(null),
									context.isWorldGen());
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	protected void placeVines(LevelAccessor level, BlockPos vinePos, BlockState vinesState, int maxLength, @Nullable BlockState tipState, boolean worldGen) {
		int len = worldGen ? Mth.clamp(level.getRandom().nextInt(maxLength) + 3, 3, maxLength) : 1;
		BlockPos.MutableBlockPos mPos = new BlockPos.MutableBlockPos(vinePos.getX(), vinePos.getY(), vinePos.getZ());
		tipState = tipState == null ? vinesState : tipState;

		for(int i = 0; i < len; ++i) {
			if (!level.isEmptyBlock(mPos)) {
				if (i > 0) {
					mPos.setY(mPos.getY() + 1);
					level.setBlock(mPos, tipState, 3);
				}
				break;
			}

			level.setBlock(mPos, i == len - 1 ? tipState : vinesState, 3);
			mPos.setY(mPos.getY() + -1);
		}

	}

}
