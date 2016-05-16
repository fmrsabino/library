package bftsmart.demo.adapt.rules.checkers;

import bftsmart.demo.adapt.util.Registry;

public class HybridValueChecker implements ValueChecker {
    private Registry registry;

    @Override
    public void setup(Registry registry) {
        this.registry = registry;
    }

    @Override
    public void check() {

    }

    @Override
    public void terminate() {

    }
}
