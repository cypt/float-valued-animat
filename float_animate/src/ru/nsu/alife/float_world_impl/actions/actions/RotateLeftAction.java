package ru.nsu.alife.float_world_impl.actions.actions;

import ru.nsu.alife.float_world_impl.Experiment;
import ru.nsu.alife.float_world_impl.world.Animal;


public class RotateLeftAction extends BaseAction {

    private final Animal animal;

    public RotateLeftAction(final Animal animal) {
        this.animal = animal;
    }

    @Override
    public boolean doAction() {
        super.doAction();
        animal.setRotationSpeed(Experiment.ROTATE_LEFT_SPEED);
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        animal.setRotationSpeed(0f);
    }

    @Override
    public String toString() {
        return "rot_left";
    }

    @Override
    public String getName() {
        return "rot_left";
    }
}
