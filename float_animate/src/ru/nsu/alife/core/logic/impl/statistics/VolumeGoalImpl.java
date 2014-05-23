package ru.nsu.alife.core.logic.impl.statistics;

import ru.nsu.alife.core.logic.fs.IGoal;
import ru.nsu.alife.core.logic.fs.ISensorsProvider;
import ru.nsu.alife.core.logic.impl.statistics.volumes.IVolume;
import ru.nsu.alife.core.sensors.ISensor;

public class VolumeGoalImpl implements IGoal, IVolume {

    private final ISensorsProvider sensorsProvider;
    private final IVolume volume;

    public VolumeGoalImpl(final ISensorsProvider sensorsProvider, final IVolume volume) {
        this.sensorsProvider = sensorsProvider;
        this.volume = volume;
    }

    public boolean check(final float[] sensorsIndex) {
//        final long counter = Utils.getProjectionsCounter(sensorsIndex);
//        for (final long )
        return volume.isInside(sensorsIndex);
    }

    @Override
    public boolean check() {
        final float[] sensorsValues = new float[sensorsProvider.getSensors().size()];
        int i = 0;
        for (final ISensor sensor : sensorsProvider.getSensors()) {
            sensorsValues[i++] = sensor.getValue();
        }
        return check(sensorsValues);
    }

    public IVolume getVolume() {
        return volume;
    }

    @Override
    public boolean isInside(final float[] point) {
        return volume.isInside(point);
    }

    @Override
    public boolean compareTo(final IVolume volume) {
        return volume.compareTo(volume);
    }

    @Override
    public VolumeStatistic getStatistic() {
        return volume.getStatistic();
    }
}
