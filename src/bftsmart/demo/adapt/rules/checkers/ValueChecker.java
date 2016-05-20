package bftsmart.demo.adapt.rules.checkers;

import bftsmart.demo.adapt.util.Registry;

public interface ValueChecker {
    void setup(Registry registry);
    void check();
    void terminate();
}
