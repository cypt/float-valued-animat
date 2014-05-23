package ru.nsu.alife.float_world_impl.world;

import java.awt.*;

public class Boundaries implements IObject {

    private static final Stroke borderStroke = new BasicStroke(2f);

    final float left;
    final float right;
    final float top;
    final float bottom;

    public Boundaries(final float top, final float left, final float right, final float bottom) {
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.top = top;
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

    @Override
    public void update(final long deltaTime) {
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setStroke(borderStroke);
        graphics2D.setColor(Color.BLACK);

        final int width = (int) (right - left);
        final int height = (int) (bottom - top);

        graphics2D.drawRect((int) left, (int) top, (int) (left + width), (int) (top + height));
    }
}
