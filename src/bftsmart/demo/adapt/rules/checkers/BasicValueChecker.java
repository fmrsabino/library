package bftsmart.demo.adapt.rules.checkers;

import bftsmart.demo.adapt.util.Registry;

public class BasicValueChecker implements ValueChecker {
    private Registry registry;
    @Override
    public void setup(Registry registry) {
        this.registry = registry;
    }

    @Override
    public void check() {
        System.out.println("[BasicValueChecker] Checking...");
    }

    @Override
    public void terminate() {
        System.out.println("[BasicValueChecker] Terminating...");
    }
}
