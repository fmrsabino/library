package bftsmart.demo.adapt.rules.checkers;

import bftsmart.demo.adapt.messages.MessageWithDigest;
import bftsmart.demo.adapt.messages.PolicyNotification;
import bftsmart.demo.adapt.messages.sensor.SensorMessage;
import bftsmart.demo.adapt.messages.sensor.ThreatLevelMessage;
import bftsmart.demo.adapt.messages.sensor.WorkloadSizeMessage;
import bftsmart.demo.adapt.util.BftUtils;
import bftsmart.demo.adapt.util.Constants;
import bftsmart.demo.adapt.util.Registry;
import bftsmart.demo.adapt.util.SecurityUtils;

import java.io.File;
import java.util.Collection;

public class BasicValueChecker implements ValueChecker {
    private Registry registry;
    private int replicaId;

    @Override
    public void setup(Registry registry, int replicaId) {
        this.registry = registry;
        this.replicaId = replicaId;
    }

    @Override
    public void check(int nSeq) {
        System.out.println("[BasicValueChecker] Checking...");
        if (registry == null) {
            return;
        }

        Collection<ThreatLevelMessage> threats = registry.extractRecentValues(SensorMessage.Type.THREAT, 5, Registry::getMedian);
        if (threats.stream().anyMatch(t -> t.getThreatLevel() >= 0)) {
            sendPolicyNotification(nSeq);
        }

        Collection<WorkloadSizeMessage> workloads = registry.extractRecentValues(SensorMessage.Type.WORKLOAD, 5, Registry::getMedian);
        if (workloads.stream().anyMatch(w -> w.getWorkload() >= 0)) {
            sendPolicyNotification(nSeq);
        }
    }

    private void sendPolicyNotification(int nSeq) {
        new Thread(() -> {
            MessageWithDigest<PolicyNotification> msg = new MessageWithDigest<>(new PolicyNotification(nSeq, replicaId));
            msg.sign(SecurityUtils.getPrivateKey(Constants.ADAPT_KEYS_PATH + File.separator + "privatekey" + replicaId, "RSA"));
            BftUtils.sendMessage(1001+replicaId, Constants.ADAPT_HOME, msg, true);
        }).start();
    }

    @Override
    public void terminate() {
        System.out.println("[BasicValueChecker] Terminating...");
    }
}
