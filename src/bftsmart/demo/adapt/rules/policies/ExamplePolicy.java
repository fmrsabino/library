package bftsmart.demo.adapt.rules.policies;

import bftsmart.demo.adapt.messages.MessageWithDigest;
import bftsmart.demo.adapt.messages.adapt.ExamplePolicyMessage;
import bftsmart.demo.adapt.messages.sensor.ReplicaStatus;
import bftsmart.demo.adapt.messages.sensor.SensorMessage;
import bftsmart.demo.adapt.messages.sensor.ThreatLevelMessage;
import bftsmart.demo.adapt.messages.sensor.WorkloadSizeMessage;
import bftsmart.demo.adapt.util.BftUtils;
import bftsmart.demo.adapt.util.Constants;
import bftsmart.demo.adapt.util.Registry;
import bftsmart.demo.adapt.util.SecurityUtils;

import java.io.File;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Collection;

public class ExamplePolicy implements AdaptPolicy {
    private static final String[] BASE_HOSTS = new String[] {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};
    private static final String[] ADDITIONAL_HOSTS = new String[] {"127.0.0.1", "127.0.0.1", "127.0.0.1"};
    private static final int[] BASE_RECONFIGURATION_GROUP_PORTS = new int[] {11005, 11015, 11025, 11035};
    private static final int[] ADDITIONAL_RECONFIGURATION_GROUP_PORT = new int[] {11045, 11055, 11065};

    private static ReplicaStatus[] dynamicGroup = new ReplicaStatus[] {
            new ReplicaStatus(4, "127.0.0.1", "11040"),
            new ReplicaStatus(5, "127.0.0.1", "11050"),
            new ReplicaStatus(6, "127.0.0.1", "11060")
    };

    @Override
    public void execute(int seqN, int executorId, Registry messages) {
        System.out.println("Executing Example Policy");
        PrivateKey pk = SecurityUtils.getPrivateKey(Constants.ADAPT_KEYS_PATH + File.separator + "privatekey" + executorId, "RSA");

        ExamplePolicyMessage examplePolicyMessage = new ExamplePolicyMessage(seqN, executorId);

        //Check threat level
        Collection<ThreatLevelMessage> threats = messages.extractRecentValues(SensorMessage.Type.THREAT, 5, Registry::getMedian);
        if (threats.stream().anyMatch(t -> t.getThreatLevel() > 0)) { //high threat
            examplePolicyMessage.setCommand(ExamplePolicyMessage.ADD_REPLICAS);
            examplePolicyMessage.setReplicas(Arrays.asList(dynamicGroup));
        } else if (threats.stream().anyMatch(t -> t.getThreatLevel() < 0)) { //low threat
            examplePolicyMessage.setCommand(ExamplePolicyMessage.REMOVE_REPLICAS);
            examplePolicyMessage.setReplicas(Arrays.asList(dynamicGroup));
        } else {
            examplePolicyMessage.setCommand(ExamplePolicyMessage.DO_NOTHING);
        }

        //Check workload
        Collection<WorkloadSizeMessage> workloads = messages.extractRecentValues(SensorMessage.Type.WORKLOAD, 5, Registry::getMedian);
        if (workloads.stream().anyMatch(w -> w.getWorkload() > 0)) {
            examplePolicyMessage.setBatchSize(600);
        } else if (workloads.stream().anyMatch(w -> w.getWorkload() < 0)) {
            examplePolicyMessage.setBatchSize(200);
        } else if (workloads.stream().anyMatch(w -> w.getWorkload() == 0)) {
            examplePolicyMessage.setBatchSize(400);
        }

        //Send configuration
        new Thread(() -> {
            MessageWithDigest<ExamplePolicyMessage> msg = new MessageWithDigest<>(examplePolicyMessage);
            msg.sign(pk);
            BftUtils.sendToReconfiguration(BASE_HOSTS, BASE_RECONFIGURATION_GROUP_PORTS, msg);
        }).start();
    }
}
