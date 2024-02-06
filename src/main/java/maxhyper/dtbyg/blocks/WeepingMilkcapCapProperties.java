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

public class WeepingMilkcapCapProperties extends CapProperties {

    public static final TypedRegistry.EntryType<CapProperties> TYPE = TypedRegistry.newType(WeepingMilkcapCapProperties::new);

    public WeepingMilkcapCapProperties(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected DynamicCapCenterBlock createDynamicCapCenter(BlockBehaviour.Properties properties) {
        return new DynamicCapCenterBlock(this, properties){

        };
    }
}
