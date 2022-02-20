package maxhyper.dtbyg.genfeatures;

import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.SoilHelper;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.trees.Species;
import corgiaoc.byg.core.world.BYGConfiguredFeatures;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.Random;
import java.util.function.BiFunction;

/**
 * @author Max Hyper
 */
public class BYGHugeMushroomGenFeature extends GenFeature {

    public static final ConfigurationProperty<Integer> MAX_ATTEMPTS = ConfigurationProperty.integer("max_attempts");
    public static final ConfigurationProperty<Float> CHANCE_FOR_MINI = ConfigurationProperty.floatProperty("chance_for_mini");

    private static final String[] fungusSoils = new String[]{"dirt_like","fungus_like"};

    private static final WeightedList<ConfiguredFeature<?, ?>> mushrooms = new WeightedList<>();
    private static final WeightedList<ConfiguredFeature<?, ?>> smallMushrooms = new WeightedList<>();
    private static final WeightedList<ConfiguredFeature<?, ?>> glowshrooms = new WeightedList<>();
    private static final WeightedList<ConfiguredFeature<?, ?>> flowers = new WeightedList<>();

    private enum MushroomType {
        MUSHROOM (1, (r,cf)-> r.nextFloat() < cf.get(CHANCE_FOR_MINI) ?
                smallMushrooms.getOne(r) : mushrooms.getOne(r)),
        GLOWSHROOM (1, (r,cf)-> r.nextBoolean() ? glowshrooms.getOne(r) : MUSHROOM.getFeature(r,cf)),
        FLOWER (0.05, (r,cf)-> flowers.getOne(r));
        double chanceMult;
        BiFunction<Random, GenFeatureConfiguration, ConfiguredFeature<?, ?>> feature;
        MushroomType (double chanceMult, BiFunction<Random, GenFeatureConfiguration, ConfiguredFeature<?, ?>> feature){
            this.chanceMult = chanceMult;
            this.feature = feature;
        }
        public double getChanceMult() {
            return chanceMult;
        }
        public ConfiguredFeature<?, ?> getFeature(Random random, GenFeatureConfiguration configuration) {
            return feature.apply(random, configuration);
        }
    }

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
        glowshrooms
                .add(BYGConfiguredFeatures.PURPLE_GLOWSHROOM_HUGE, 9)
                .add(BYGConfiguredFeatures.BLUE_GLOWSHROOM_HUGE, 1);
        flowers
                .add(BYGConfiguredFeatures.GIANT_ANGELICA_FLOWER, 2)
                .add(BYGConfiguredFeatures.GIANT_DANDELION_FLOWER, 2)
                .add(BYGConfiguredFeatures.GIANT_IRIS_FLOWER, 5)
                .add(BYGConfiguredFeatures.GIANT_ROSE_FLOWER, 1);
    }

    @Override
    protected void registerProperties() {
        this.register(PLACE_CHANCE, MAX_COUNT, MAX_ATTEMPTS, CHANCE_FOR_MINI);
    }

    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {
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
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        IWorld world = context.world();
        BlockPos rootPos = context.pos();
        Species species = context.species();
        if (!(world instanceof ISeedReader)) return false;
        Random rand = world.getRandom();
        MushroomType mushroomType = getType(world, rootPos, species, context.biome());
        int count = configuration.get(MAX_COUNT);
        int magnitude = (int)(context.radius() * 0.75);
        if (magnitude <= 2) return false;
        magnitude++;
        for (int i=0; i<configuration.get(MAX_ATTEMPTS); i++){
            if (rand.nextFloat() < configuration.get(PLACE_CHANCE)* mushroomType.getChanceMult()) {
                double angle = rand.nextFloat() * (2*Math.PI);
                Vector2f offsetVec = new Vector2f((float)(magnitude * Math.cos(angle)), (float)(magnitude * Math.sin(angle)));
                Vector3i offsetVecInt = new Vector3i(Math.ceil(offsetVec.x), 0, Math.ceil(offsetVec.y));
                BlockPos shroomPos = findFloor(species, world, rootPos.offset(offsetVecInt));
                if (shroomPos != BlockPos.ZERO && SoilHelper.isSoilAcceptable(world.getBlockState(shroomPos), SoilHelper.getSoilFlags(fungusSoils))){
                    ConfiguredFeature<?, ?> feature = mushroomType.getFeature(rand, configuration);
                    feature.place((ISeedReader) world, null, rand, shroomPos);
                    count--;
                    if (count == 0)
                        return true;
                }
            }
        }
        return count != configuration.get(MAX_COUNT);
    }

    protected MushroomType getType (IWorld world, BlockPos rootPos, Species species, Biome biome){
        MushroomType type;
        String biomeName = biome.getRegistryName().toString();
        if (biomeName.matches(".*glow.*"))
            type = MushroomType.GLOWSHROOM;
        else if (biomeName.matches(".*flower.*"))
            type = MushroomType.FLOWER;
        else type = MushroomType.MUSHROOM;
        return type;
    }

}
