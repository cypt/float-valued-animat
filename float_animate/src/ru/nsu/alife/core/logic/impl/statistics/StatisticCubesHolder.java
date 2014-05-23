package ru.nsu.alife.core.logic.impl.statistics;

import ru.nsu.alife.core.logic.fs.IUpdateStatisticListener;
import ru.nsu.alife.float_world_impl.Experiment;

import java.util.List;
import java.util.Vector;

public class StatisticCubesHolder {

    private final int actionsCount;
    private final int dCount;

    private final int[] sensorsDimensionSizes;
    private final int[] actionDimensionSizes;

    private ExtendedStatisticCube[] extendedStatisticCubes;
    private StatisticCube[] statisticCubes;

    private final SlicedStatisticCube[][] slicedStatisticCubes;

    private final List<IUpdateStatisticListener> updateStatisticListeners = new Vector<IUpdateStatisticListener>();

    public StatisticCubesHolder(final int[] actionDimensionSizes) {

        this.sensorsDimensionSizes = Experiment.SENSOR_DIMENSION_SIZES_DS[0];
        this.actionDimensionSizes = actionDimensionSizes;

        this.actionsCount = actionDimensionSizes.length;

        final int size = actionsCount;

        dCount = Experiment.SENSOR_DIMENSION_SIZES_DS.length;
        slicedStatisticCubes = new SlicedStatisticCube[size][dCount];

        extendedStatisticCubes = new ExtendedStatisticCube[actionsCount];
        statisticCubes = new StatisticCube[actionsCount];

        for (int i = 0; i < actionsCount; i++) {

            for (int j = 0; j < dCount; j++) {
                slicedStatisticCubes[i][j] = new SlicedStatisticCube(Experiment.SENSOR_DIMENSION_SIZES_DS[j],
                        actionDimensionSizes[i]);
            }
            statisticCubes[i] = slicedStatisticCubes[i][0].getStatisticCube();
            extendedStatisticCubes[i] = slicedStatisticCubes[i][0].getExtendedStatisticCube();
        }
    }

    public int[] getSensorsDimensionSizes() {
        return sensorsDimensionSizes;
    }

    public int[] getActionDimensionSizes() {
        return actionDimensionSizes;
    }

    public void updateStatistic(
            final float[] sensorsValues, final int actionIndex, final int durationIndex, final boolean result) {
        for (int i = 0; i < dCount; i++) {
            //System.out.println("UPDATING");
            slicedStatisticCubes[actionIndex][i].updateStatistic(sensorsValues, durationIndex, result);
        }
    }

    public void endUpdatingBatch() {
        for (int i = 0; i < actionsCount; i++) {
            for (int j = 0; j < dCount; j++) {
                slicedStatisticCubes[i][j].process();
                //System.out.println("PROCESSING");
            }
        }

        for (IUpdateStatisticListener updateStatisticListener : updateStatisticListeners) {
            updateStatisticListener.onStatisticUpdated(this);
        }
    }

    public void addUpdateStatisticListener(final IUpdateStatisticListener updateStatisticListener) {
        this.updateStatisticListeners.add(updateStatisticListener);
    }

    public StatisticCube[] getStatisticCubes() {
        return statisticCubes;
    }

    public ExtendedStatisticCube[] getExtendedStatisticCubes() {
        return extendedStatisticCubes;
    }

    public SlicedStatisticCube[][] getSlicedStatisticCubes() {
        return slicedStatisticCubes;
    }

    public int getSensorsCount() {
        return sensorsDimensionSizes.length;
    }

    public int getActionsCount() {
        return actionsCount;
    }

    public static class Result {
        public int actionIndex;
        public int durationIndex;
        public int dIndex;
        public double probability;

        public Result(final int actionIndex, final int durationIndex, final int dIndex, final double probability) {
            this.actionIndex = actionIndex;
            this.durationIndex = durationIndex;
            this.dIndex = dIndex;
            this.probability = probability;
        }
    }

    public Result getRule(final float[] sensorValues) {

        double bProb = 0;
        int bActionIndex = -1;
        int bDIndex = -1;

        ExtendedStatisticCube.ExtendedStatisticCell bCell = null;

        for (int i = 0; i < actionsCount; i++) {
            for (int j = 0; j < dCount; j++) {

                final ExtendedStatisticCube.ExtendedStatisticCell at = slicedStatisticCubes[i][j].getAt(sensorValues);

                if (at != null) {

                    final double prob = at.getProbability();

                    if (prob > bProb && at.totalResults > 10) {
                        bCell = at;
                        bActionIndex = i;
                        bDIndex = j;
                        bProb = prob;
                    }

                }

            }
        }

        if (bCell != null) {
            return new Result(bActionIndex, bCell.durationIndex, bDIndex, bCell.getProbability());
        } else {
            return null;
        }
    }
}
