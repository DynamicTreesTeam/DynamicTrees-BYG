package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BaobabLogic extends GrowthLogicKit {

    public BaobabLogic(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public int[] directionManipulation(World world, BlockPos pos, Species species, int radius, GrowSignal signal, int[] probMap) {
        Direction originDir = signal.dir.getOpposite();

        if (!signal.isInTrunk()){
            Direction relativePosToRoot = Direction.fromNormal(signal.delta.getX(), 0, signal.delta.getY());
            if (relativePosToRoot != null){
                if (signal.energy > 2){ //Flaring at end points, higher min energy means more flaring
                    probMap[Direction.DOWN.ordinal()] = 0;
                    for (Direction dir: CoordUtils.HORIZONTALS){
                        probMap[dir.ordinal()] = 0;
                    }
                }
                boolean isBranchUp = world.getBlockState(pos.offset(relativePosToRoot.getNormal())).getBlock() instanceof BranchBlock;
                boolean isBranchSide = world.getBlockState(pos.above()).getBlock() instanceof BranchBlock;
                probMap[Direction.UP.ordinal()] = isBranchUp && !isBranchSide? 0:2;
                probMap[relativePosToRoot.ordinal()] = isBranchSide && !isBranchUp? 0:3;
            }
        }

        probMap[originDir.ordinal()] = 0;

        return probMap;
    }

    private float getHashedVariation (World world, BlockPos pos){
        long day = world.getGameTime() / 24000L;
        int month = (int)day / 30;//Change the hashs every in-game month
        return (CoordUtils.coordHashCode(pos.above(month), 2) % 4);//Vary the height energy by a psuedorandom hash function
    }

    @Override
    public float getEnergy(World world, BlockPos pos, Species species, float signalEnergy) {
        return signalEnergy * species.biomeSuitability(world, pos) + getHashedVariation(world, pos);
    }

    @Override
    public int getLowestBranchHeight(World world, BlockPos pos, Species species, int lowestBranchHeight) {
        return (int)(lowestBranchHeight * species.biomeSuitability(world, pos) + getHashedVariation(world, pos));
    }

}
