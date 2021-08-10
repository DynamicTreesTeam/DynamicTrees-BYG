package maxhyper.dtbyg.cells.cell;

import com.ferreusveritas.dynamictrees.cells.MatrixCell;

public class BushyLeafCell extends MatrixCell {

    public BushyLeafCell(int value) {
        super(value, valMap);
    }

    static final byte[] valMap = {
            0, 1, 2, 3, 4, 0, 0, 0, //D Maps * -> 0
            0, 1, 2, 2, 3, 0, 0, 0, //U Maps 3 -> 2, 4 -> 4, * -> *
            0, 1, 2, 3, 4, 0, 0, 0, //N Maps 3 -> 0, 4 -> 2, * -> *
            0, 1, 2, 3, 4, 0, 0, 0, //S Maps 3 -> 0, 4 -> 2, * -> *
            0, 1, 2, 3, 4, 0, 0, 0, //W Maps 3 -> 0, 4 -> 2, * -> *
            0, 1, 2, 3, 4, 0, 0, 0 //E Maps 3 -> 0, 4 -> 2, * -> *
    };

}
