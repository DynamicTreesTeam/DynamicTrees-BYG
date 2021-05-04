package maxhyper.dtbyg.cells;

import com.ferreusveritas.dynamictrees.api.cells.CellKit;
import com.ferreusveritas.dynamictrees.api.cells.CellNull;
import com.ferreusveritas.dynamictrees.api.cells.ICell;
import com.ferreusveritas.dynamictrees.api.cells.ICellSolver;
import com.ferreusveritas.dynamictrees.api.registry.IRegistry;
import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.cells.MetadataCell;
import com.ferreusveritas.dynamictrees.cells.NormalCell;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import maxhyper.dtbyg.DynamicTreesBYG;
import net.minecraft.util.ResourceLocation;

public class DTBYGCellKits {

    public static void register(final IRegistry<CellKit> registry) {
        registry.registerAll(SPARSE, POPLAR);
    }

    public static final CellKit SPARSE = new CellKit(new ResourceLocation(DynamicTreesBYG.MOD_ID, "sparse")) {

        private final ICell sparseBranch = new SparseBranchCell();
        private final ICell sparseLeaves = new NormalCell(1);

        private final ICellSolver solver = new com.ferreusveritas.dynamictrees.cells.CellKits.BasicSolver(new short[] {0x0211});

        @Override
        public ICell getCellForLeaves(int hydro) {
            return hydro > 0 ? sparseLeaves : CellNull.NULL_CELL;
        }

        @Override
        public ICell getCellForBranch(int radius, int meta) {
            return radius == 1 ? sparseBranch : CellNull.NULL_CELL;
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return DTBYGLeafClusters.sparse;
        }

        @Override
        public ICellSolver getCellSolver() {
            return solver;
        }

        @Override
        public int getDefaultHydration() {
            return 1;
        }

    };

    public static final CellKit POPLAR = new CellKit(new ResourceLocation(DynamicTreesBYG.MOD_ID, "poplar")) {

        private final ICell poplarBranch = new PoplarBranchCell();
        private final ICell poplarTopBranch = new PoplarTopBranchCell();
        private final ICell poplarUpperTrunk = new NormalCell(4);

        private final ICell[] poplarLeaves = new ICell[] {
                CellNull.NULL_CELL,
                new PoplarLeafCell(1),
                new PoplarLeafCell(2),
                new PoplarLeafCell(3),
                new PoplarLeafCell(4),
        };

        private final ICellSolver solver = new com.ferreusveritas.dynamictrees.cells.CellKits.BasicSolver(new short[] {
                0x0412, 0x0311, 0x0211
        });

        @Override
        public ICell getCellForLeaves(int hydro) {
            return poplarLeaves[hydro];
        }

        @Override
        public ICell getCellForBranch(int radius, int meta) {
            if (meta == MetadataCell.CONIFERTOP) return poplarTopBranch;
            if (radius == 1) return poplarBranch;
            if (radius < 4) return poplarUpperTrunk;
            return CellNull.NULL_CELL;
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return DTBYGLeafClusters.poplar;
        }

        @Override
        public ICellSolver getCellSolver() {
            return solver;
        }

        @Override
        public int getDefaultHydration() {
            return 4;
        }

    };

}
