package bftsmart.demo.adapt.rules.checkers;

import bftsmart.demo.adapt.util.Registry;

public interface ValueChecker {
    //setup can be used to setup a timer
    void setup(Registry registry);
    void check();
    void terminate();
}
