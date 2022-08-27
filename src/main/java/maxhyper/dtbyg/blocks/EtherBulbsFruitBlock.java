package maxhyper.dtbyg.blocks;

import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class EtherBulbsFruitBlock extends FruitBlock {

    public EtherBulbsFruitBlock(Properties properties, Fruit fruit) {
        super(properties, fruit);
    }

    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return getAge(state) >= 2 ? 15 : 4;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        VoxelShape shape = this.getShape(state, world, pos, ISelectionContext.empty());
        Vector3d shapeCenter = shape.bounds().getCenter();
        double centerX = (double) pos.getX() + shapeCenter.x;
        double centerZ = (double) pos.getZ() + shapeCenter.z;
        for (int i = 0; i < 3; ++i) {
            if (random.nextBoolean()) {
                world.addParticle(ParticleTypes.REVERSE_PORTAL,
                        centerX + (double) (random.nextFloat() / 5.0F),
                        (double) pos.getY() + (0.5D - (double) random.nextFloat()),
                        centerZ + (double) (random.nextFloat() / 5.0F),
                        0.0D, 0.0D, 0.0D);
            }
        }
    }

}
