package maxhyper.dtbyg.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.config.ConfiguredGenFeature;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import corgiaoc.byg.common.world.feature.config.BYGMushroomConfig;
import corgiaoc.byg.common.world.feature.overworld.mushrooms.util.BYGAbstractMushroomFeature;
import corgiaoc.byg.core.world.BYGConfiguredFeatures;
import corgiaoc.byg.core.world.BYGFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.List;
import java.util.Random;

public class BYGHugeMushroomGenFeature extends GenFeature implements IPostGenFeature {

    public static final ConfigurationProperty<Integer> MAX_ATTEMPTS = ConfigurationProperty.integer("max_attempts");
    public static final ConfigurationProperty<Float> CHANCE_FOR_MINI = ConfigurationProperty.floatProperty("chance_for_mini");

    private static final WeightedList<ConfiguredFeature<?, ?>> mushrooms = new WeightedList<>();
    private static final WeightedList<ConfiguredFeature<?, ?>> smallMushrooms = new WeightedList<>();


    public BYGHugeMushroomGenFeature(ResourceLocation registryName) {
        super(registryName);
        setMushrooms();
    }

    protected void setMushrooms (){
        mushrooms
                .add(BYGConfiguredFeatures.GREEN_MUSHROOM_HUGE, 1)
                .add(BYGConfiguredFeatures.WOOD_BLEWIT_HUGE, 1)
                .add(BYGConfiguredFeatures.WEEPING_MILKCAP_HUGE, 1)
                .add(BYGConfiguredFeatures.BLACK_PUFF_HUGE, 1);
        smallMushrooms
                .add(BYGConfiguredFeatures.GREEN_MUSHROOM_MINI, 1)
                .add(BYGConfiguredFeatures.WOOD_BLEWIT_MINI, 1)
                .add(BYGConfiguredFeatures.WEEPING_MILKCAP_MINI, 1)
                .add(BYGConfiguredFeatures.BLACK_PUFF_MINI, 1);
    }

    @Override
    protected void registerProperties() {
        this.register(PLACE_CHANCE, MAX_COUNT, MAX_ATTEMPTS, CHANCE_FOR_MINI);
    }

    @Override
    public ConfiguredGenFeature<GenFeature> createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(PLACE_CHANCE, 0.4f)
                .with(MAX_COUNT, 1)
                .with(MAX_ATTEMPTS, 3)
                .with(CHANCE_FOR_MINI, 0.2f);
    }

    private static final int height = 5;
    private BlockPos findFloor (Species species, IWorld world, BlockPos startPos){
        for (int i=-height;i<height;i++){
            BlockPos thisPos = startPos.above(i);
            BlockPos upPos = thisPos.above();
            if (species.isAcceptableSoil(world.getBlockState(thisPos)) && world.getBlockState(upPos).getMaterial().isReplaceable()){
                return thisPos;
            }
        }
        return BlockPos.ZERO;
    }

    @Override
    public boolean postGeneration(ConfiguredGenFeature<?> configuredGenFeature, IWorld world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, BlockState initialDirtState, Float seasonValue, Float seasonFruitProductionFactor) {
        Random rand = world.getRandom();
        int count = configuredGenFeature.get(MAX_COUNT);
        int magnitude = (int)(radius * 0.75);
        if (magnitude <= 2) return false;
        for (int i=0; i<configuredGenFeature.get(MAX_ATTEMPTS); i++){
            if (rand.nextFloat() < configuredGenFeature.get(PLACE_CHANCE)) {
                double angle = rand.nextFloat() * (2*Math.PI);
                Vector2f offsetVec = new Vector2f((float)(magnitude * Math.cos(angle)), (float)(magnitude * Math.sin(angle)));
                Vector3i offsetVecInt = new Vector3i(Math.ceil(offsetVec.x), 0, Math.ceil(offsetVec.y));
                BlockPos shroomPos = findFloor(species, world, rootPos.offset(offsetVecInt));
                if (shroomPos != BlockPos.ZERO){
                    ConfiguredFeature<?, ?> shroom;
                    if (rand.nextFloat() < configuredGenFeature.get(CHANCE_FOR_MINI)){
                        shroom = smallMushrooms.getOne(rand);
                    } else {
                        shroom = mushrooms.getOne(rand);
                    }
                    if (world instanceof ISeedReader){
                        shroom.place((ISeedReader) world, null, rand, shroomPos);
                        count--;
                        if (count == 0)
                            return true;
                    }
                }
            }
        }
        return count != configuredGenFeature.get(MAX_COUNT);
    }

}
