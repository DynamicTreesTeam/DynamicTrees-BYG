package maxhyper.dtbyg.blocks;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.systems.poissondisc.Vec2i;
import com.ferreusveritas.dynamictreesplus.block.mushroom.CapProperties;
import com.ferreusveritas.dynamictreesplus.block.mushroom.DynamicCapBlock;
import com.ferreusveritas.dynamictreesplus.block.mushroom.DynamicCapCenterBlock;
import com.ferreusveritas.dynamictreesplus.systems.mushroomlogic.MushroomCapDisc;
import maxhyper.dtbyg.init.DTBYGRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import potionstudios.byg.common.block.end.impariusgrove.TreeBranchBlock;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

public class ImpariusMushroomCapProperties extends CapProperties {

    public static final TypedRegistry.EntryType<CapProperties> TYPE = TypedRegistry.newType(ImpariusMushroomCapProperties::new);

    public ImpariusMushroomCapProperties(ResourceLocation registryName) {
        super(registryName);
    }

    protected float capSideBranchesChance = 0.5f;

    @Override
    public BlockBehaviour.Properties getDefaultBlockProperties(Material material, MaterialColor materialColor) {
        return BlockBehaviour.Properties.of(Material.GRASS, MaterialColor.COLOR_CYAN).strength(0.2F).sound(SoundType.TWISTING_VINES).speedFactor(0.5F).jumpFactor(0.5F);
    }

    @Override
    protected DynamicCapCenterBlock createDynamicCapCenter(BlockBehaviour.Properties properties) {
        return new DynamicCapCenterBlock(this, properties){

            public void clearRing(LevelAccessor level, BlockPos pos, int radius) {
                List<Vec2i> ring = MushroomCapDisc.getPrecomputedRing(radius);
                for (Vec2i vec : ring) {
                    BlockPos ringPos = new BlockPos(pos.getX() + vec.x, pos.getY(), pos.getZ() + vec.z);
                    if (this.properties.isPartOfCap(level.getBlockState(ringPos))) {
                        if (level.getBlockState(ringPos.above()).is(DTBYGRegistries.IMPARIUS_MUSHROOM_BRANCH.get())){
                            level.setBlock(ringPos.below(), Blocks.AIR.defaultBlockState(), 2);
                        }
                        level.setBlock(ringPos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }

            }

            public boolean placeRing(LevelAccessor level, BlockPos pos, int radius, int step, boolean yMoved, boolean negFactor) {
                List<Vec2i> ring = MushroomCapDisc.getPrecomputedRing(radius);
                int placed = 0;
                int notPlaced = 0;

                int age = 0;
                for (int i=0;i<8;i++){
                    BlockPos searchPos = pos.above((negFactor?-1:1)*i);
                    BlockState searchState = level.getBlockState(searchPos);
                    if (searchState.hasProperty(AGE)){
                        age = searchState.getValue(AGE);
                        break;
                    }
                }
                boolean lastRing = step == age;
                for (Vec2i vec : ring) {
                    BlockPos ringPos = new BlockPos(pos.getX() + vec.x, pos.getY(), pos.getZ() + vec.z);
                    if (canCapReplace(level.getBlockState(ringPos))) {
                        BlockState placeCapState = this.getStateForAge(this.properties, step, new Vec2i(-vec.x, -vec.z), yMoved, negFactor, this.properties.isPartOfCap(level.getBlockState(ringPos.above())), lastRing);
                        level.setBlock(ringPos, placeCapState, 2);
                        //place branches
                        if (level.getRandom().nextFloat() < capSideBranchesChance && level.getBlockState(ringPos.above()).isAir()){
                            List<Direction> validDirs = new LinkedList<>();
                            for (Direction dir : Direction.Plane.HORIZONTAL){
                                if (level.getBlockState(ringPos.above().offset(dir.getNormal())).getBlock() instanceof DynamicCapBlock)
                                    validDirs.add(dir);
                            }
                            if (!validDirs.isEmpty()){
                                Direction facing = validDirs.get(level.getRandom().nextInt(validDirs.size()));
                                level.setBlock(ringPos.above(), DTBYGRegistries.IMPARIUS_MUSHROOM_BRANCH.get().defaultBlockState().setValue(TreeBranchBlock.FACING, facing.getOpposite()), 2);
                            }
                        }
                        ++placed;
                    } else {
                        ++notPlaced;
                    }
                }

                return placed >= notPlaced;
            }

            @Nonnull
            private BlockState getStateForAge(CapProperties properties, int age, Vec2i centerDirection, boolean yMoved, boolean negativeFactor, boolean topIsCap, boolean lastRing) {
                boolean[] dirs = new boolean[]{lastRing, !topIsCap, true, true, true, true};
                if (yMoved || age == 1) {

                    for (Direction dir : Direction.Plane.HORIZONTAL) {
                        float dot = (float) (dir.getNormal().getX() * centerDirection.x + dir.getNormal().getZ() * centerDirection.z);
                        if (dot >= 0.0F) {
                            dirs[negativeFactor ? dir.getOpposite().ordinal() : dir.ordinal()] = false;
                        }
                    }
                }

                return properties.getDynamicCapState(age, dirs);
            }

        };
    }
}
