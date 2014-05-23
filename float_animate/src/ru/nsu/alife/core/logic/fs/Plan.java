package ru.nsu.alife.core.logic.fs;

import java.util.ArrayList;
import java.util.List;

public class Plan {
    private final IRule rule;
    private final FS fs;
    private double minProbability;
    private int length = 0;      //How many actions should we execute before we reach final goal
    private static final double LAMBDA = 0.95;  //Goal depth discount rate

    private List<Double> probabilityChain = new ArrayList<Double>();

    public Plan(final IRule rule, final FS fs, final double probability) {
        this.rule = rule;
        this.fs = fs;
        this.minProbability = probability;
        this.probabilityChain.add(probability);
    }

    /**
     * We keeping least probable action in chain and updating plan length
      * @param nextActionProb
     */
    public final void updateProbabilityWithNextAction(double nextActionProb) {
        this.probabilityChain.add(nextActionProb);
        this.minProbability = StrictMath.min(this.minProbability, nextActionProb);
        this.length++;
    }

    public IRule getRule() {
        return rule;
    }

    public FS getFs() {
        return fs;
    }

    public double getMinProbability() {
        return this.minProbability;
    }

    /**
     * This function estimates the score to compare several plans
     * Score is plan probability discounted with lambda for each action in chain
     * @return
     */
    public double getScore(){
        return StrictMath.pow(LAMBDA, length)*this.getMinProbability();
    }
}


