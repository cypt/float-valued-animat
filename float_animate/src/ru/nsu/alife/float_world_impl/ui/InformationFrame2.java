package ru.nsu.alife.float_world_impl.ui;

import ru.nsu.alife.core.logic.AnimalController;
import ru.nsu.alife.core.logic.impl.acceptor.AcceptorImpl;

import javax.swing.*;

public class InformationFrame2 extends JFrame {

    {
        setSize(640, 480);
    }

    public InformationFrame2(final AnimalController animalController) {
        final AcceptorImpl acceptor = (AcceptorImpl) animalController.getAcceptors().get(0);
        final StatisticsCubesHolderPanel panel = (new StatisticsCubesHolderPanel(acceptor,
                acceptor.getStatisticCubesHolder(), acceptor.getSensorsProvider(), acceptor.getActionsProvider()));
        acceptor.getStatisticCubesHolder().addUpdateStatisticListener(panel.getUpdateStatisticListener());
        panel.setSize(640, 480);
        this.getContentPane().add(panel);
    }
}
