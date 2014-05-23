package ru.nsu.alife.test.stuff;

import ru.nsu.alife.float_world_impl.Experiment;
import ru.nsu.alife.float_world_impl.world.Apple;
import ru.nsu.alife.float_world_impl.world.IAppleFactory;
import ru.nsu.alife.float_world_impl.world.World;

import java.util.Random;

public class AppleFactoryImpl implements IAppleFactory {
    Random r = new Random();
    @Override
    public Apple createApple(final World world) {
        float appleX = world.getAnimal().getPositionX() + r.nextFloat() * 300f + Experiment.ANIMAL_RADIUS +
                Experiment.APPLE_RADIUS + 10f;
        float appleY = world.getAnimal().getPositionY() + (-100f + 200f * r.nextFloat());
        Apple apple = new Apple(appleX, appleY , Experiment.APPLE_RADIUS);
        return apple;
    }
}
