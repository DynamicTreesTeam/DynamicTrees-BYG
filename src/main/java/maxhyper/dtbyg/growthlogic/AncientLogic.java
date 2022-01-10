package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKitConfiguration;
import com.ferreusveritas.dynamictrees.growthlogic.PalmGrowthLogic;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionManipulationContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.DirectionSelectionContext;
import com.ferreusveritas.dynamictrees.growthlogic.context.PositionalSpeciesContext;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AncientLogic extends VariateHeightLogic {

    public static final ConfigurationProperty<Float> CANOPY_ENERGY = ConfigurationProperty.floatProperty("canopy_energy");

    public AncientLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected GrowthLogicKitConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(CANOPY_ENERGY, 8f)
                .with(HEIGHT_VARIATION, 5)
                .with(LOWEST_BRANCH_VARIATION, 3);
    }

    @Override
    protected void registerProperties() {
        this.register(CANOPY_ENERGY, HEIGHT_VARIATION, LOWEST_BRANCH_VARIATION);
    }

    private void branchTwisting(World world, BlockPos pos, GrowSignal signal, int[] probMap){
        boolean allowUp = !(signal.numTurns == 1 && signal.delta.distSqr(0, signal.delta.getY(), 0, true) == 1.0);

        boolean found = false;
        for (Direction dir : Direction.values()){
            if (dir != signal.dir.getOpposite() && (allowUp || dir != Direction.UP)){
                if (TreeHelper.isBranch(world.getBlockState(pos.offset(dir.getNormal())))){
                    probMap[dir.ordinal()] = 1;
                    found = true;
                }
            }
        }
        if (!found){
            if (allowUp) probMap[1] = 1;
            probMap[2] = probMap[3] = probMap[4] = probMap[5] = 1;
        }
    }

    private void darkOakCanopy(GrowSignal signal, int[] probMap) {
        probMap[Direction.UP.get3DDataValue()] = 4;

        //Disallow up/down turns after having turned out of the trunk once.
        if (!signal.isInTrunk()) {
            probMap[Direction.UP.get3DDataValue()] = 0;
            probMap[Direction.DOWN.get3DDataValue()] = 0;
            probMap[signal.dir.ordinal()] *= 0.35;//Promotes the zag of the horizontal branches
        }

        //Amplify cardinal directions to encourage spread
        float spreadPush = 1.5f;
        for (Direction dir : CoordUtils.HORIZONTALS) {
            probMap[dir.ordinal()] *= spreadPush;
        }

        //Ensure that the branch gets out of the trunk at least two blocks so it won't interfere with new side branches at the same level
        if (signal.numTurns == 1 && signal.delta.distSqr(0, signal.delta.getY(), 0, true) == 1.0) {
            for (Direction dir : CoordUtils.HORIZONTALS) {
                if (signal.dir != dir) {
                    probMap[dir.ordinal()] = 0;
                }
            }
        }

        //If the side branches are too swole then give some other branches a chance
        if (signal.isInTrunk()) {
            for (Direction dir : CoordUtils.HORIZONTALS) {
                if (probMap[dir.ordinal()] >= 7) {
                    probMap[dir.ordinal()] = 2;
                }
            }
        }
    }

    @Override
    public int[] populateDirectionProbabilityMap(GrowthLogicKitConfiguration configuration, DirectionManipulationContext context) {
        final GrowSignal signal = context.signal();
        final int[] probMap = context.probMap();
        final World world = context.world();
        final BlockPos pos = context.pos();
        final Species species = context.species();
        Direction originDir = signal.dir.getOpposite();
        int treeHash = CoordUtils.coordHashCode(signal.rootPos, 2);

        probMap[0] = 0; // Down is always disallowed for palm

        if (signal.energy > configuration.get(CANOPY_ENERGY)){ //not canopy
            // Alter probability map for direction change
            probMap[1] = species.getUpProbability();
            // Start by disabling probability on the sides
            probMap[2] = probMap[3] = probMap[4] = probMap[5] =  0;

            if (signal.isInTrunk()) {
                if (signal.delta.getY() <= configuration.getLowestBranchHeight(context) + 1) { //midway
                    int sideHash = treeHash % 16;
                    probMap[2] = sideHash % 2 < 1 ? 1 : 0;
                    probMap[3] = sideHash % 4 < 2 ? 1 : 0;
                    probMap[4] = sideHash % 8 < 4 ? 1 : 0;
                    probMap[5] = sideHash < 8 ? 1 : 0;
                }
            } else {
                branchTwisting(world, pos, signal, probMap);
            }
        } else // it is canopy so dark oak logic is used
            darkOakCanopy(signal, probMap);

        probMap[originDir.ordinal()] = 0; // Disable the direction we came from

        return probMap;
    }

    @Override
    public Direction selectNewDirection(GrowthLogicKitConfiguration configuration, DirectionSelectionContext context) {
        final Direction newDir = super.selectNewDirection(configuration, context);
        final GrowSignal signal = context.signal();
        if (signal.isInTrunk() && newDir != Direction.UP){
            signal.energy = Math.min(signal.energy, configuration.get(CANOPY_ENERGY) + 3.5f);
        }
        return newDir;
    }

    @Override
    public int getLowestBranchHeight(GrowthLogicKitConfiguration configuration, PositionalSpeciesContext context) {
        return super.getLowestBranchHeight(configuration, context) + (int)getHashedVariation(context.world(), context.pos(), configuration.get(LOWEST_BRANCH_VARIATION));
    }

}
