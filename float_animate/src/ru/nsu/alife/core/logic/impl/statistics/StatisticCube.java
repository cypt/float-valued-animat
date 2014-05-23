package ru.nsu.alife.core.logic.impl.statistics;

import ru.nsu.alife.core.LoggerHolder;
import ucar.ma2.ArrayObject;
import ucar.ma2.Index;

import java.util.Arrays;

public class StatisticCube {

    public static class SimpleStatisticCell {
        long positiveResults;
        long totalResults;
        int successCount = 0;

        /**
         * updates statistic in cell
         * if there is multiple fails or successes in a row punishment and reward grows linearly
         * @param success
         */
        public void update(boolean success) {
            if (totalResults > 100000) return;
            if(success) {
                if (successCount < 0 || successCount > 10) {
                    successCount = 0;
                }
                successCount += 1;
                positiveResults += 1 + successCount;
                totalResults +=  1 + successCount;
            }
            else {
                if (successCount > 0) {
                    successCount = 0;
                }
                successCount -= 1;
                totalResults +=  1 - 10*successCount;
            }
        }

        public String toString() {
            return "[" + positiveResults + "," + totalResults + "," + getProbability() + "]";
        }

        public double getProbability() {
            return ((double) positiveResults / totalResults);
        }
    }

    private final int[] sensorsDimensionsSizes;
    private final int actionDurationDimensionSize;

    private final ArrayObject[] cubes;
    private int[] previousSensorsIndex;
    private int previousDurationIndex;

    public StatisticCube(final int[] sensorsDimensionSizes, final int actionDurationDimensionSize) {
        this.actionDurationDimensionSize = actionDurationDimensionSize;
        this.sensorsDimensionsSizes = sensorsDimensionSizes;
        LoggerHolder.logger.debug(
                "StatisticCube : " + actionDurationDimensionSize + " " + Arrays.toString(sensorsDimensionSizes));
        cubes = new ArrayObject[actionDurationDimensionSize];

        for (int i = 0; i < actionDurationDimensionSize; i++) {
            cubes[i] = new ArrayObject(SimpleStatisticCell.class, this.sensorsDimensionsSizes);
        }

    }

    public void updateStatistic(final int[] sensorsIndex, final int durationIndex, final boolean result) {
        //System.out.println("Outside cube we trust");
//        if (!(Arrays.equals(sensorsIndex, previousSensorsIndex) && (durationIndex == previousDurationIndex))) {
            final Index index = cubes[durationIndex].getIndex();
            index.set(sensorsIndex);
           // System.out.println("Inside cube we live");
            SimpleStatisticCell statisticCell = (SimpleStatisticCell) cubes[durationIndex].getObject(index);

            if (statisticCell == null) {
                statisticCell = new SimpleStatisticCell();
                cubes[durationIndex].setObject(index, statisticCell);
            }

            statisticCell.update(result);
//        }
//
//        previousSensorsIndex = sensorsIndex;
//        previousDurationIndex = durationIndex;
    }

    final SimpleStatisticCell getAt(final int[] sensorsIndex, final int actionsIndex) {
        return (SimpleStatisticCell) cubes[actionsIndex].getObject(cubes[actionsIndex].getIndex().set(sensorsIndex));
    }

    public int getActionDurationDimensionSize() {
        return actionDurationDimensionSize;
    }

    public ArrayObject[] getCubes() {
        return cubes;
    }
}
