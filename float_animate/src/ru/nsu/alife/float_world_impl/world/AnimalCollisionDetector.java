package ru.nsu.alife.float_world_impl.world;

import java.util.ArrayList;
import java.util.List;

public class AnimalCollisionDetector {
    private final World world;
    private final ArrayList<ICollisionEventListener> collisionEventListeners = new ArrayList<ICollisionEventListener>();

    public AnimalCollisionDetector(World world) {
        this.world = world;
    }

    public boolean addCollisionEventListener(final ICollisionEventListener iCollisionEventListener) {
        return collisionEventListeners.add(iCollisionEventListener);
    }

    public void checkCollision() {
        final List<Apple> apples = new ArrayList<Apple>(world.getApples());
        final Animal animal = world.getAnimal();
        final Boundaries boundaries = world.getBoundaries();
        if (boundaries != null) {
            if (CollisionUtils.areCollide(animal, boundaries)) {
                for (final ICollisionEventListener collisionEventListener : collisionEventListeners) {
                    collisionEventListener.onCollisionDetected(animal, boundaries);
                }
            }
        }

        for (final Apple apple : apples) {         //apple collisions moved to AnimalSatisfiedGoal
            if (CollisionUtils.areCollide(animal, apple)) {
                for (final ICollisionEventListener collisionEventListener : collisionEventListeners) {
                    collisionEventListener.onCollisionDetected(animal, apple);
                }
            }
        }
    }
}
