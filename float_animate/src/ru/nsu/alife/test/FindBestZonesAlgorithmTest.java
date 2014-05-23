package ru.nsu.alife.test;

//import ru.nsu.alife.core.LoggerHolder;
//import ru.nsu.alife.core.logic.impl.statistics.ExtendedStatisticCube;
//import ru.nsu.alife.core.logic.impl.statistics.goal_finding_algorithms.FindBestZonesAlgorithm;
//import ru.nsu.alife.core.logic.impl.statistics.volumes.IVolume;
//import ru.nsu.alife.test.utils.SerializeUtils;
//
//import java.util.List;
//
//public class FindBestZonesAlgorithmTest {
//
//    private static final long[][] mapPositive = new long[][]{{1, 1, 1, 0, 0, 0, 0, 0, 0, 0}, {1, 1, 1, 0, 0, 0, 0, 0,
//            0, 0}, {1, 1, 1, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0,
//            0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
//
//    private static final long[][] mapTotal = new long[][]{{1, 1, 1, 100, 0, 0, 0, 0, 0, 0}, {1, 1, 1, 100, 0, 0, 0,
//            0, 0, 0}, {1, 1, 1, 100, 0, 0, 0, 0, 0, 0}, {100, 100, 100, 100, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0,
//            0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
//
//    @org.junit.Test
//    public void testFromArray() {
//        ExtendedStatisticCube extendedStatisticCube = new ExtendedStatisticCube(new int[]{10, 10});
//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 10; j++) {
//                ExtendedStatisticCube.ExtendedStatisticCell cell = new ExtendedStatisticCube.ExtendedStatisticCell();
//                cell.positiveResults = mapPositive[i][j];
//                cell.totalResults = mapTotal[i][j];
//                extendedStatisticCube.setObject(extendedStatisticCube.getIndex().set(new int[]{i, j}), cell);
//            }
//        }
//
//        final List<? extends IVolume> goodVolumes = new FindBestZonesAlgorithm(extendedStatisticCube).start();
//        for (final IVolume volume : goodVolumes) {
//            LoggerHolder.logger.debug(String.valueOf(volume));
//        }
//    }
//
//    @org.junit.Test
//    public void testFromFile() {
//        ExtendedStatisticCube[] extendedStatisticCubes = SerializeUtils.deserializeExtendedStatisticCubes();
//        for (ExtendedStatisticCube cube : extendedStatisticCubes) {
//            LoggerHolder.log(cube);
//            new FindBestZonesAlgorithm(cube).start();
//        }
//    }
//}
