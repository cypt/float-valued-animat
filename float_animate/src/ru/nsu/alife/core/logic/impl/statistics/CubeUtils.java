package ru.nsu.alife.core.logic.impl.statistics;

import org.apache.log4j.Level;
import ru.nsu.alife.float_world_impl.Experiment;
import ru.nsu.alife.core.LoggerHolder;
import ucar.ma2.ArrayObject;
import ucar.ma2.IndexIterator;

public final class CubeUtils {

    private CubeUtils() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public static interface BustCallback {
        public void bust(final ArrayObject cube, final Object object, final int[] index);
    }

    public static void bust(final ArrayObject cube, final BustCallback callback) {
        IndexIterator indexIterator = cube.getIndexIterator();
        while (indexIterator.hasNext()) {
            indexIterator.getCurrentCounter();
            final Object object = indexIterator.next();
            callback.bust(cube, object, indexIterator.getCurrentCounter());
        }
    }

    public static void copyIndex(final int[] index1, final int[] index2) {
        for (int i = 0; i < index1.length; i++) {
            index2[i] = index1[i];
        }
    }

    public static int getDurationDimensionIndex(final float duration) {
        if (duration == 1.f) {
            return Experiment.PARTS_PER_DURATION - 1;
        } else {
            return (int) (duration * Experiment.PARTS_PER_DURATION);
        }
    }

    public static int getIndexFromDurationValue(long duration, final int action) {
        final int index = (int)((float)(Experiment.ACTION_DIMENSION_SIZES[action] * (Math
                .min(duration, Experiment.MAXIMUM_ACTION_DURATION - 1)) / Experiment.MAXIMUM_ACTION_DURATION));
        return index;
    }

    public static long getDurationFromIndexValue(final int durationIndexValue, final int action) {
        LoggerHolder.logger.log(Level.DEBUG, "Duration dimension index is " + durationIndexValue);
        return (int) (Experiment.MAXIMUM_ACTION_DURATION * (((float) (2 * durationIndexValue + 1))
                / (2 * Experiment.ACTION_DIMENSION_SIZES[action])));
    }

    public static int[] initIndex(final int[] index, final int[] shape, final float[] sensorValues) {
        for (int i = 0; i < sensorValues.length; i++) {
            index[i] = Math.min(shape[i] - 1, (int) (sensorValues[i] * shape[i]));
        }
        return index;
    }
}
