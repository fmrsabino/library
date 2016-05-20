package bftsmart.demo.adapt.rules.checkers;

import bftsmart.demo.adapt.util.Registry;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimedValueChecker implements ValueChecker {
    private ScheduledExecutorService service;
    private Registry registry;
    private int replicaId;

    @Override
    public void setup(Registry registry, int replicaId) {
        this.registry = registry;
        this.replicaId = replicaId;
        if (service == null || service.isShutdown()) {
            service = Executors.newSingleThreadScheduledExecutor();
            service.scheduleAtFixedRate(new TimedCheck(), 0, 20, TimeUnit.SECONDS);
        }
    }

    @Override
    public void check(int nSeq) {
        System.out.println("[TimedValueChecker] This is a timed checker. Ignoring check() call.");
    }

    private class TimedCheck implements Runnable {
        @Override
        public void run() {
            System.out.println("[TimedValueChecker] Started check");
        }
    }

    @Override
    public void terminate() {
        System.out.println("[TimedValueChecker] Terminating...");
        if (service != null) {
            service.shutdown();
        }
    }
}
