package ru.nsu.alife.float_world_impl.sensors;

import ru.nsu.alife.float_world_impl.Experiment;
import ru.nsu.alife.float_world_impl.world.Animal;
import ru.nsu.alife.float_world_impl.world.Apple;
import ru.nsu.alife.float_world_impl.world.World;
import ru.nsu.alife.core.sensors.ISensor;
import ru.nsu.alife.core.sensors.ISensorEventListener;

import java.util.ArrayList;
import java.util.List;

public class DistanceSensor implements ISensor {

    public class AppleAtFrontEvent implements ISensorEventListener.SensorEvent {
        public static final String APPLE_AT_FRONT_DETECTION_EVENT = "APPLE_AT_FRONT_DETECTION_EVENT";

        private final Apple apple;
        private final boolean detected;

        private AppleAtFrontEvent(final Apple apple) {
            this.apple = apple;
            this.detected = true;
        }

        private AppleAtFrontEvent(boolean detected) {
            this.detected = detected;
            this.apple = null;
        }

        public Apple getApple() {
            return apple;
        }

        @Override
        public Animal getAnimal() {
            return DistanceSensor.this.animal;
        }

        @Override
        public String getEventType() {
            return APPLE_AT_FRONT_DETECTION_EVENT;
        }

        public boolean isDetected() {
            return detected;
        }

        public ISensor getSensor() {
            return DistanceSensor.this;
        }
    }

    private final World world;
    private final Animal animal;
    private float value = Float.MAX_VALUE;

    private final ArrayList<ISensorEventListener> sensorEventListeners = new ArrayList<ISensorEventListener>();

    public DistanceSensor(final World world, final Animal animal) {
        this.world = world;
        this.animal = animal;
    }

    public float getValue() {
        return value;
    }

    public void updateValue() {
        final List<Apple> apples = world.getApples();
        Apple nearestApple = null;
        float nearestDistance = Float.MAX_VALUE;
        for (final Apple apple : apples) {
            final float distance = SensorUtils.calculateDistanceToApple(animal, apple);
            if (/*distance != Float.NaN && */distance < nearestDistance) {
                nearestDistance = distance;
                nearestApple = apple;
            }
        }

        try {
            /*if (nearestApple == null) {
                value = Float.NaN;
                return;
            } else {
                value = nearestDistance;
                return;
            } */
            value = Math.min(1f, nearestDistance / Experiment.MAX_VIEW_DISTANCE);
        } finally {
            if (nearestApple != null) {
                for (final ISensorEventListener sensorEventListener : sensorEventListeners) {
                    sensorEventListener.onSensorEvent(new AppleAtFrontEvent(nearestApple));
                }
            } else {
                for (final ISensorEventListener sensorEventListener : sensorEventListeners) {
                    sensorEventListener.onSensorEvent(new AppleAtFrontEvent(false));
                }
            }
        }
    }

    @Override
    public void addSensorEventListener(final ISensorEventListener iSensorEventListener) {
        if (iSensorEventListener != null) {
            sensorEventListeners.add(iSensorEventListener);
        }
    }

    @Override
    public String toString() {
        return "front";
    }

    @Override
    public String getName() {
        return "front";
    }
}
