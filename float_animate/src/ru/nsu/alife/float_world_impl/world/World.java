package ru.nsu.alife.float_world_impl.world;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class World implements IObject {

    private IAppleFactory appleFactory;
    private final List<Apple> apples = new ArrayList<Apple>();
    private Boundaries boundaries;
    private Animal animal;

    final float left;
    final float right;
    final float top;
    final float bottom;

    public World(final float top, final float left, final float right, final float bottom) {
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.top = top;
    }

    public Boundaries getBoundaries() {
        return boundaries;
    }

    public void setBoundaries(Boundaries boundaries) {
        this.boundaries = boundaries;
    }

    public List<Apple> getApples() {
        return apples;
    }

    public void addApple() {
        if (appleFactory == null) {
            throw new RuntimeException("appleFactory == null");
        }
        addApple(appleFactory.createApple(this));
    }

    public void addApple(final Apple apple) {
        apples.add(apple);
    }

    public void putAnimal(final Animal animal) {
        this.animal = animal;
    }

    public void draw(final Graphics2D graphics2D) {
        graphics2D.setColor(Color.GREEN);

        final int width = (int) (right - left);
        final int height = (int) (bottom - top);

        graphics2D.fillRect((int) left, (int) top, (int) (left + width), (int) (top + height));

        if (boundaries != null) {
            boundaries.draw(graphics2D);
        }

        for (final Apple apple : apples) {
            if (apple != null) {
                apple.draw(graphics2D);
            }
        }

        if (animal != null) {
            animal.draw(graphics2D);
        }
    }

    public Animal getAnimal() {
        return animal;
    }

    public boolean removeApple(final Apple apple) {
        return apples.remove(apple);
    }

    public void update(final long deltaTime) {
        animal.update(deltaTime);
        for (final Apple apple : apples) {
            apple.update(deltaTime);
        }
    }

    public float getLeft() {
        return left;
    }

    public float getRight() {
        return right;
    }

    public float getTop() {
        return top;
    }

    public float getBottom() {
        return bottom;
    }

    public void setAppleFactory(final IAppleFactory appleFactory) {
        this.appleFactory = appleFactory;
    }
}
