package maxhyper.dtbyg.mushroomshape;

import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictreesplus.block.mushroom.DynamicCapCenterBlock;
import com.ferreusveritas.dynamictreesplus.systems.mushroomlogic.MushroomShapeConfiguration;
import com.ferreusveritas.dynamictreesplus.systems.mushroomlogic.context.MushroomCapContext;
import com.ferreusveritas.dynamictreesplus.systems.mushroomlogic.shapekits.MushroomShapeKit;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

public class LinearShape extends MushroomShapeKit {
    public static final ConfigurationProperty<Integer> POINTED_TIP_AGE =
            ConfigurationProperty.integer("pointed_tip_max_age");
    /**
     * A factor of 1 is equivalent to a 45-degree angle.
     * Any factor lower than 1 will create a tighter cap (ex. 0.5 is 22.5°)
     * Factors higher than 1 will create a more open cap (ex. 2 is 67.5°)
     */
    public static final ConfigurationProperty<Float> FACTOR =
            ConfigurationProperty.floatProperty("factor");

    public LinearShape(ResourceLocation registryName) {
        super(registryName);
    }

    @Override @NotNull
    public MushroomShapeConfiguration getDefaultConfiguration() {
        return this.defaultConfiguration
                .with(CHANCE_TO_AGE, 0.75f)
                .with(MAX_CAP_AGE, 6)
                .with(POINTED_TIP_AGE, 0)
                .with(FACTOR, 1f);
    }

    @Override
    protected void registerProperties() {
        this.register(CHANCE_TO_AGE, MAX_CAP_AGE, POINTED_TIP_AGE, FACTOR);
    }

    @Override
    public void generateMushroomCap(MushroomShapeConfiguration configuration, MushroomCapContext context) {
        placeRing(configuration, context, Math.min(context.age(), configuration.get(MAX_CAP_AGE)), ringAction.PLACE);
    }

    @Override
    public void clearMushroomCap (MushroomShapeConfiguration configuration, MushroomCapContext context){
        placeRing(configuration, context, context.age(), ringAction.CLEAR);
    }

    @Override
    public List<BlockPos> getShapeCluster(MushroomShapeConfiguration configuration, MushroomCapContext context){
        return placeRing(configuration, context, context.age(), ringAction.GET);
    }

    @Override
    public int getMaxCapAge(MushroomShapeConfiguration configuration) {
        return configuration.get(MAX_CAP_AGE);
    }

    @Override
    public float getChanceToAge(MushroomShapeConfiguration configuration) {
        return configuration.get(CHANCE_TO_AGE);
    }


    enum ringAction {
        PLACE,
        CLEAR,
        GET
    }
    @Nonnull
    private List<BlockPos> placeRing (MushroomShapeConfiguration configuration, MushroomCapContext context, int age, ringAction action){
        DynamicCapCenterBlock centerBlock = context.species().getCapProperties().getDynamicCapCenterBlock().orElse(null);
        List<BlockPos> ringPositions = new LinkedList<>();
        if (centerBlock == null) return ringPositions;
        float factor = configuration.get(FACTOR);
        int y = 0;
        int radius = 1;
        for (int i=1; i<=age; i++){

            boolean tip = i == 1 && age > configuration.get(POINTED_TIP_AGE);
            boolean moveY = !tip && (factor < 1 || i % factor == 0);
            if (moveY) y+= 1;

            BlockPos pos = context.pos().below(y);
            if (action == ringAction.CLEAR)
                centerBlock.clearRing(context.level(), pos, radius);
            else if (action == ringAction.PLACE){
                // if the ring failed to generate then don't bother with the next rings
                if (!centerBlock.placeRing(context.level(), pos, radius, i, i>1 || moveY, factor < 0.0F)) break;
            }
            else if (action == ringAction.GET)
                ringPositions.addAll(centerBlock.getRing(context.level(), pos, radius));

            if ((factor > 1 || i % (1/factor) == 0)) radius++;
        }
        ringPositions.add(context.pos());
        return ringPositions;
    }
}
