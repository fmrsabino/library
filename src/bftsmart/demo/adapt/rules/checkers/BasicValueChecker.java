package bftsmart.demo.adapt.rules.checkers;

import bftsmart.demo.adapt.messages.sensor.SensorMessage;
import bftsmart.demo.adapt.messages.sensor.ThreatLevelMessage;
import bftsmart.demo.adapt.util.Registry;

import java.util.Collection;

public class BasicValueChecker implements ValueChecker {
    private Registry registry;
    @Override
    public void setup(Registry registry) {
        this.registry = registry;
    }

    @Override
    public void check() {
        System.out.println("[BasicValueChecker] Checking...");
        if (registry == null) {
            return;
        }

        Collection<ThreatLevelMessage> threats = registry.extractRecentValues(SensorMessage.Type.THREAT, 5, Registry::getMedian);
    }

    @Override
    public void terminate() {
        System.out.println("[BasicValueChecker] Terminating...");
    }
}
