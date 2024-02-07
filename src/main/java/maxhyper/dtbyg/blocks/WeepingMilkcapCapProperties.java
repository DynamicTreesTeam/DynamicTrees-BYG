package maxhyper.dtbyg.blocks;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.systems.poissondisc.Vec2i;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
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
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class WeepingMilkcapCapProperties extends CapProperties {

    public static final TypedRegistry.EntryType<CapProperties> TYPE = TypedRegistry.newType(WeepingMilkcapCapProperties::new);

    public WeepingMilkcapCapProperties(ResourceLocation registryName) {
        super(registryName);
    }

    public static final SimpleVoxmap PATTERN_A = (new SimpleVoxmap(9, 1, 9,
            new byte[]{
                    0, 0, 0, 1, 0, 1, 0, 0, 0,
                    0, 0, 0, 1, 0, 1, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    1, 1, 0, 0, 0, 0, 0, 1, 1,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    1, 1, 0, 0, 0, 0, 0, 1, 1,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 1, 0, 1, 0, 0, 0,
                    0, 0, 0, 1, 0, 1, 0, 0, 0
            })).setCenter(new BlockPos(4, 0, 4));

    public static final SimpleVoxmap PATTERN_B = (new SimpleVoxmap(5, 1, 5,
            new byte[]{
                    0, 0, 1, 0, 0,
                    0, 0, 0, 0, 0,
                    1, 0, 0, 0, 1,
                    0, 0, 0, 0, 0,
                    0, 0, 1, 0, 0,
            })).setCenter(new BlockPos(2, 0, 2));

    static {
        PATTERN_A.setYTouched(0);
        PATTERN_B.setYTouched(0);
    }

    @Override
    protected DynamicCapCenterBlock createDynamicCapCenter(BlockBehaviour.Properties properties) {
        return new DynamicCapCenterBlock(this, properties){

            @Override
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
                for (Vec2i vec : ring) {
                    if (age == 4 || age == 3){
                        if (PATTERN_A.getVoxel(vec.x, 0, vec.z) == 1) continue;
                    }
                    if (age == 2){
                        if (PATTERN_B.getVoxel(vec.x, 0, vec.z) == 1) continue;
                    }
                    BlockPos ringPos = new BlockPos(pos.getX() + vec.x, pos.getY(), pos.getZ() + vec.z);
                    if (canCapReplace(level.getBlockState(ringPos))) {
                        BlockState placeCapState = this.getStateForAge(this.properties, step, new Vec2i(-vec.x, -vec.z), yMoved, negFactor, this.properties.isPartOfCap(level.getBlockState(ringPos.above())));
                        level.setBlock(ringPos, placeCapState, 2);
                        ++placed;
                    } else {
                        ++notPlaced;
                    }
                }

                return placed >= notPlaced;
            }


            @Nonnull
            private BlockState getStateForAge(CapProperties properties, int age, Vec2i centerDirection, boolean yMoved, boolean negativeFactor, boolean topIsCap) {
                boolean[] dirs = new boolean[]{false, !topIsCap, false, false, false, false};
                if (yMoved || age == 1) {

                    for (Direction dir : Direction.Plane.HORIZONTAL) {
                        float dot = (float) (dir.getNormal().getX() * centerDirection.x + dir.getNormal().getZ() * centerDirection.z);
                        if (dot >= 0.0F) {
                            dirs[negativeFactor ? dir.ordinal() : dir.getOpposite().ordinal()] = true;
                        }
                    }
                }

                return properties.getDynamicCapState(age, dirs);
            }

        };
    }
}
