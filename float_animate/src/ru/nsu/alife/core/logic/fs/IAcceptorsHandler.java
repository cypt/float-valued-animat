package ru.nsu.alife.core.logic.fs;

import ru.nsu.alife.core.logic.fs.IAcceptor;

public interface IAcceptorsHandler {
    public void addAcceptor(final IAcceptor acceptor);

    public void removeAcceptor(final IAcceptor acceptor);
}
