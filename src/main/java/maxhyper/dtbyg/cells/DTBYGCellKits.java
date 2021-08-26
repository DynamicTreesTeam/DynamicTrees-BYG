package maxhyper.dtbyg.cells;

import com.ferreusveritas.dynamictrees.api.cells.CellKit;
import com.ferreusveritas.dynamictrees.api.cells.CellNull;
import com.ferreusveritas.dynamictrees.api.cells.ICell;
import com.ferreusveritas.dynamictrees.api.cells.ICellSolver;
import com.ferreusveritas.dynamictrees.api.registry.IRegistry;
import com.ferreusveritas.dynamictrees.cells.*;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import maxhyper.dtbyg.DynamicTreesBYG;
import maxhyper.dtbyg.cells.cell.*;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

public class DTBYGCellKits {

    public static void register(final IRegistry<CellKit> registry) {
        registry.registerAll(PALM, SPARSE, POPLAR, SMALL_DECIDUOUS, WILLOW, ROUND_CONIFER, SYTHIAN_FUNGUS);
    }

    public static final CellKit PALM = new CellKit(new ResourceLocation(DynamicTreesBYG.MOD_ID, "palm")) {

        private final ICell palmBranch = new ICell() {
            @Override
            public int getValue() {
                return 5;
            }

            @Override
            public int getValueFromSide(Direction side) {
                return side == Direction.UP ? getValue() : 0;
            }

        };

        private final ICell[] palmFrondCells = {
                CellNull.NULL_CELL,
                new PalmFrondCell(1),
                new PalmFrondCell(2),
                new PalmFrondCell(3),
                new PalmFrondCell(4),
                new PalmFrondCell(5),
                new PalmFrondCell(6),
                new PalmFrondCell(7)
        };

        private final CellKits.BasicSolver palmSolver = new CellKits.BasicSolver(new short[]{0x0514, 0x0413, 0x0312, 0x0221});

        @Override
        public ICell getCellForLeaves(int hydro) {
            return palmFrondCells[hydro];
        }

        @Override
        public ICell getCellForBranch(int radius, int meta) {
            return radius == 3? palmBranch : CellNull.NULL_CELL;
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return LeafClusters.PALM;
        }

        @Override
        public ICellSolver getCellSolver() {
            return palmSolver;
        }

        @Override
        public int getDefaultHydration() {
            return 4;
        }

    };

