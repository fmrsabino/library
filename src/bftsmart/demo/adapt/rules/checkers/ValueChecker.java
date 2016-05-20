package bftsmart.demo.adapt.rules.checkers;

import bftsmart.demo.adapt.util.Registry;

public interface ValueChecker {
    void setup(Registry registry, int replicaId);
    void check(int nSeq);
    void terminate();
}
