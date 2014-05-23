package ru.nsu.alife.core.logic.fs;

import org.apache.log4j.Level;
import ru.nsu.alife.core.LoggerHolder;

import java.util.HashMap;
import java.util.Map;

public class FS {
    private static final String TAG = FS.class.getName();

    public static final int MAX_DEPTH = 3;

    private Context context;

    private final IGoal goal;

    private final IAcceptor acceptor;

    private final IAcceptorFactory acceptorFactory;

    private IAcceptorsHandler acceptorsHandler;

    private FsCallback inputCallback;

    private Plan selectedPlan;

    private PlanProvider planProvider;

    public FS(
            final IGoal goal, int depth, final IAcceptorFactory acceptorFactory,
            final IAcceptorsHandler acceptorsHandler) {
        LoggerHolder.logger.log(Level.DEBUG, "new FS " + goal);

        this.goal = goal;
        this.acceptorFactory = acceptorFactory;
        this.acceptor = acceptorFactory.getAcceptor(goal);

        context = Context.createContext(this, acceptor, depth);
        this.planProvider = new PlanProvider(context);

        final AcceptorListener acceptorListener = new AcceptorListener() {
            @Override
            public void onResult(final boolean success, final boolean executor) {
                LoggerHolder.logger.log(Level.DEBUG,
                        "With rule " + FS.this.selectedPlan + " acceptorListener.onResult " + success);
                if (executor) {
                    LoggerHolder.logger.debug("XXX FS goal state " + success + " rule was " + selectedPlan.getRule());
    //                planProvider.correct(selectedPlan, success);
                    FS.this.inputCallback.onResult(success);
                }
            }
        };

        acceptor.addAcceptorListener(acceptorListener);

        this.acceptorsHandler = acceptorsHandler;
        acceptorsHandler.addAcceptor(acceptor);
    }

    public void updateSubFsList() {
        LoggerHolder.logger.debug("updating subsystems for FS " + this + " at depth " + context.getDepth());
        context.getSubFs().clear();
        HashMap<IRule, IGoal> goals = acceptor.generateGoals();
        for (final Map.Entry<IRule, IGoal> entry : goals.entrySet()) {
            final FS newFS = new FS(entry.getValue(), context.getDepth() + 1, acceptorFactory, acceptorsHandler);
            LoggerHolder.logger.debug("created new FS " + newFS + " fro rule " + entry.getKey());
            context.getSubFs().put(entry.getKey(), newFS);
            newFS.init();
        }
    }

    public double predictProbability() {
        selectedPlan = planProvider.getPlan();
        LoggerHolder.logger.debug("Plan for level " + context.getDepth());
        LoggerHolder.log(selectedPlan);
        if (selectedPlan == null) {
            return 0;
        } else {
            return selectedPlan.getMinProbability();
        }
    }

    public Plan getPlan() {
        selectedPlan = planProvider.getPlan();
        return selectedPlan;
    }

    public void reachGoal(final FsCallback inputCallback) {
        LoggerHolder.logger.log(Level.DEBUG, "reach goal");

        this.inputCallback = inputCallback;

        if (selectedPlan.getFs() != null) {
            final FsCallback outputCallback = new FsCallback() {
                @Override
                public void onResult(boolean success) {
                    if (success) {
                        if (selectedPlan.getRule().execute(acceptor)) {
                            return;
                        }
                    }
                    inputCallback.onResult(false);
                }
            };
            selectedPlan.getFs().reachGoal(outputCallback);
        } else {
            if (!selectedPlan.getRule().execute(acceptor)) {
                inputCallback.onResult(false);
            }
        }
    }

    public String toString() {
        return goal.toString();
    }

    public IGoal getGoal() {
        return goal;
    }

    public IAcceptor getAcceptor() {
        return acceptor;
    }

    public void init() {
        acceptor.launchLearnOnHistory();
        if (context.getDepth() < MAX_DEPTH) {
            updateSubFsList();
        }
    }

    public void recursiveUpdate() {
        if (context.getDepth() < MAX_DEPTH) {
            updateSubFsList();
            for (FS fs :context.getSubFs().values()) {
                fs.recursiveUpdate();
            }
        }
    }
}
