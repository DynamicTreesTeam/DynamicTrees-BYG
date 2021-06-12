package maxhyper.dtbyg.cells.cell;

import com.ferreusveritas.dynamictrees.cells.MatrixCell;

/**
 * @author Harley O'Connor
 */
public final class WillowLeafCell extends MatrixCell {

    private static final byte[] VAL_MAP = {
            0, 1, 2, 3, 4, 5, 6, 7, // D Maps 5 -> 4, * -> *
            0, 0, 0, 0, 4, 5, 5, 6, // U Maps 6 -> 6, 7 -> 7, * -> 0
            0, 0, 0, 0, 4, 5, 5, 6, // N Maps 6 -> 6, 7 -> 7, * -> 0
            0, 0, 0, 0, 4, 5, 5, 6, // S Maps 6 -> 6, 7 -> 7, * -> 0
            0, 0, 0, 0, 4, 5, 5, 6, // W Maps 6 -> 6, 7 -> 7, * -> 0
            0, 0, 0, 0, 4, 5, 5, 6  // E Maps 6 -> 6, 7 -> 7, * -> 0
    };

    public WillowLeafCell(int value) {
        super(value, VAL_MAP);
    }

}
