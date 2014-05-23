package ru.nsu.alife.core.logic.impl.statistics.goal_finding_algorithms;

import ru.nsu.alife.core.logic.impl.statistics.ExtendedStatisticCube;
import ru.nsu.alife.core.logic.impl.statistics.SlicedStatisticCube;
import ru.nsu.alife.core.logic.impl.statistics.volumes.IVolume;
import ru.nsu.alife.core.logic.impl.statistics.volumes.MergedSubVolume;
import ru.nsu.alife.float_world_impl.Experiment;
import ucar.ma2.IndexIterator;

import java.util.Collections;
import java.util.List;

public class FindGoodVolumeAlgorithm implements Algorithm {

    private SlicedStatisticCube[][] slicedStatisticCubes;

    public FindGoodVolumeAlgorithm(final SlicedStatisticCube[][] slicedStatisticCubes) {
        this.slicedStatisticCubes = slicedStatisticCubes;
    }

    @Override
    public List<? extends IVolume> start() {
        final ExtendedStatisticCube[] extendedStatisticCubes = new ExtendedStatisticCube[slicedStatisticCubes.length];

        for (int i = 0; i < slicedStatisticCubes.length; i++) {
            extendedStatisticCubes[i] = slicedStatisticCubes[i][0].getExtendedStatisticCube();
        }

        MergedSubVolume subVolume = new MergedSubVolume(Experiment.SENSOR_DIMENSION_SIZES_DS[0],
                extendedStatisticCubes);

        final IndexIterator indexIterator = subVolume.
                getExtendedStatisticCubes()[0].getCube().getIndexIterator();

        final int[] sensorsDimensionSizes = slicedStatisticCubes[0][0].getSensorsDimensionSizes();
        final float[] sensorValues = new float[sensorsDimensionSizes.length];

        while (indexIterator.hasNext()) {
            indexIterator.next();

            final int[] currentCounter = indexIterator.getCurrentCounter();

            double bestProbability = 0;
            int bestAction = -1;

            createSensorValues(sensorValues, sensorsDimensionSizes, currentCounter);

            for (int i = 0; i < slicedStatisticCubes.length; i++) {
                for (int j = 0; j < slicedStatisticCubes[i].length; j++) {

                    final ExtendedStatisticCube.ExtendedStatisticCell at = slicedStatisticCubes[i][j]
                            .getAt(sensorValues);

                    if (at != null) {

                        final double probability = at.getProbability();

                        if (probability > bestProbability && at.totalResults > 10){
                            bestAction = i;
                            bestProbability = probability;
                        }
                    }
                }
            }

            if (bestProbability > 0.8) {
                subVolume.setPointValue(currentCounter, bestAction);
            }
        }

        subVolume.updateStatistic();

        return Collections.singletonList(subVolume);
    }

    private void createSensorValues(final float[] sensorValues, final int[] sensorDimensionSizes, final int[] index) {
        for (int i = 0; i < index.length; i++) {
            sensorValues[i] = (float) (2 * index[i] + 1) / (2 * sensorDimensionSizes[i]);
        }
    }
}
