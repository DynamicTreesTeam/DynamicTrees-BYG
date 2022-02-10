package maxhyper.dtbyg.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public interface LavaLoggable extends IBucketPickupHandler, ILiquidContainer {

    BooleanProperty LAVALOGGED = BooleanProperty.create("lavalogged");

    default boolean canPlaceLiquid(IBlockReader reader, BlockPos pos, BlockState blockState, Fluid fluid) {
        return !blockState.getValue(LAVALOGGED) && fluid == Fluids.LAVA;
    }

    default boolean placeLiquid(IWorld world, BlockPos pos, BlockState blockState, FluidState fluidState) {
        if (!blockState.getValue(LAVALOGGED) && fluidState.getType() == Fluids.LAVA) {
            if (!world.isClientSide()) {
                world.setBlock(pos, blockState.setValue(LAVALOGGED, true), 3);
                world.getLiquidTicks().scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(world));
            }

            return true;
        } else {
            return false;
        }
    }

    default Fluid takeLiquid(IWorld world, BlockPos pos, BlockState blockState) {
        if (blockState.getValue(LAVALOGGED)) {
            world.setBlock(pos, blockState.setValue(LAVALOGGED, false), 3);
            return Fluids.LAVA;
        } else {
            return Fluids.EMPTY;
        }
    }

}
