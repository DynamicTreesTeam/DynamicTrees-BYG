package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AraucariaLogic extends GrowthLogicKit {

    public AraucariaLogic(ResourceLocation registryName) {
        super(registryName);
    }

    private static final float canopyHeightFraction = 0.8f;
    private static final float splitHeightFraction = 0.6f;

    @Override
    public int[] directionManipulation(World world, BlockPos pos, Species species, int radius, GrowSignal signal, int[] probMap) {
        //Alter probability map for direction change
        float energy = species.getEnergy(world, signal.rootPos);
        float splitCanopyHeight = energy * splitHeightFraction;
        if ((signal.delta.getY() > splitCanopyHeight && signal.isInTrunk()) ||
                signal.delta.getY() > energy * canopyHeightFraction)
            probMap[Direction.UP.ordinal()] = 0;

        if (signal.delta.getY() < splitCanopyHeight){
            //Ensure that the branch doesn't go up after turning out of trunk so it won't interfere with it
            if (signal.numTurns == 1 && signal.delta.distSqr(0, signal.delta.getY(), 0, true) <= 1.0) {
                probMap[Direction.UP.ordinal()] = 0;
            }
            if (signal.isInTrunk()){
                probMap[Direction.UP.ordinal()] *= 10;
            }
        }


        return probMap;
    }

    @Override
    public Direction newDirectionSelected(World world, BlockPos pos, Species species, Direction newDir, GrowSignal signal) {
        float energy = species.getEnergy(world, signal.rootPos);
        // if signal just turned out of trunk under the split height
        if (signal.isInTrunk() && newDir != Direction.UP && signal.delta.getY() < energy * splitHeightFraction) {
            signal.energy = Math.min(2.5f, signal.energy);
        }

        return newDir;
    }

    private float getHashedVariation (World world, BlockPos pos, int heightVariation){
        long day = world.getGameTime() / 24000L;
        int month = (int)day / 30;//Change the hashs every in-game month
        return (CoordUtils.coordHashCode(pos.above(month), 2) % heightVariation);//Vary the height energy by a psuedorandom hash function
    }

    @Override
    public float getEnergy(World world, BlockPos pos, Species species, float signalEnergy) {
        return signalEnergy + getHashedVariation(world, pos, 13);
    }

    @Override
    public int getLowestBranchHeight(World world, BlockPos pos, Species species, int lowestBranchHeight) {
        return (int) (lowestBranchHeight + getHashedVariation(world, pos, 7));
    }
}
