package ru.nsu.alife.float_world_impl.sensors;

import ru.nsu.alife.float_world_impl.world.Animal;
import ru.nsu.alife.float_world_impl.world.Apple;
import ru.nsu.alife.float_world_impl.world.World;
import ru.nsu.alife.core.sensors.ISensor;
import ru.nsu.alife.core.sensors.ISensorEventListener;

import java.util.ArrayList;
import java.util.List;

public class AngleSensor implements ISensor {

    public final class SideApplesDetectionEvent implements ISensorEventListener.SensorEvent {
        public static final String SIDE_APPLES_DETECTION_EVENT = "SIDE_APPLES_DETECTION_EVENT";

        private final Apple apple;
        private final float rotationShift;
        private final boolean detected;

        private SideApplesDetectionEvent(final Apple apple, final float rotationShift) {
            this.apple = apple;
            this.rotationShift = rotationShift;
            this.detected = true;
        }

        private SideApplesDetectionEvent(boolean detected) {
            this.detected = detected;
            this.rotationShift = Float.NaN;
            this.apple = null;
        }

        public float getRotationShift() {
            return rotationShift;
        }

        public Apple getApple() {
            return apple;
        }

        @Override
        public Animal getAnimal() {
            return AngleSensor.this.animal;
        }

        @Override
        public String getEventType() {
            return SIDE_APPLES_DETECTION_EVENT;
        }

        public ISensor getSensor() {
            return AngleSensor.this;
        }

        public boolean isDetected() {
            return detected;
        }
    }

    private final World world;
    private final Animal animal;

    private float value = Float.NaN;

    private final ArrayList<ISensorEventListener> sensorEventListeners = new ArrayList<ISensorEventListener>();

    public AngleSensor(World world, Animal animal) {
        this.world = world;
        this.animal = animal;
    }

    private float getRotationShift(final Apple apple) {
        final float rotationAngle = animal.getRotationAngle();
        final float animalRadius = animal.getRadius();

        final float animalViewVectorX = animalRadius * (float) Math.cos(rotationAngle);
        final float animalViewVectorY = animalRadius * (float) Math.sin(rotationAngle);

        final float connectionVectorX = apple.getPositionX() - animal.getPositionX();
        final float connectionVectorY = apple.getPositionY() - animal.getPositionY();

        final float vectorMul = (animalViewVectorX) * (connectionVectorY) - (animalViewVectorY) * (connectionVectorX);
        final float connectionVectorLength = (float) Math
                .sqrt(connectionVectorX * connectionVectorX + connectionVectorY * connectionVectorY);

        final float cosBetween = (connectionVectorX * animalViewVectorX + connectionVectorY * animalViewVectorY) /
                (connectionVectorLength * animalRadius);
        float angle = (float) Math.acos(cosBetween);
        if (vectorMul > 0) {
            angle *= -1;
        }
        return angle;
    }

    public float getValue() {
        return value;
    }

    public void updateValue() {
        final List<Apple> apples = world.getApples();
        Apple nearestApple = null;
        float nearestDistance = Float.MAX_VALUE;

        for (final Apple apple : apples) {
            float distance = SensorUtils.calculateDistanceToApple(animal, apple);
            if (nearestDistance > distance) {
                nearestApple = apple;
                nearestDistance = distance;
            }
        }

        float tan = nearestApple.getPositionX() - animal.getPositionX() / (nearestApple.getPositionY() - animal
                .getPositionY());

        float nearestRotationShift = getRotationShift(nearestApple);

        try {
            if (nearestRotationShift < 0) {
                value = (2 * (float) Math.PI + nearestRotationShift) / (float) (2 * Math.PI);
            } else {
                value = nearestRotationShift / (float) (2 * Math.PI);
            }
            return;
        } finally {
            if (nearestApple != null) {
                for (final ISensorEventListener sensorEventListener : sensorEventListeners) {
                    sensorEventListener.onSensorEvent(new SideApplesDetectionEvent(nearestApple, nearestRotationShift));
                }
            } else {
                for (final ISensorEventListener sensorEventListener : sensorEventListeners) {
                    sensorEventListener.onSensorEvent(new SideApplesDetectionEvent(false));
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
        return "rear";
    }

    @Override
    public String getName() {
        return "rear";
    }
}
