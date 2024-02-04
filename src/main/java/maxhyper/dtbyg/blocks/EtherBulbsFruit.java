package maxhyper.dtbyg.blocks;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.FruitBlock;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class EtherBulbsFruit extends Fruit {

    public static final TypedRegistry.EntryType<Fruit> TYPE = TypedRegistry.newType(EtherBulbsFruit::new);

    public EtherBulbsFruit(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected FruitBlock createBlock(Block.Properties properties) {
        return new FruitBlock(properties, this){

            @Override
            public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
                return state.getValue(fruit.getAgeProperty()) >= 2 ? 15 : 4;
            }

            @OnlyIn(Dist.CLIENT)
            public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
                VoxelShape shape = this.getShape(stateIn, worldIn, pos, CollisionContext.empty());
                Vec3 shapeCenter = shape.bounds().getCenter();
                double centerX = (double)pos.getX() + shapeCenter.x;
                double centerZ = (double)pos.getZ() + shapeCenter.z;

                for(int i = 0; i < 3; ++i)
                    if (rand.nextBoolean())
                        worldIn.addParticle(
                                ParticleTypes.REVERSE_PORTAL,
                                centerX + (double)(rand.nextFloat() / 5.0F), (double)pos.getY() + (0.5D - (double)rand.nextFloat()), centerZ + (double)(rand.nextFloat() / 5.0F),
                                0.0D, 0.0D, 0.0D
                        );
            }

        };
    }
}
