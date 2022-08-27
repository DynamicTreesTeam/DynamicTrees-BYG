package maxhyper.dtbyg.fruits;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import maxhyper.dtbyg.blocks.EtherBulbsFruitBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.ResourceLocation;

public class EtherBulbsFruit extends Fruit {

    public static final TypedRegistry.EntryType<Fruit> TYPE = TypedRegistry.newType(EtherBulbsFruit::new);

    public EtherBulbsFruit(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected FruitBlock createBlock(AbstractBlock.Properties properties) {
        return new EtherBulbsFruitBlock(properties, this);
    }
}
