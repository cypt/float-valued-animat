package ru.nsu.alife.float_world_impl.ui;

import ru.nsu.alife.float_world_impl.world.IObject;

import javax.swing.*;
import java.awt.*;

public class WorldView extends JPanel {

    private volatile IObject world;

    public void setWorld(final IObject world) {
        this.world = world;
    }

    @Override
    public synchronized void paint(final Graphics g) {
        super.paint(g);
        world.draw((Graphics2D) g);
    }
}
