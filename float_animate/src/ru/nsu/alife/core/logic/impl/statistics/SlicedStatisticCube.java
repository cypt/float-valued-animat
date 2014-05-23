package ru.nsu.alife.core.logic.impl.statistics;

public class SlicedStatisticCube {

    private StatisticCube statisticCube;

    private ExtendedStatisticCube extendedStatisticCube;

    private int[] sensorsDimensionSizes;

    private int[] sensorsIndex;

    public SlicedStatisticCube(
            final int[] sensorsDimensionSizes, final int actionDurationDimensionSize) {
        this.statisticCube = new StatisticCube(sensorsDimensionSizes, actionDurationDimensionSize);
        this.extendedStatisticCube = new ExtendedStatisticCube(sensorsDimensionSizes);
        this.sensorsDimensionSizes = sensorsDimensionSizes;
        this.sensorsIndex = new int[sensorsDimensionSizes.length];
    }

    public StatisticCube.SimpleStatisticCell getAt(final float[] sensorsValues, final int actionsIndex) {
        return statisticCube
                .getAt(CubeUtils.initIndex(sensorsIndex, sensorsDimensionSizes, sensorsValues), actionsIndex);
    }

    public void updateStatistic(final float[] sensorsValues, final int durationIndex, final boolean result) {
        statisticCube
                .updateStatistic(CubeUtils.initIndex(sensorsIndex, sensorsDimensionSizes, sensorsValues), durationIndex,
                        result);
    }

    public ExtendedStatisticCube.ExtendedStatisticCell getAt(final float[] sensorsValues) {
        return extendedStatisticCube.getAt(CubeUtils.initIndex(sensorsIndex, sensorsDimensionSizes, sensorsValues));
    }

    public void process() {
        extendedStatisticCube.process(statisticCube);
    }

    public StatisticCube getStatisticCube() {
        return statisticCube;
    }

    public ExtendedStatisticCube getExtendedStatisticCube() {
        return extendedStatisticCube;
    }

    public int[] getSensorsDimensionSizes() {
        return sensorsDimensionSizes;
    }
}
