package ru.nsu.alife.float_world_impl.world;

public class AnimalWithBoundariesCollisionEventListener implements ICollisionEventListener {
    @Override
    public void onCollisionDetected(final IObject o1, final IObject o2) {
        Animal animal = null;
        Boundaries boundaries = null;

        if (o1 instanceof Animal && o2 instanceof Boundaries) {
            boundaries = (Boundaries) o2;
            animal = (Animal) o1;
        } else if (o1 instanceof Boundaries && o1 instanceof Animal) {
            boundaries = (Boundaries) o1;
            animal = (Animal) o2;
        } else {
            return;
        }

        final float animalPositionX = animal.getPositionX();
        final float animalPositionY = animal.getPositionY();
        final float animalRadius = animal.getRadius();

        if (boundaries.left > (animalPositionX - animalRadius)) {
            animal.setPositionX(boundaries.left + animalRadius);
        }

        if (boundaries.right < (animalPositionX + animalRadius)) {
            animal.setPositionX(boundaries.right - animalRadius);
        }

        if (boundaries.top > (animalPositionY - animalRadius)) {
            animal.setPositionY(boundaries.top + animalRadius);
        }

        if (boundaries.bottom < (animalPositionY + animalRadius)) {
            animal.setPositionY(boundaries.bottom - animalRadius);
        }

    }
}
