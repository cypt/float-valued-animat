package ru.nsu.alife.float_world_impl.world;

import java.awt.*;

public class Animal implements IObject {

    private float positionX;
    private float positionY;
    private float rotationAngle;
    private float speed;
    private float rotationSpeed;
    private float radius;

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public float getRotationAngle() {
        return rotationAngle;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public void setRotationAngle(final float rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(final float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public Animal(final float positionX, final float positionY, final float rotationAngle, final float radius) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.rotationAngle = rotationAngle;
        this.radius = radius;
    }

    public Animal(final float radius) {
        this.radius = radius;
    }

    public void update(final long deltaTime) {
        positionX += speed * Math.cos(rotationAngle) * deltaTime;
        positionY += speed * Math.sin(rotationAngle) * deltaTime;
        rotationAngle += rotationSpeed * deltaTime;
    }

    public void draw(final Graphics2D graphics2D) {
        graphics2D.setColor(Color.BLUE);
        graphics2D.setBackground(Color.GREEN);
        graphics2D.fillOval((int) (positionX - radius), (int) (positionY - radius), (int) (2 * radius),
                (int) (2 * radius));
        final int rotationMarkX = (int) (positionX + radius * (float) Math.cos(rotationAngle));
        final int rotationMarkY = (int) (positionY + radius * (float) Math.sin(rotationAngle));
        graphics2D.setColor(Color.RED);
        graphics2D.drawLine((int) (positionX), (int) (positionY), rotationMarkX, rotationMarkY);
    }

    public float getRadius() {
        return radius;
    }
}
