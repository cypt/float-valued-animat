package ru.nsu.alife.core.logic.impl.statistics.volumes;

import ru.nsu.alife.core.logic.impl.statistics.CubeUtils;
import ru.nsu.alife.core.logic.impl.statistics.ExtendedStatisticCube;
import ucar.ma2.ArrayByte;
import ucar.ma2.Index;
import ucar.ma2.IndexIterator;

public class MergedSubVolume implements IVolume {

    private static final byte UNUSED = -1;

    private ExtendedStatisticCube[] extendedStatisticCubes;
    private ArrayByte cube;

    private VolumeStatistic volumeStatistic = new VolumeStatistic();

    private int[] shape;
    private int[] sensorsIndex;

    public MergedSubVolume(final int shape[], final ExtendedStatisticCube[] cubes) {
        this.shape = shape;
        this.sensorsIndex = new int[shape.length];

        cube = new ArrayByte(shape);
        IndexIterator indexIterator = cube.getIndexIterator();

        while (indexIterator.hasNext()) {
            indexIterator.next();
            indexIterator.setByteCurrent(UNUSED);
        }

        extendedStatisticCubes = cubes;
    }

    public void setPointValue(final int[] point, final int actionIndex) {
        Index index = cube.getIndex().set(point);
        cube.setByte(index, (byte) actionIndex);
    }

    public byte getPointAt(final int[] point) {
        Index index = cube.getIndex().set(point);
        return cube.getByte(index);
    }

    @Override
    public boolean isInside(final float[] point) {
        Index index = cube.getIndex().set(CubeUtils.initIndex(sensorsIndex, shape, point));

        if (cube.getByte(index) != UNUSED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean compareTo(IVolume volume) {
        if (volume.getClass() == MergedSubVolume.class) {
            return this.cube.equals(((MergedSubVolume) volume).cube);
        } else {
            return false;
        }
    }

    public void updateStatistic() {
        volumeStatistic = new VolumeStatistic();

        IndexIterator indexIterator = cube.getIndexIterator();

        while (indexIterator.hasNext()) {
            indexIterator.next();

            final byte byteCurrent = indexIterator.getByteCurrent();

            if (byteCurrent != -1) {
                final ExtendedStatisticCube extendedStatisticCube = extendedStatisticCubes[byteCurrent];
                final ExtendedStatisticCube.ExtendedStatisticCell at = extendedStatisticCube
                        .getAt(indexIterator.getCurrentCounter());
                volumeStatistic.positiveResults += at.positiveResults;
                volumeStatistic.totalResults += at.totalResults;
            }
        }
    }

    @Override
    public VolumeStatistic getStatistic() {
        try {
            return (VolumeStatistic) volumeStatistic.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayByte getCube() {
        return cube;
    }

    public ExtendedStatisticCube[] getExtendedStatisticCubes() {
        return extendedStatisticCubes;
    }
}
