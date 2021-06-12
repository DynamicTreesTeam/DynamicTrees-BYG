package maxhyper.dtbyg.cells.cell;

import com.ferreusveritas.dynamictrees.api.cells.ICell;
import net.minecraft.util.Direction;

/**
 * @author Harley O'Connor
 */
public final class WillowBranchCell implements ICell {

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
