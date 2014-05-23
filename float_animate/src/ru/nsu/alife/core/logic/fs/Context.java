package ru.nsu.alife.core.logic.fs;

import java.util.HashMap;

public class Context {

    private static HashMap<FS, Context> contexts = new HashMap<FS, Context>();

    private FS currentFS;

    private HashMap<IRule, FS> subFs = new HashMap<IRule, FS>();

    private IAcceptor acceptor;

    private int depth;

    public HashMap<IRule, FS> getSubFs() {
        return subFs;
    }

    public IAcceptor getAcceptor() {
        return acceptor;
    }

    public int getDepth() {
        return depth;
    }

    public static Context getContext(final FS fs) {
        return contexts.get(fs);
    }

    public static Context createContext(FS fs, IAcceptor acceptor, int depth) {
        Context context = new Context();
        context.currentFS = fs;
        context.acceptor = acceptor;
        context.depth = depth;
        contexts.put(fs, context);
        return context;
    }
}
