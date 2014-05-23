package ru.nsu.alife.core.logic.fs;

import ru.nsu.alife.core.sensors.ISensor;

import java.util.List;

public final class Utils {

    private Utils() {
    }

    public static int[] getSensorsDimensionsSizesWithProjection(int[] sensorsDimensionsSizes) {
        int[] result = new int[sensorsDimensionsSizes.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = sensorsDimensionsSizes[i] + 1;
        }
        return result;
    }

    public static int[] getNewIndexWithProjectionIncluded(
            final int[] sensorsIndex, final long counter, final int[] sensorDimensionWithProjectionSizes) {
        final int[] result = new int[sensorsIndex.length];
        final int length = sensorsIndex.length;
        long indexer = 1;
        for (int i = 0; i < length; i++) {
            if ((indexer & counter) != 0) {
                result[i] = sensorDimensionWithProjectionSizes[i] - 1;
            } else {
                result[i] = sensorsIndex[i];
            }
            indexer *= 2;
        }
        return result;
    }

    public static long getProjectionsCounter(final int[] sensorsIndex) {
        return (long) ((1 << sensorsIndex.length) - 1);
    }

    public static float[] getSensorValues(final ISensorsProvider sensorsProvider) {
        final List<ISensor> sensorsProviderContent = sensorsProvider.getSensors();
        final float[] sensorsValues = new float[sensorsProviderContent.size()];
        int counter = 0;
        for (final ISensor sensor : sensorsProviderContent) {
            sensor.updateValue();
            sensorsValues[counter++] = sensor.getValue();
        }
        return sensorsValues;
    }
}
