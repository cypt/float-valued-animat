package ru.nsu.alife.float_world_impl.world;

import java.awt.*;

public interface IObject {
    public void update(final long deltaTime);

    public void draw(final Graphics2D graphics2D);
}
