package ru.nsu.alife.float_world_impl;

import org.apache.log4j.Level;
import ru.nsu.alife.core.LoggerHolder;
import ru.nsu.alife.float_world_impl.actions.actions.MoveForwardAction;
import ru.nsu.alife.float_world_impl.actions.actions.RotateLeftAction;
import ru.nsu.alife.float_world_impl.actions.actions.RotateRightAction;
import ru.nsu.alife.core.logic.fs.IAnimalController;
import ru.nsu.alife.core.logic.AnimalController;
import ru.nsu.alife.core.logic.impl.history.HistoryElement;
import ru.nsu.alife.core.sensors.ISensor;
import ru.nsu.alife.core.sensors.ISensorEventListener;
import ru.nsu.alife.float_world_impl.sensors.AngleSensor;
import ru.nsu.alife.float_world_impl.sensors.DistanceSensor;
import ru.nsu.alife.float_world_impl.ui.InformationFrame2;
import ru.nsu.alife.float_world_impl.ui.VisualisationSensorEventListener;
import ru.nsu.alife.float_world_impl.ui.WorldView;
import ru.nsu.alife.core.logic.fs.IAction;
import ru.nsu.alife.float_world_impl.world.*;

import javax.swing.*;
import java.util.ArrayList;

public class Experiment {
    private static final JFrame frame = new JFrame();
    private final static WorldView worldView = new WorldView();

    private static final int FRAME_WIDTH = 1024;
    private static final int FRAME_HEIGHT = 768;

    private static final JLabel sensorLabel1 = new JLabel();
    private static final JLabel sensorLabel2 = new JLabel();

    public static final float ANIMAL_RADIUS = 30f;
    public static final float APPLE_RADIUS = 25f;
    public static final float MOVE_SPEED = 0.1f;
    public static final float ROTATE_LEFT_SPEED = -0.001f;
    public static final float ROTATE_RIGHT_SPEED = 0.001f;
    public static final int PARTS_PER_DURATION = 80;
    public static final float MAX_VIEW_DISTANCE = 400f;
    public static final long MAXIMUM_ACTION_DURATION = 4000;
    public static final long RANDOM_STATE_LENGTH = (0000 * MAXIMUM_ACTION_DURATION / 8);

    public static final long LIGHTSPEED_STATE_LENGTH = (0000 * MAXIMUM_ACTION_DURATION / 8);
    public static final int TIME_DELTA = 50;
    public static final float BEST_ALGORITHM_THRESHOLD = 0.85f;
    public static final int SLEEP_TIME = 0;
    public static final int TOTAL_RESULTS_TRESHOLD = 10;

    public static final int[][] SENSOR_DIMENSION_SIZES_DS = new int[][]
            {{20, 20}};

    private static long sleepTime;
    public static final int[] ACTION_DIMENSION_SIZES = new int[] {10, 25, 25};

    static {
        initViews();
    }

    public static final float WORLD_SIZE = 400f;

    private Experiment() {
    }

    private static void initViews() {
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        sensorLabel1.setSize(200, 50);
        sensorLabel1.setLocation(10, 610);
        sensorLabel2.setSize(200, 50);
        sensorLabel2.setLocation(240, 610);
        frame.getContentPane().add(sensorLabel1);
        frame.getContentPane().add(sensorLabel2);
        frame.getContentPane().add(worldView);
        worldView.setSize(800, 600);
        worldView.setLocation(10, 10);
        frame.setVisible(true);
    }

    public static ArrayList<ISensor> createSensors(final World world, final Animal animal,
                                                   final ISensorEventListener iSensorEventListener) {
        final ArrayList<ISensor> sensors = new ArrayList<ISensor>();
        final DistanceSensor distanceSensor = new DistanceSensor(world, animal);
        distanceSensor.addSensorEventListener(iSensorEventListener);
        final AngleSensor angleSensor = new AngleSensor(world, animal);
        if (iSensorEventListener != null) {
            angleSensor.addSensorEventListener(iSensorEventListener);
        }
        sensors.add(distanceSensor);
        sensors.add(angleSensor);
        return sensors;
    }

    public static ArrayList<IAction> createActions(final Animal animal) {
        final ArrayList<IAction> actions = new ArrayList<IAction>();
        final MoveForwardAction moveForwardAction = new MoveForwardAction(animal);
        final RotateLeftAction rotateLeftAction = new RotateLeftAction(animal);
        final RotateRightAction rotateRightAction = new RotateRightAction(animal);
        actions.add(moveForwardAction);
        actions.add(rotateLeftAction);
        actions.add(rotateRightAction);
        return actions;
    }

    public static void main(String[] args) {
        LoggerHolder.logger.setLevel(Level.OFF);

        final World world = new World(0f, 0f, WORLD_SIZE, WORLD_SIZE);
        final Animal animal = new Animal(50f, 50f, 0f, ANIMAL_RADIUS);
        final AnimalCollisionDetector animalCollisionDetector = new AnimalCollisionDetector(world);
        animalCollisionDetector.addCollisionEventListener(new AnimalWithBoundariesCollisionEventListener());

        world.putAnimal(animal);
        world.setAppleFactory(new AppleFactory());
        for (int i = 0; i < 4; i++) {
            world.addApple();
        }

        world.setBoundaries(new Boundaries(0f, 0f, WORLD_SIZE, WORLD_SIZE));
        worldView.setWorld(world);

        final IAnimalController animalController = new AnimalController(createActions(animal),
                createSensors(world, animal, new VisualisationSensorEventListener(sensorLabel1,
                        sensorLabel2)),  false);


        ((AnimalController) animalController).setEnableSaving(false);
        ((AnimalController) animalController).setupMainGoal(new AnimalSatisfiedGoal(world));
        ((AnimalController) animalController).setRandomStateEnds(RANDOM_STATE_LENGTH);
        InformationFrame2 informationFrame2 = new InformationFrame2((AnimalController) animalController);
        informationFrame2.setVisible(true);

        HistoryElement lastHistoryElement = ((AnimalController) animalController).getHistoryHolder()
                .getLastElement();

        long timestamp = (lastHistoryElement == null) ? 0 : lastHistoryElement.timestamp;

        sleepTime = 0;


        while (true) {
            if (timestamp > LIGHTSPEED_STATE_LENGTH) {
                sleepTime = 4;
            }
            try {
                Thread.sleep(timestamp > RANDOM_STATE_LENGTH ? sleepTime : 0);

            } catch (InterruptedException interruptedException) {
            }
            synchronized (world) {
                LoggerHolder.logger.debug("timestamp " + timestamp);
                timestamp += TIME_DELTA;
                world.update(TIME_DELTA);
                animalCollisionDetector.checkCollision();
                animalController.update(timestamp);

                if (timestamp > RANDOM_STATE_LENGTH) {
                    sleepTime = SLEEP_TIME;
                }

            }

            worldView.repaint();
        }
    }
}
