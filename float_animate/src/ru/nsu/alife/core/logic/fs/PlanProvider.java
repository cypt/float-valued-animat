package ru.nsu.alife.core.logic.fs;

import ru.nsu.alife.core.LoggerHolder;
import ru.nsu.alife.core.logic.impl.acceptor.AcceptorImpl;
import ru.nsu.alife.core.logic.impl.statistics.VolumeGoalImpl;
import ru.nsu.alife.core.logic.impl.statistics.volumes.IVolume;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlanProvider {
    private Context context;

    public PlanProvider(final Context context) {
        this.context = context;
    }

    private float[] sensorValues;

    /**
     * Takes the best of 1-step rules
     * Then takes the best of subFS rules
     * Comparing al together and returns best score
     * @return
     */

    public Plan getPlan() {
        //We need acceptor of this FS to get it's best local rule
        final AcceptorImpl acceptor = (AcceptorImpl) context.getAcceptor();

        //We ned sensor values to estimate current state
        sensorValues = Utils.getSensorValues(acceptor.getSensorsProvider());

        //Getting rule from acceptor, if it returns null, we initialize best local rule probability as 0
        double bestSimpleProbability = 0;
        IRule simpleRule = acceptor.getRule(sensorValues);
        if (simpleRule != null) {
            bestSimpleProbability = simpleRule.getProbability();
        }

        //Now we want to ask our subFS about their plans
        Set<Map.Entry<IRule, FS>> setOfCombinations = context.getSubFs().entrySet();

        //Initialize zero plan that will be returned if we will not find any better
        Plan bestPlan = new Plan(null, null, 0);

        for (final Map.Entry<IRule, FS> combination : setOfCombinations) {
            LoggerHolder.log((VolumeGoalImpl) combination.getValue().getGoal());
            LoggerHolder.logger.debug("\n");
        }

        //Here we ask every subFS for plan, then extend it with action of current subFS and keep best of them in
        //bestPlan variable
        for (final Map.Entry<IRule, FS> combination : setOfCombinations) {
            final IVolume volume = combination.getKey().getSituationDescription();
            Plan p = combination.getValue().getPlan();
            // Our plan needs one more action to goal. So we extend it with probability of action, associated with
            // current subFS
            p.updateProbabilityWithNextAction(volume.getStatistic().getProbability());

            //Keeping best plan
            if (p.getScore() > bestPlan.getScore()) {
                bestPlan = p;
            }
        }

        // If we found any subFS plan better than local, we return it
        if (bestPlan.getScore() > bestSimpleProbability) {
            return bestPlan;
        }
        // Else if we have any good local plan we return it
        else if (bestSimpleProbability != 0) {
            return new Plan(simpleRule, null, bestSimpleProbability);
        }

        // Return empty plan with null probability
        return new Plan(null, null, 0);
    }

}
