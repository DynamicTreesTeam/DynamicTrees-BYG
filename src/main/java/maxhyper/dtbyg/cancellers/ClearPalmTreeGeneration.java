package maxhyper.dtbyg.cancellers;

import corgiaoc.byg.core.world.BYGConfiguredFeatures;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ClearPalmTreeGeneration {

    static void makeNotFinalAndModify(Field field, Object newValue) throws NoSuchFieldException, IllegalAccessException{
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

    public static void replacePalmFeature(ConfiguredFeature<?, ?> newFeature){
        try {
            makeNotFinalAndModify(BYGConfiguredFeatures.class.getDeclaredField("RANDOM_PALM_TREE"), newFeature);
        } catch (Exception e){
            System.out.println("Failed to reflect field RANDOM_PALM_TREE: "+ e.getLocalizedMessage());
        }
    }

}
