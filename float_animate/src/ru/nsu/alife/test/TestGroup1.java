package ru.nsu.alife.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.nsu.alife.float_world_impl.Experiment;
import ru.nsu.alife.core.LoggerHolder;
import ru.nsu.alife.float_world_impl.actions.actions.MoveForwardAction;
import ru.nsu.alife.core.logic.AnimalController;
import ru.nsu.alife.float_world_impl.AnimalSatisfiedGoal;
import ru.nsu.alife.float_world_impl.world.Animal;
import ru.nsu.alife.float_world_impl.world.AnimalCollisionDetector;
import ru.nsu.alife.float_world_impl.world.Apple;
import ru.nsu.alife.float_world_impl.world.World;
import ru.nsu.alife.float_world_impl.sensors.AngleSensor;
import ru.nsu.alife.core.utils.DoubleUtils;
import ru.nsu.alife.core.logic.fs.IAction;
import ru.nsu.alife.test.stuff.AppleFactoryImpl;

import java.util.ArrayList;
import java.util.Iterator;

public class TestGroup1 {

    private static final int TEST_TIME_1 = 1000;

    private static final float ANIMAL_X = 100f;
    private static final float ANIMAL_Y = 100f;

    private static final float[][] applePositions1 = new float[][]{{ANIMAL_X - 15f, ANIMAL_Y}, {ANIMAL_X + 16f,
            ANIMAL_Y}, {ANIMAL_X, ANIMAL_Y + 16f}};

    private static final float[][] applePositions2 = new float[][]{{ANIMAL_X - 16f, ANIMAL_Y}, {ANIMAL_X + 15f,
            ANIMAL_Y}, {ANIMAL_X, ANIMAL_Y + 16f}};

    private static final float[][] applePositions3 = new float[][]{{ANIMAL_X - 17f, ANIMAL_Y}, {ANIMAL_X + 17f,
            ANIMAL_Y}, {ANIMAL_X + 7f, ANIMAL_Y + 7f}};

    private static final float[][] applePositions4 = new float[][]{{ANIMAL_X - 15f, ANIMAL_Y}, {ANIMAL_X + 150f,
            ANIMAL_Y}, {ANIMAL_X, ANIMAL_Y + 16f}};

    private World world;
    private AnimalController animalController;

    private void setupAppleCoordinates(final World world, final float[][] coordinates) {
        world.getApples().clear();
        for (int i = 0; i < coordinates.length; i++) {
            final Apple apple = new Apple(coordinates[i][0], coordinates[i][1], Experiment.APPLE_RADIUS);
            world.addApple(apple);
        }
    }

    @Before
    public void prepareModel() {
        world = new World(0f, 0f, 100000f, 300f);

        final Animal animal = new Animal(Experiment.ANIMAL_RADIUS);
        animal.setPositionX(ANIMAL_X);
        animal.setPositionY(ANIMAL_Y);
        /*
         * ^y
         * |
         *  ---->
         *      ->x
         */
        animal.setRotationAngle(0f);
        world.putAnimal(animal);
        world.setAppleFactory(new AppleFactoryImpl());

    }

    @Test
    public void testAngleSensor() {
        setupAppleCoordinates(world, applePositions1);
        final AngleSensor angleSensor = new AngleSensor(world, world.getAnimal());
        angleSensor.updateValue();
        Assert.assertTrue(DoubleUtils.equals(angleSensor.getValue(), 0.5f));
        setupAppleCoordinates(world, applePositions2);
        angleSensor.updateValue();
        Assert.assertTrue(DoubleUtils.equals(angleSensor.getValue(), 0f));
        setupAppleCoordinates(world, applePositions3);
        angleSensor.updateValue();
        Assert.assertTrue(DoubleUtils.equals(angleSensor.getValue(), 0.875f));
        world.getAnimal().setRotationAngle((float) Math.PI / 4);
        angleSensor.updateValue();
        Assert.assertTrue(DoubleUtils.equals(angleSensor.getValue(), 0.750f));
        setupAppleCoordinates(world, applePositions3);
        world.getAnimal().setRotationAngle(0.17f);
        angleSensor.updateValue();
        Assert.assertTrue(DoubleUtils.equals(angleSensor.getValue(), 0.95539816f));
        world.getAnimal().setRotationAngle(0.17f);
        setupAppleCoordinates(world, applePositions1);
        world.getAnimal().setRotationAngle((float) Math.PI);
        angleSensor.updateValue();
        Assert.assertTrue(DoubleUtils.equals(angleSensor.getValue(), 0f));
    }

    @Test
    public void testLearning() {

        setupAppleCoordinates(world, applePositions4);

        animalController = new AnimalController(new ArrayList<IAction>() {
            {
                add(new MoveForwardAction(world.getAnimal()));
            }

            ;
        }, Experiment.createSensors(world, world.getAnimal(), null), false);
        animalController.setupMainGoal(new AnimalSatisfiedGoal(world));
        animalController.setEnableSaving(false);
        final AnimalCollisionDetector animalCollisionDetector = new AnimalCollisionDetector(world);
        //first action is move forward action

        final long time = (long) ((animalController.getSensors().get(0)
                .getValue() * Experiment.MAX_VIEW_DISTANCE - Experiment.APPLE_RADIUS -
                Experiment.APPLE_RADIUS / 2) / Experiment.MOVE_SPEED);
        animalController.launchAction(animalController.getActions().get(0), time);
        world.getAnimal().setRotationAngle(0);
        animalController.setRandomStateEnds(Long.MAX_VALUE);

        for (long timestamp = 0; true; timestamp += Experiment.TIME_DELTA) {
            LoggerHolder.logger.debug("timestamp " + timestamp);
            LoggerHolder.logger.debug("world update");
            world.update(Experiment.TIME_DELTA);
            //animalCollisionDetector.checkCollision();
            LoggerHolder.logger.debug("animalController update");
            animalController.update(timestamp);


            LoggerHolder.logger.debug("remove apples");
            final float animalX = world.getAnimal().getPositionX();

            final Iterator<Apple> it = world.getApples().iterator();
            int remCount = 0;

            while (it.hasNext()) {
                if (it.next().getPositionX() < animalX - 60) {
                    it.remove();
                    remCount ++;
                }

            }

            for (int i = 0; i < remCount; i++) {
                world.addApple();
            }
        }
    }

    @Test
    public void testDistanceSensor() {
        //final Dista
    }
}
