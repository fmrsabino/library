package bftsmart.demo.adapt.rules.checkers;

import bftsmart.demo.adapt.messages.MessageWithDigest;
import bftsmart.demo.adapt.messages.adapt.ChangeFMessage;
import bftsmart.demo.adapt.messages.sensor.BandwidthMessage;
import bftsmart.demo.adapt.messages.sensor.SensorMessage;
import bftsmart.demo.adapt.util.BftUtils;
import bftsmart.demo.adapt.util.Constants;
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

        Collection<BandwidthMessage> threats = registry.extractRecentValues(SensorMessage.Type.BANDWIDTH, 5, Registry::getMedian);
        if (threats.stream().anyMatch(t -> t.getBandwidth() >= 100)) {
            System.out.println("Detected High Bandwidth level. Noticing to run the policy");
            new Thread(() -> BftUtils.sendMessage(1001, Constants.ADAPT_HOME, new MessageWithDigest<>(new ChangeFMessage(0,0,0, null)), true)).start();
        }
    }

    @Override
    public void terminate() {
        System.out.println("[BasicValueChecker] Terminating...");
    }
}
