package ru.nsu.alife.float_world_impl;

import ru.nsu.alife.core.logic.fs.IGoal;
import ru.nsu.alife.float_world_impl.world.CollisionUtils;
import ru.nsu.alife.float_world_impl.world.World;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class AnimalSatisfiedGoal implements IGoal {
    public static final int PERIOD = 1000;
    private final World world;

    private FileWriter fileWriter;

    private long checkCounter;
    private long foodCounter;

    public AnimalSatisfiedGoal(final World world) {
        this.world = world;

        try {
            fileWriter = new FileWriter("out_" + System.currentTimeMillis() + ".flo");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean check() {
        final boolean result = CollisionUtils.removeContiguousApples(world);
        if (result) foodCounter++;
        checkCounter ++;

        if (checkCounter % PERIOD == 0) {
            try {
                fileWriter.write(checkCounter + " " +String.valueOf(foodCounter) + "\n");
                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkCounter = 0;
            foodCounter = 0;
        }

        return result;
    }

    @Override
    public boolean check(final float[] history) {
        return (history[0] < (Experiment.APPLE_RADIUS + Experiment.ANIMAL_RADIUS) / Experiment.MAX_VIEW_DISTANCE);
    }
}
