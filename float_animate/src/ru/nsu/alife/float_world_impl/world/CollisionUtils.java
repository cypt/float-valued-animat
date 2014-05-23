package ru.nsu.alife.float_world_impl.world;

import ru.nsu.alife.float_world_impl.sensors.SensorUtils;

import java.util.Iterator;

public class CollisionUtils {
    private CollisionUtils() {
    }

    public static boolean areCollide(final Animal animal, final Apple apple) {
        //final float animalX = animal.getPositionX();
        //final float animalY = animal.getPositionY();
        final float animalR = animal.getRadius();
        //final float appleX = apple.getPositionX();
        //final float appleY = apple.getPositionY();
        final float appleR = apple.getRadius();
        //final float distanceX = animalX - appleX;
        //final float distanceY = animalY - appleY;
        final float radiusSum = animalR + appleR;
        if (SensorUtils.calculateDistanceToApple(animal, apple) < radiusSum) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean areCollide(final Apple apple1, final Apple apple2) {
        final float apple1X = apple1.getPositionX();
        final float apple1Y = apple1.getPositionY();
        final float apple1R = apple1.getRadius();
        final float apple2X = apple2.getPositionX();
        final float apple2Y = apple2.getPositionY();
        final float apple2R = apple2.getRadius();
        final float distanceX = apple1X - apple2X;
        final float distanceY = apple1Y - apple2Y;
        final float radiusSum = apple1R + apple2R;
        if (Math.sqrt(distanceX * distanceX + distanceY * distanceY) < radiusSum) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean areCollide(final Animal animal, final Boundaries boundaries) {
        //hax
        return true;
    }

    public static boolean removeContiguousApples(final World world) {
        final Animal animal = world.getAnimal();

        boolean result = false;

        Iterator<Apple> appleIterator = world.getApples().iterator();

        int applesRemoved = 0;

        while (appleIterator.hasNext()) {
            if (areCollide(animal, appleIterator.next())) {
                result = true;
                appleIterator.remove();
                applesRemoved++;
            }
        }
        
        for (int i = 0; i < applesRemoved; i++) {
            world.addApple();
        }
        return result;
    }
}
