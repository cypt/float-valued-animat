package ru.nsu.alife.core.logic.impl.statistics;

import ru.nsu.alife.float_world_impl.Experiment;
import ucar.ma2.ArrayObject;
import ucar.ma2.Index;
import ucar.ma2.IndexIterator;

import java.io.*;

/**
 * This cube holds best cells of statistic cubes and makes projection on single dimensions
 */
public class ExtendedStatisticCube implements Externalizable {

    private static final long serialVersionUID = 4312093934290503778l;

    public static class ExtendedStatisticCell implements Serializable {
        public long positiveResults;
        public long totalResults;
        public int durationIndex;

        public String toString() {
            return "[" + positiveResults + "," + totalResults + "," + String
                    .format("%.2f", getProbability()) + "," + durationIndex + "]";
        }

        public double getProbability() {
            return ((double) positiveResults / totalResults);
        }
    }

    private ArrayObject cube;
    private int dimensionSizesWithProjectionsIncluded[];

    public ExtendedStatisticCube() {
    }

    public ExtendedStatisticCube(final int[] dimensionSizesWithProjectionsIncluded) {
        this.dimensionSizesWithProjectionsIncluded = dimensionSizesWithProjectionsIncluded;
        cube = new ArrayObject(ExtendedStatisticCell.class, dimensionSizesWithProjectionsIncluded);
    }

    public void process(final StatisticCube statisticCube) {
        final IndexIterator indexIterator = statisticCube.getCubes()[0].getIndexIterator();
        while (indexIterator.hasNext()) {
            indexIterator.next();
            final int[] sensorIndex = indexIterator.getCurrentCounter();
            process(sensorIndex, statisticCube);
        }
    }

    private void process(final int[] sensorsIndex, final StatisticCube statisticCube) {
        final int actionDurationDimensionSize = statisticCube.getActionDurationDimensionSize();
        int bestDurationIndex = -1;
        StatisticCube.SimpleStatisticCell bestStatisticCell = null;
        for (int i = 0; i < actionDurationDimensionSize; i++) {
            final StatisticCube.SimpleStatisticCell statisticCell = statisticCube.getAt(sensorsIndex, i);
            if ((bestStatisticCell == null) || ((statisticCell != null) && (statisticCell
                    .getProbability() > bestStatisticCell.getProbability() && statisticCell.totalResults > Experiment
                    .TOTAL_RESULTS_TRESHOLD))) {
                bestStatisticCell = statisticCell;
                bestDurationIndex = i;
            }
        }
        convert(sensorsIndex, bestDurationIndex, bestStatisticCell);
    }

    private void convert(
            final int[] sensorsIndex, final int durationIndex, final StatisticCube.SimpleStatisticCell statisticCell) {
        final Index index = cube.getIndex().set(sensorsIndex);
        ExtendedStatisticCell extendedStatisticCell = (ExtendedStatisticCell) cube.getObject(index);
        if (extendedStatisticCell == null) {
            extendedStatisticCell = new ExtendedStatisticCell();
            cube.setObject(index, extendedStatisticCell);
        }
        if (statisticCell != null) {
            extendedStatisticCell.positiveResults = statisticCell.positiveResults;
            extendedStatisticCell.totalResults = statisticCell.totalResults;
            extendedStatisticCell.durationIndex = durationIndex;
        } else {
            extendedStatisticCell.positiveResults = 0;
            extendedStatisticCell.totalResults = 0;
            extendedStatisticCell.durationIndex = 0;
        }
    }

    public ExtendedStatisticCell getAt(final int[] index) {
        final Index _index = cube.getIndex();
        _index.set(index);
        return (ExtendedStatisticCell) cube.getObject(_index);
    }

    public int[] getDimensionSizesWithProjectionsIncluded() {
        return dimensionSizesWithProjectionsIncluded;
    }

    public ArrayObject getCube() {
        return cube;
    }

    public void setObject(final Index i, final Object value) {
        cube.setObject(i, value);
    }

    public Index getIndex() {
        return cube.getIndex();
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeObject(dimensionSizesWithProjectionsIncluded);
        IndexIterator it = cube.getIndexIterator();
        while (it.hasNext()) {
            out.writeObject(it.next());
        }
        out.flush();
    }

    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        dimensionSizesWithProjectionsIncluded = (int[]) in.readObject();
        cube = new ArrayObject(ExtendedStatisticCell.class, dimensionSizesWithProjectionsIncluded);
        IndexIterator it = cube.getIndexIterator();
        while (it.hasNext()) {
            it.next();
            it.getCurrentCounter();
            cube.setObject(cube.getIndex().set(it.getCurrentCounter()), in.readObject());
        }
    }
}
