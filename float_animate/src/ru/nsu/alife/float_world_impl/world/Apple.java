package ru.nsu.alife.float_world_impl.world;

import java.awt.*;

public class Apple implements IObject {

    private static final Color DEFAULT_COLOR = Color.red;

    private final float positionX;
    private final float positionY;
    private final float radius;

    private Color color = DEFAULT_COLOR;

    public Apple(float positionX, float positionY, float radius) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void draw(final Graphics2D graphics2D) {
        graphics2D.setColor(color);
        graphics2D.setBackground(color);
        graphics2D.fillOval((int) (positionX - radius), (int) (positionY - radius), (int) (2 * radius),
                (int) (2 * radius));
    }

    public void setColor(final Color color) {
        this.color = color;
    }

    @Override
    public void update(long deltaTime) {
        this.color = DEFAULT_COLOR;
    }
}