    public static final CellKit SPARSE = new CellKit(new ResourceLocation(DynamicTreesBYG.MOD_ID, "sparse")) {

        private final ICell sparseBranch = new SparseBranchCell();
        private final ICell sparseLeaves = new NormalCell(1);

        private final ICellSolver solver = new CellKits.BasicSolver(new short[] {0x0211});

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
            return DTBYGLeafClusters.SPARSE;
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

        private final ICellSolver solver = new CellKits.BasicSolver(new short[] {
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
            return DTBYGLeafClusters.POPLAR;
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

    public static final CellKit SMALL_DECIDUOUS = new CellKit(new ResourceLocation(DynamicTreesBYG.MOD_ID, "small_deciduous")) {

        private final ICell sparseBranch = new NormalCell(4);
        private final ICell sparseLeaves = new NormalCell(1);

        private final ICellSolver solver = new CellKits.BasicSolver(new short[] {0x0211});

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
            return DTBYGLeafClusters.SPARSE;
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

    // TODO: Still needs some work.
    public static final CellKit WILLOW = new CellKit(DynamicTreesBYG.resLoc("willow")) {

        private final ICell branch = new WillowBranchCell();

        private final ICell[] willowLeafCells = {
                CellNull.NULL_CELL,
                new WillowLeafCell(1),
                new WillowLeafCell(2),
                new WillowLeafCell(3),
                new WillowLeafCell(4),
                new WillowLeafCell(5),
                new WillowLeafCell(6),
                new WillowLeafCell(7)
        };

        private final CellKits.BasicSolver solver = new CellKits.BasicSolver(new short[]{0x0817, 0x0726, 0x0625, 0x0714, 0x0614, 0x0514, 0x0413, 0x0312, 0x0211});

        @Override
        public ICell getCellForLeaves(int distance) {
            return this.willowLeafCells[distance];
        }

        @Override
        public ICell getCellForBranch(int radius, int meta) {
            return radius == 1 ? this.branch : CellNull.NULL_CELL;
        }

        @Override
        public ICellSolver getCellSolver() {
            return this.solver;
        }

        // TODO: Willow leaf cluster.
        @Override
        public SimpleVoxmap getLeafCluster() {
            return DTBYGLeafClusters.WILLOW;
        }

        @Override
        public int getDefaultHydration() {
            return 7;
        }
    };

    public static final CellKit ROUND_CONIFER = new CellKit(DynamicTreesBYG.resLoc("round_conifer")) {

        private final ICell coniferBranch = new NormalCell(3);
        private final ICell coniferTopBranch = new ConiferTopBranchCell();

        private final ICell[] coniferLeafCells = {
                CellNull.NULL_CELL,
                new ConiferLeafCell2(1),
                new ConiferLeafCell2(2),
                new ConiferLeafCell2(3),
                new ConiferLeafCell2(4),
                new ConiferLeafCell2(5),
                new ConiferLeafCell2(6),
                new ConiferLeafCell2(7)
        };

        private final CellKits.BasicSolver coniferSolver =
                new CellKits.BasicSolver(new short[]{0x0514, 0x0413, 0x0312, 0x0221});

        @Override
        public ICell getCellForLeaves(int hydro) {
            return coniferLeafCells[hydro];
        }

        @Override
        public ICell getCellForBranch(int radius, int meta) {
            if (meta == MetadataCell.CONIFERTOP) {
                return coniferTopBranch;
            } else if (radius == 1) {
                return coniferBranch;
            } else {
                return CellNull.NULL_CELL;
            }
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return DTBYGLeafClusters.ROUND_CONIFER;
        }

        @Override
        public ICellSolver getCellSolver() {
            return coniferSolver;
        }

        @Override
        public int getDefaultHydration() {
            return 4;
        }

    };

    public static final CellKit BUSHY = new CellKit(DynamicTreesBYG.resLoc("bushy")) {

        private final ICell branchCell = new BushyBranchCell();
        private final ICell coniferTopBranch = new ConiferTopBranchCell();

        private final ICell[] coniferLeafCells = {
                CellNull.NULL_CELL,
                new BushyLeafCell(1),
                new BushyLeafCell(2),
                new BushyLeafCell(3),
                new BushyLeafCell(4),
                new BushyLeafCell(5),
                new BushyLeafCell(6),
                new BushyLeafCell(7)
        };

        private final CellKits.BasicSolver solver = new CellKits.BasicSolver(new short[]{0x0614, 0x0513, 0x0423, 0x0322, 0x0411, 0x0211});

        @Override
        public ICell getCellForLeaves(int hydro) {
            return coniferLeafCells[hydro];
        }

        @Override
        public ICell getCellForBranch(int radius, int meta) {
            if (meta == MetadataCell.CONIFERTOP) {
                return coniferTopBranch;
            } else if (radius == 1) {
                return branchCell;
            } else {
                return CellNull.NULL_CELL;
            }
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return DTBYGLeafClusters.BUSHY;
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

    public static final CellKit SYTHIAN_FUNGUS = new CellKit(DynamicTreesBYG.resLoc("sythian_fungus")) {

        private final ICell sythianBranch = new SythianWartCell(3);
        private final ICell sythianTopBranch = new SythianWartCell(5);

        private final ICell[] sythianLeafCells = {
                CellNull.NULL_CELL,
                new SythianWartCell(1),
                new SythianWartCell(2),
                new SythianWartCell(3),
                new SythianWartCell(4),
                new SythianWartCell(4),
                new SythianWartCell(4),
                new SythianWartCell(4)
        };

        private final CellKits.BasicSolver sythianSolver = new CellKits.BasicSolver(new short[]{0x0514, 0x0411, 0x0312, 0x0221});

        @Override
        public ICell getCellForLeaves(int hydro) {
            return sythianLeafCells[hydro];
        }

        @Override
        public ICell getCellForBranch(int radius, int meta) {
            if (meta == MetadataCell.CONIFERTOP) {
                return sythianTopBranch;
            } else if (radius == 3){
                return sythianBranch;
            } else {
                return CellNull.NULL_CELL;
            }
        }

        @Override
        public SimpleVoxmap getLeafCluster() {
            return DTBYGLeafClusters.SYTHIAN_FUNGUS;
        }

        @Override
        public ICellSolver getCellSolver() {
            return sythianSolver;
        }

        @Override
        public int getDefaultHydration() {
            return 4;
        }

    };

}
