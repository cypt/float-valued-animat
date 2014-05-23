package ru.nsu.alife.float_world_impl.actions.actions;

import ru.nsu.alife.float_world_impl.Experiment;
import ru.nsu.alife.float_world_impl.world.Animal;

public class MoveForwardAction extends BaseAction {

    final Animal animal;

    public MoveForwardAction(final Animal animal) {
        this.animal = animal;
    }

    @Override
    public boolean doAction() {
        super.doAction();
        animal.setSpeed(Experiment.MOVE_SPEED);
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        animal.setSpeed(0f);
    }

    @Override
    public String toString() {
        return "move";
    }

    @Override
    public String getName() {
        return "move";
    }
}
