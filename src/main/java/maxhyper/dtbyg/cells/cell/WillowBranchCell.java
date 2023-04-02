package maxhyper.dtbyg.cells.cell;

import com.ferreusveritas.dynamictrees.api.cell.Cell;
import net.minecraft.core.Direction;

/**
 * @author Harley O'Connor
 */
public final class WillowBranchCell implements Cell {

    @Override
    public int getValue() {
        return 4;
    }

    private static final int[] MAP = {7, 6, 7, 7, 7, 7};

    @Override
    public int getValueFromSide(Direction side) {
        return MAP[side.ordinal()];
    }

}
