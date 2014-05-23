package ru.nsu.alife.core.sensors;

import ru.nsu.alife.float_world_impl.world.Animal;

public interface ISensorEventListener {
    public static interface SensorEvent {
        public Animal getAnimal();

        public String getEventType();

        public ISensor getSensor();

        public boolean isDetected();
    }

    public void onSensorEvent(final SensorEvent sensorEvent);
}
