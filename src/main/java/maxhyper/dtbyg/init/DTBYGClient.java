package maxhyper.dtbyg.init;

import com.ferreusveritas.dynamictrees.blocks.rootyblocks.RootyBlock;
import com.ferreusveritas.dynamictrees.systems.RootyBlockHelper;
import corgiaoc.byg.core.BYGBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;

import java.util.LinkedList;
import java.util.List;

public class DTBYGClient {

    public static void setup() {
        registerColorHandlers();
    }

    /**
     * This is a temporal solution till we publish beta 8 of DT that has a simpler way to do this
     */
    private static final List<Block> grassyRoots = new LinkedList<Block>(){{
        add(BYGBlocks.OVERGROWN_DACITE);
        add(BYGBlocks.OVERGROWN_STONE);
        add(BYGBlocks.OVERGROWN_NETHERRACK);
        add(BYGBlocks.MEADOW_GRASSBLOCK);
        add(BYGBlocks.GLOWCELIUM);
        add(BYGBlocks.MOSSY_NETHERRACK);
    }};;
    private static void registerColorHandlers() {
        // Register Rooty Colorizers
        final BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        final int white = 0xFFFFFFFF;
        for (RootyBlock roots : DTBYGRegistries.rootyBlocks){
            if (grassyRoots.contains(roots.getPrimitiveDirt())){
                blockColors.register((state, world, pos, tintIndex) -> {
                            switch(tintIndex) {
                                case 1: return blockColors.getColor(roots.getPrimitiveDirt().defaultBlockState(), world, pos, tintIndex);
                                case 2: return state.getBlock() instanceof RootyBlock ? roots.rootColor(state, world, pos) : white;
                                default: return white;
                            }
                        }, roots
                );
            }
        }
    }

}
