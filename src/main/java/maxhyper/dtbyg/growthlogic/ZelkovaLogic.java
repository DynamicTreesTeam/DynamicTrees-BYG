package maxhyper.dtbyg.growthlogic;

import com.ferreusveritas.dynamictrees.growthlogic.ConiferLogic;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ZelkovaLogic extends ConiferLogic {

    private int lowestBranchVariation = 7;

    public ZelkovaLogic(ResourceLocation registryName) {
        super(registryName, 10);
    }

    public ConiferLogic setLowestBranchVariation(int lowestBranchVariation) {
        this.lowestBranchVariation = lowestBranchVariation;
        return this;
    }

    @Override
    public int getLowestBranchHeight(World world, BlockPos pos, Species species, int lowestBranchHeight) {
        long day = world.getGameTime() / 24000L;
        int month = (int) day / 30;//Change the hashs every in-game month

        return (int)(lowestBranchHeight * species.biomeSuitability(world, pos) + (CoordUtils.coordHashCode(pos.above(month), 2) % lowestBranchVariation));//Vary the height energy by a psuedorandom hash function
    }

}
