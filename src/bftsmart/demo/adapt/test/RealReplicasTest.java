package bftsmart.demo.adapt.test;

import bftsmart.demo.adapt.util.SecurityUtils;
import bftsmart.demo.counter.CounterClient;

import java.io.IOException;
import java.security.PrivateKey;

public class RealReplicasTest {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        addReplica(4, 11040, 0);
        addReplica(5, 11050, 1);
        addReplica(6, 11060, 2);
        Thread.sleep(10000);
        CounterClient.main(new String[]{"1001", "2"});
        Thread.sleep(10000);
        removeReplica(6, 11060, 3);
        removeReplica(5, 11050, 4);
        removeReplica(4, 11040, 5);
        Thread.sleep(10000);
        CounterClient.main(new String[]{"1002", "2"});
        Thread.sleep(10000);
        System.exit(0);
    }

    public static void addReplica(int id, int port, int seqN) throws InterruptedException {
        PrivateKey privateKey0 = SecurityUtils.getPrivateKey("adapt-config/keys/privatekey0", "RSA");
        PrivateKey privateKey1 = SecurityUtils.getPrivateKey("adapt-config/keys/privatekey1", "RSA");
        PrivateKey privateKey2 = SecurityUtils.getPrivateKey("adapt-config/keys/privatekey2", "RSA");
        PrivateKey privateKey3 = SecurityUtils.getPrivateKey("adapt-config/keys/privatekey3", "RSA");

        int[] ports = new int[] {
                //11005,
                //11015,
                //11025,
                //11035,
                11045,
                //11055,
                //11065
        };
        String[] hosts = new String[] {
                "127.0.0.1",
                /*"127.0.0.1",
                "127.0.0.1",
                "127.0.0.1",
                "127.0.0.1",
                "127.0.0.1",
                "127.0.0.1"*/
        };

        /*MessageWithDigest<ExamplePolicyMessage> msg0 = new MessageWithDigest<>(
                new ExamplePolicyMessage(seqN, 0, ExamplePolicyMessage.ADD_REPLICAS, new ReplicaStatus(id, "127.0.0.1", ""+port)));
        msg0.sign(privateKey0);
        MessageWithDigest<ExamplePolicyMessage> msg1 = new MessageWithDigest<>(
                new ExamplePolicyMessage(seqN, 1, ExamplePolicyMessage.ADD_REPLICAS, new ReplicaStatus(id, "127.0.0.1", ""+port)));
        msg1.sign(privateKey1);
        MessageWithDigest<ExamplePolicyMessage> msg2 = new MessageWithDigest<>(
                new ExamplePolicyMessage(seqN, 2, ExamplePolicyMessage.ADD_REPLICAS, new ReplicaStatus(id, "127.0.0.1", ""+port)));
        msg2.sign(privateKey2);
        MessageWithDigest<ExamplePolicyMessage> msg3 = new MessageWithDigest<>(
                new ExamplePolicyMessage(seqN, 3, ExamplePolicyMessage.ADD_REPLICAS, new ReplicaStatus(id, "127.0.0.1", ""+port)));
        msg3.sign(privateKey3);


        BftUtils.sendToReconfiguration(hosts, ports, msg0);
        Thread.sleep(3000);
        BftUtils.sendToReconfiguration(hosts, ports, msg1);
        Thread.sleep(3000);
        BftUtils.sendToReconfiguration(hosts, ports, msg2);
        Thread.sleep(3000);
        BftUtils.sendToReconfiguration(hosts, ports, msg3);*/
    }

    public static void removeReplica(int id, int port, int seqN) throws InterruptedException {
        PrivateKey privateKey0 = SecurityUtils.getPrivateKey("adapt-config/keys/privatekey0", "RSA");
        PrivateKey privateKey1 = SecurityUtils.getPrivateKey("adapt-config/keys/privatekey1", "RSA");
        PrivateKey privateKey2 = SecurityUtils.getPrivateKey("adapt-config/keys/privatekey2", "RSA");
        PrivateKey privateKey3 = SecurityUtils.getPrivateKey("adapt-config/keys/privatekey3", "RSA");

        int[] ports = new int[] {
                11005,
                11015,
                11025,
                11035,
                11045,
                11055,
                11065
        };
        String[] hosts = new String[] {
                "127.0.0.1",
                "127.0.0.1",
                "127.0.0.1",
                "127.0.0.1",
                "127.0.0.1",
                "127.0.0.1",
                "127.0.0.1"
        };

        /*MessageWithDigest<ExamplePolicyMessage> msg0 = new MessageWithDigest<>(
                new ExamplePolicyMessage(seqN, 0, ExamplePolicyMessage.REMOVE_REPLICAS, new ReplicaStatus(id, "127.0.0.1", ""+port)));
        msg0.sign(privateKey0);
        MessageWithDigest<ExamplePolicyMessage> msg1 = new MessageWithDigest<>(
                new ExamplePolicyMessage(seqN, 1, ExamplePolicyMessage.REMOVE_REPLICAS, new ReplicaStatus(id, "127.0.0.1", ""+port)));
        msg1.sign(privateKey1);
        MessageWithDigest<ExamplePolicyMessage> msg2 = new MessageWithDigest<>(
                new ExamplePolicyMessage(seqN, 2, ExamplePolicyMessage.REMOVE_REPLICAS, new ReplicaStatus(id, "127.0.0.1", ""+port)));
        msg2.sign(privateKey2);
        MessageWithDigest<ExamplePolicyMessage> msg3 = new MessageWithDigest<>(
                new ExamplePolicyMessage(seqN, 3, ExamplePolicyMessage.REMOVE_REPLICAS, new ReplicaStatus(id, "127.0.0.1", ""+port)));
        msg3.sign(privateKey3);


        BftUtils.sendToReconfiguration(hosts, ports, msg0);
        Thread.sleep(3000);
        BftUtils.sendToReconfiguration(hosts, ports, msg1);
        Thread.sleep(3000);
        BftUtils.sendToReconfiguration(hosts, ports, msg2);
        Thread.sleep(3000);
        BftUtils.sendToReconfiguration(hosts, ports, msg3);*/
    }
}
