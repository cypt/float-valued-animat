package ru.nsu.alife.float_world_impl.sensors;

import ru.nsu.alife.float_world_impl.world.Animal;
import ru.nsu.alife.float_world_impl.world.Apple;

public class SensorUtils {

    private SensorUtils() {
    }

    public static float calculateDistanceToApple(final Animal animal, final Apple apple, final int maxDistance) {
        final float appleX = apple.getPositionX();
        final float appleY = apple.getPositionY();
        final float angle = animal.getRotationAngle();
        final float translatedAppleX = appleX - animal.getPositionX();
        final float translatedAppleY = appleY - animal.getPositionY();
        final float rotatedAppleX = translatedAppleX * (float) Math.cos(-angle) - translatedAppleY * (float) Math
                .sin(-angle);
        final float rotatedAppleY = translatedAppleX * (float) Math.sin(-angle) + translatedAppleY * (float) Math
                .cos(-angle);

        // we check if apple intersected with animal view zone
        if (Math.abs(rotatedAppleY) < (apple.getRadius() + animal.getRadius())) {
            final float distanceByX = rotatedAppleX;
            if (distanceByX < 0) {
                return Float.NaN;
            } else if (distanceByX < maxDistance) {
                return (distanceByX) / maxDistance;
            } else {
                return Float.NaN;
            }
        }

        return Float.NaN;
    }

    public static float calculateDistanceToApple(final Animal animal, final Apple apple) {
        final float appleX = apple.getPositionX();
        final float appleY = apple.getPositionY();

        final float translatedAppleX = appleX - animal.getPositionX();
        final float translatedAppleY = appleY - animal.getPositionY();

        double dist = Math.sqrt(translatedAppleX * translatedAppleX + translatedAppleY * translatedAppleY);

        return (float) dist;
    }

    public static int getDiscreteValue(float value, int discreteValuesCount) {
        final float partSize = 1.f / discreteValuesCount;

        if (value == Float.NaN) {
            throw new RuntimeException();
        }

        int discreteValue;

        if (value != 1.f) {
            discreteValue = (int) (value / partSize);
        } else {
            discreteValue = discreteValuesCount - 1;
        }

        return discreteValue;
    }

}
