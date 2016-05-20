package bftsmart.demo.adapt.rules.checkers;

import bftsmart.demo.adapt.util.Registry;

public class HybridValueChecker implements ValueChecker {
    private Registry registry;
    private int replicaId;

    @Override
    public void setup(Registry registry, int replicaId) {
        this.registry = registry;
        this.replicaId = replicaId;
    }

    @Override
    public void check(int nSeq) {

    }

    @Override
    public void terminate() {

    }
}
