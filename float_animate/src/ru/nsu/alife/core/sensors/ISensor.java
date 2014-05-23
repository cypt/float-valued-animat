package ru.nsu.alife.core.sensors;

public interface ISensor {
    public void updateValue();

    public void addSensorEventListener(final ISensorEventListener iSensorEventListener);

    public String getName();

    public float getValue();
}
