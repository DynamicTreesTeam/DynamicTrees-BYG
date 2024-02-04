package maxhyper.dtbyg.blocks;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.systems.poissondisc.Vec2i;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictreesplus.block.mushroom.CapProperties;
import com.ferreusveritas.dynamictreesplus.block.mushroom.DynamicCapBlock;
import com.ferreusveritas.dynamictreesplus.block.mushroom.DynamicCapCenterBlock;
import com.ferreusveritas.dynamictreesplus.systems.mushroomlogic.MushroomCapDisc;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import potionstudios.byg.common.block.BYGBlocks;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class WartyCapProperties extends CapProperties {

    public static final TypedRegistry.EntryType<CapProperties> TYPE = TypedRegistry.newType(WartyCapProperties::new);

    public WartyCapProperties(ResourceLocation registryName) {
        super(registryName);
    }

    protected Block shroomlightBlock = Blocks.SHROOMLIGHT;

    protected float extraWartChance = 0.5f;
    protected float shroomlightUpChance = 0.02f;
    protected float shroomlightDownChance = 0.1f;

    protected String getBlockRegistryNameSuffix() {
        return "_wart";
    }

    protected String getCenterBlockRegistryNameSuffix() {
        return "_wart_center";
    }

    public void setShroomlightBlock(Block shroomlightBlock) {
        this.shroomlightBlock = shroomlightBlock;
    }

    public Block getShroomlightBlock() {
        return shroomlightBlock;
    }

    @Override
    public BlockBehaviour.Properties getDefaultBlockProperties(Material material, MaterialColor materialColor) {
        return BlockBehaviour.Properties.of(Material.GRASS).sound(SoundType.WART_BLOCK).strength(1.0F);
    }

    @Override
    protected DynamicCapBlock createDynamicCap(BlockBehaviour.Properties properties) {
        return new DynamicCapBlock(this, properties){
            public void tick(BlockState pState, ServerLevel level, BlockPos pos, Random pRandom) {
                if (level.getBlockState(pos).getBlock() == this) {
                    int dist = pState.getValue(DISTANCE);
                    boolean supportFound = false;

                    for (BlockPos offPos : BlockPos.withinManhattan(pos, 1, 1, 1)) {
                        if (offPos != pos) {
                            BlockState offsetState = level.getBlockState(offPos);
                            if (offsetState.hasProperty(DISTANCE) && offsetState.getValue(DISTANCE) == dist - 1 || dist == 1 && offsetState.getBlock() == this.properties.getDynamicCapCenterBlock().orElse(null)) {
                                supportFound = true;
                                break;
                            }
                        }
                    }

                    if (!supportFound) {
                        level.destroyBlock(pos, true);
                        BlockState upState = level.getBlockState(pos.above());
                        BlockState downState = level.getBlockState(pos.below());
                        if (upState.is(shroomlightBlock))
                            level.destroyBlock(pos.above(), true);
                        if (downState.is(shroomlightBlock))
                            level.destroyBlock(pos.below(), true);
                        this.updateNeighborsSurround(level, pos, DynamicCapBlock.class);
                    } else {
                        super.tick(pState, level, pos, pRandom);
                    }
                }
            }
        };
    }

    @Override
    protected DynamicCapCenterBlock createDynamicCapCenter(BlockBehaviour.Properties properties) {
        return new DynamicCapCenterBlock(this, properties){

            @Override
            public void clearRing(LevelAccessor level, BlockPos pos, int radius) {
                List<Vec2i> ring = MushroomCapDisc.getPrecomputedRing(radius);
                for (Vec2i vec : ring) {
                    BlockPos ringPos = new BlockPos(pos.getX() + vec.x, pos.getY(), pos.getZ() + vec.z);
                    if (this.properties.isPartOfCap(level.getBlockState(ringPos))) {
                        BlockState upState = level.getBlockState(ringPos.above());
                        BlockState downState = level.getBlockState(ringPos.below());
                        if (this.properties.isPartOfCap(upState) || upState.is(shroomlightBlock))
                            level.setBlock(ringPos.above(), Blocks.AIR.defaultBlockState(), 2);
                        if (this.properties.isPartOfCap(downState) || downState.is(shroomlightBlock))
                            level.setBlock(ringPos.below(), Blocks.AIR.defaultBlockState(), 2);
                        level.setBlock(ringPos, Blocks.AIR.defaultBlockState(), 2);
                    }
                }

            }

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
                boolean lastRing = step == age;
                for (Vec2i vec : ring) {
                    BlockPos ringPos = new BlockPos(pos.getX() + vec.x, pos.getY(), pos.getZ() + vec.z);
                    if (canCapReplace(level.getBlockState(ringPos))) {
                        BlockState placeCapState = this.getStateForAge(this.properties, step, new Vec2i(-vec.x, -vec.z), yMoved, negFactor, this.properties.isPartOfCap(level.getBlockState(ringPos.above())), lastRing);
                        level.setBlock(ringPos, placeCapState, 2);
                        //place wart above
                        if (step <= 8 && step > 1){
                            placeExtraWart(level, ringPos.above(), placeCapState, true, shroomlightUpChance);
                            placeExtraWart(level, ringPos.below(), placeCapState, false, shroomlightDownChance);
                        }
                        ++placed;
                    } else {
                        ++notPlaced;
                    }
                }

                return placed >= notPlaced;
            }

            private void placeExtraWart (LevelAccessor level, BlockPos ringPos, BlockState placeCapState, boolean checkAround, float shroomlightChance){
                if (level.getRandom().nextFloat() < extraWartChance && level.getBlockState(ringPos).isAir()){
                    boolean canPlace = !checkAround;
                    if (checkAround){
                        for (Direction dir : Direction.Plane.HORIZONTAL){
                            if (properties.isPartOfCap(level.getBlockState(ringPos.offset(dir.getNormal())))){
                                canPlace = true;
                                break;
                            }
                        }
                    }
                    if (canPlace){
                        BlockState place = placeCapState;
                        if (level.getRandom().nextFloat() < shroomlightChance)
                            place = shroomlightBlock.defaultBlockState();
                        level.setBlock(ringPos, place, 2);
                    }
                }
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
