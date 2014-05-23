package ru.nsu.alife.float_world_impl.world;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static ru.nsu.alife.float_world_impl.Experiment.*;

public final class AppleFactory implements IAppleFactory {
    private static final Random random = new Random();

    @Override
    public Apple createApple(final World world) {
        final Animal animal = world.getAnimal();
        final List<Apple> apples = world.getApples();
        boolean appleCreated = false;

        final float zoneLeft = world.getLeft() + APPLE_RADIUS;
        final float zoneRight = world.getRight() - APPLE_RADIUS;
        final float zoneWidth = zoneRight - zoneLeft;
        final float zoneTop = world.getTop() + APPLE_RADIUS;
        final float zoneBottom = world.getBottom() - APPLE_RADIUS;
        final float zoneHeight = zoneBottom - zoneTop;
        /*System.out.println("zoneLeft " + zoneLeft);
        System.out.println("zoneRight " + zoneRight);
        System.out.println("zoneTop " + zoneTop);
        System.out.println("zoneBottom " + zoneBottom);  */
        while (!appleCreated) {
            final float posX = random.nextFloat() * zoneWidth + zoneLeft;
            final float posY = random.nextFloat() * zoneHeight + zoneTop;
            final Apple newApple = new Apple(posX, posY, APPLE_RADIUS);
            //LoggerHolder.log("apple created: " + posX + " " + posY);
            if (!CollisionUtils.areCollide(animal, newApple)) {
                final Iterator<Apple> iterator = apples.iterator();
                boolean collide = false;
                while (iterator.hasNext()) {
                    if (CollisionUtils.areCollide(newApple, iterator.next())) {
                        collide = true;
                        break;
                    }
                }
                if (!collide) {
                    appleCreated = true;
                    return newApple;
                }
            }
        }
        return null;
    }
}
