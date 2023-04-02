package maxhyper.dtbyg.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.Optional;

public interface SimpleLavaloggedBlock extends BucketPickup, LiquidBlockContainer {

    BooleanProperty LAVALOGGED = BooleanProperty.create("lavalogged");

    @Override
    default boolean canPlaceLiquid(BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return !state.getValue(LAVALOGGED) && fluid == Fluids.LAVA;
    }

    @Override
    default boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState){
        if (!state.getValue(LAVALOGGED) && fluidState.getType() == Fluids.LAVA) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(LAVALOGGED, true), 3);
                level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            }

            return true;
        }
        return false;
    }

    @Override
    default ItemStack pickupBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        if (state.getValue(LAVALOGGED)) {
            level.setBlock(pos, state.setValue(LAVALOGGED, false), 3);
            if (!state.canSurvive(level, pos)) {
                level.destroyBlock(pos, true);
            }

            return new ItemStack(Items.LAVA_BUCKET);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    default Optional<SoundEvent> getPickupSound(BlockState state) {
        return Fluids.LAVA.getPickupSound();
    }

}
