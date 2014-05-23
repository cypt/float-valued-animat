package ru.nsu.alife.core.logic;

import org.apache.log4j.Level;
import ru.nsu.alife.core.LoggerHolder;
import ru.nsu.alife.core.logic.fs.*;
import ru.nsu.alife.core.logic.impl.acceptor.AcceptorFactoryImpl;
import ru.nsu.alife.core.logic.impl.history.HistoryElement;
import ru.nsu.alife.core.logic.impl.history.HistoryHolder;
import ru.nsu.alife.core.logic.impl.statistics.CubeUtils;
import ru.nsu.alife.core.sensors.ISensor;
import ru.nsu.alife.float_world_impl.Experiment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnimalController implements IActionsProvider, ISensorsProvider, IAcceptorsHandler, IAnimalController {

    private static final String FILE_HISTORY_RANDOM = "random.his";

    private static final Random random = new Random();
    public static final float PROBABILITY_THRESHOLD = 0.4f;
    private int goalCount = 0;

    private final ArrayList<IAction> actions;
    private final ArrayList<ISensor> sensors;
    private final ArrayList<IAcceptor> acceptors;

    private HistoryHolder historyHolder;

    private AcceptorFactoryImpl acceptorFactory;

    private FS rootFS;
    private FsCallback fsCallback;

    private IAction currentActiveAction;
    private long currentActiveActionEnds;
    private IAcceptor executor;

    private long currentTime;

    private boolean randomState;
    private long randomStateEnds = -1;
    private boolean rootFsIsInited = false;

    private boolean enableSaving;
    private boolean justStarted = true;
    private boolean prognosedActionPerforming;

    private IAnimalControllerListener animalControllerListener;

    public AnimalController(
            final ArrayList<IAction> actions, final ArrayList<ISensor> sensors, final boolean initHolderFromFile) {
        this.actions = new ArrayList<IAction>(actions);
        this.sensors = new ArrayList<ISensor>(sensors);
        this.acceptors = new ArrayList<IAcceptor>();

        if (initHolderFromFile) {
            historyHolder = HistoryHolder.restoreHistoryHolder(FILE_HISTORY_RANDOM);
        }

        if (historyHolder == null) {
            historyHolder = new HistoryHolder();
        }

        this.acceptorFactory = new AcceptorFactoryImpl(this, this, historyHolder);
    }

    private void performPrognosedAction() {
        Plan plan = rootFS.getPlan();
        if (plan.getMinProbability() > PROBABILITY_THRESHOLD) {
            LoggerHolder.logger.debug("XXX plan with probability " + plan.getMinProbability() + " will be launched");
            System.out.println("XXX plan with probability " + plan.getMinProbability() + " will be launched. Score: " + plan.getScore());
            prognosedActionPerforming = true;
            rootFS.reachGoal(fsCallback);
        } else {
            LoggerHolder.logger.debug("XXX random action will be launched");
            performRandomAction();
        }
    }

    public void setupMainGoal(final IGoal mainGoal) {
        rootFS = new FS(mainGoal, 1, acceptorFactory, this);
        fsCallback = new FsCallback() {
            @Override
            public void onResult(boolean success) {
                LoggerHolder.logger.log(Level.DEBUG, "goal " + mainGoal + " result " + success);
                prognosedActionPerforming = false;
            }
        };
    }

    private void performSomeAction() {
        if (randomState) {
            LoggerHolder.logger.debug("performSomeAction randomState");
            performRandomAction();
        } else {
            LoggerHolder.logger.debug("performPrognosedAction");
            performPrognosedAction();
        }
    }

    public void update(final long currentTime) {
        this.currentTime = currentTime;

        updateRandomState(currentTime);

        if (checkIfJustStarted()) return;

        final boolean endOfAction = currentTime >= currentActiveActionEnds;
        final HistoryElement historyElement = new HistoryElement(currentTime, getFreshSensorValues(),
                getActiveActionIndex(actions), endOfAction);

        historyHolder.add(historyElement);
        LoggerHolder.log(sensors, actions);

        if (endOfAction) {

            if (!randomState && enableSaving) {
                LoggerHolder.logger.debug("stopSaving");
                historyHolder.stopSaving();
                enableSaving = false;
            }

            if (!randomState && !rootFsIsInited) {
                this.rootFS.init();
                rootFsIsInited = true;
            }

            LoggerHolder.logger.debug("endOfAction");
            for (final IAcceptor acceptor : acceptors) {
                LoggerHolder.logger.debug("updating acceptor " + acceptor);
                acceptor.update(currentTime);
                if (!randomState) {
                    acceptor.learn();
                }
            }

            if (!randomState) {
                LoggerHolder.logger.debug("!randomState");
                final List<IAcceptor> workingCopy = (List<IAcceptor>) acceptors.clone();

                for (final IAcceptor acceptor : workingCopy) {
                    LoggerHolder.logger.debug("invoke fs of acceptor " + acceptor);
                    acceptor.invokeFs(acceptor == executor);
                }
            }

            if (!prognosedActionPerforming) {
                performSomeAction();
            }
            this.goalCount += 1;
            if (this.goalCount > 100) {
                this.goalCount = 0;
                this.rootFS.recursiveUpdate();
            }
        }
    }

    private boolean checkIfJustStarted() {
        if (justStarted) {
            if (animalControllerListener != null) {
                animalControllerListener.onControllerLaunched();
            }

            if (enableSaving) {
                LoggerHolder.logger.debug("enableSaving");
                historyHolder.beginSaving(FILE_HISTORY_RANDOM, true);
            }

            performSomeAction();
            justStarted = false;
            return true;
        }
        return false;
    }

    private void updateRandomState(long currentTime) {
        boolean newRandomState = randomStateEnds > currentTime;

        if (!newRandomState && randomState) {
            if (animalControllerListener != null) {
                animalControllerListener.onRandomStateFinished();
            }
        }

        randomState = newRandomState;
    }

    @Override
    public ArrayList<ISensor> getSensors() {
        return sensors;
    }

    @Override
    public ArrayList<IAction> getActions() {
        return actions;
    }

    @Override
    public void addAcceptor(final IAcceptor acceptor) {
        acceptors.add(acceptor);
    }

    public ArrayList<IAcceptor> getAcceptors() {
        return acceptors;
    }

    @Override
    public void performAction(final IAcceptor executor, final IAction action, final long duration) {

        for (final IAction anotherAction : actions) {
            anotherAction.stop();
        }

        this.executor = executor;
        currentActiveAction = action;
        currentActiveActionEnds = currentTime + duration;
        LoggerHolder.logger.debug("XXX perform action " + action + " with duration " + duration + " current time " +
                currentTime);

        currentActiveAction.doAction();
    }

    @Override
    public IAction getCurrentAction() {
        return currentActiveAction;
    }

    @Override
    public void removeAcceptor(final IAcceptor acceptor) {
        this.acceptors.remove(acceptor);
    }

    private int getActiveActionIndex(final ArrayList<IAction> actions) {
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i).isActive()) {
                return i;
            }
        }
        return -1;
    }

    private float[] getFreshSensorValues() {
        final float[] sensorsValues = new float[sensors.size()];
        for (int i = 0; i < sensors.size(); i++) {
            sensors.get(i).updateValue();
            sensorsValues[i] = sensors.get(i).getValue();
        }
        return sensorsValues;
    }

    private void performRandomAction() {
        final ArrayList<IAction> actions = this.actions;
        final int size = actions.size();
        final int actionNumber = Math.abs(random.nextInt()) % size;

        final IAction action = actions.get(actionNumber);

        long durationMs = CubeUtils
                .getDurationFromIndexValue(Math.abs(random.nextInt()) % Experiment.ACTION_DIMENSION_SIZES[actionNumber],
                        actionNumber);

        performAction(null, action, durationMs);
        LoggerHolder.logger.debug("XXX perform random action " + action + " with duration " + durationMs);
    }


    public void launchAction(final IAction action, final long time) {
        LoggerHolder.logger.debug("XXX perform random action " + action + " with duration " + time);
        performAction(null, action, time);
    }

    public void setRandomStateEnds(final long randomStateEnds) {
        this.randomStateEnds = randomStateEnds;
    }

    public void setEnableSaving(final boolean enableSaving) {
        this.enableSaving = enableSaving;
    }

    public HistoryHolder getHistoryHolder() {
        return historyHolder;
    }

    public void setAnimalControllerListener(IAnimalControllerListener animalControllerListener) {
        this.animalControllerListener = animalControllerListener;
    }
}

