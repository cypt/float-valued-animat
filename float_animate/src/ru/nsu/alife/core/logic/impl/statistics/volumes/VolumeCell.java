package ru.nsu.alife.core.logic.impl.statistics.volumes;

import ru.nsu.alife.core.logic.impl.statistics.CubeUtils;
import ru.nsu.alife.core.logic.impl.statistics.ExtendedStatisticCube;

import java.util.Arrays;

public class VolumeCell implements IVolume {

    private int[] point;
    private int[] shape;
    private int[] tempPoint;

    private ExtendedStatisticCube extendedStatisticCube;

    public VolumeCell(final ExtendedStatisticCube extendedStatisticCube, final int[] point) {
        this.point = point;
        this.tempPoint = new int[point.length];
        this.extendedStatisticCube = extendedStatisticCube;
    }

    @Override
    public boolean isInside(final float[] values) {
        return Arrays.equals(CubeUtils.initIndex(point, shape, values), this.point);
    }

    @Override
    public boolean compareTo(final IVolume volume) {
        //todo : remake
        return false;
    }

    public int[] getPoint() {
        return point;
    }

    @Override
    public VolumeStatistic getStatistic() {
        VolumeStatistic volumeStatistic = new VolumeStatistic();
        ExtendedStatisticCube.ExtendedStatisticCell cell = extendedStatisticCube.getAt(point);
        volumeStatistic.positiveResults = cell.positiveResults;
        volumeStatistic.totalResults = cell.totalResults;
        return volumeStatistic;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final VolumeCell that = (VolumeCell) o;

        if (!extendedStatisticCube.equals(that.extendedStatisticCube)) return false;
        if (!Arrays.equals(point, that.point)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(point);
    }
}
