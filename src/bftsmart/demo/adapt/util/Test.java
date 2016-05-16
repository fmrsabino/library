package bftsmart.demo.adapt.util;

import bftsmart.demo.adapt.messages.MessageWithDigest;
import bftsmart.demo.adapt.messages.adapt.ChangeFMessage;
import bftsmart.demo.adapt.messages.sensor.ReplicaStatus;
import bftsmart.demo.adapt.servers.ReconfigurableReplica;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        PrivateKey privateKey0 = SecurityUtils.getPrivateKey("adapt-keys/privatekey0", "RSA");
        PublicKey publicKey0 = SecurityUtils.getPublicKey("adapt-keys/publickey0", "RSA");
        PrivateKey privateKey1 = SecurityUtils.getPrivateKey("adapt-keys/privatekey1", "RSA");
        PublicKey publicKey1 = SecurityUtils.getPublicKey("adapt-keys/publickey1", "RSA");
        PrivateKey privateKey2 = SecurityUtils.getPrivateKey("adapt-keys/privatekey2", "RSA");
        PublicKey publicKey2 = SecurityUtils.getPublicKey("adapt-keys/publickey2", "RSA");
        PrivateKey privateKey3 = SecurityUtils.getPrivateKey("adapt-keys/privatekey3", "RSA");
        PublicKey publicKey3 = SecurityUtils.getPublicKey("adapt-keys/publickey3", "RSA");

        Map<Integer, PublicKey> pks = new HashMap<>();
        pks.put(0, publicKey0);
        pks.put(1, publicKey1);
        pks.put(2, publicKey2);
        pks.put(3, publicKey3);

        ReconfigurableReplica.ReconfigurationService.MessagesContainer container
                = new ReconfigurableReplica.ReconfigurationService.MessagesContainer(pks, 2, 4);


       /* MessageWithDigest<ChangeBatchSizeMessage> msg1 =
                new MessageWithDigest<>(new ChangeBatchSizeMessage(0, 0, 10), privateKey0);
        MessageWithDigest<ChangeBatchSizeMessage> msg2 =
                new MessageWithDigest<>(new ChangeBatchSizeMessage(0, 1, 10), privateKey1);
        MessageWithDigest<ChangeBatchSizeMessage> msg3 =
                new MessageWithDigest<>(new ChangeBatchSizeMessage(0, 2, 10), privateKey2);
        MessageWithDigest<ChangeBatchSizeMessage> msg4 =
                new MessageWithDigest<>(new ChangeBatchSizeMessage(0, 3, 10), privateKey3);*/



        /*System.out.println(container.store(msg1) != null ? "REACHED QUORUM!!" : "DIDN'T REACH QUORUM");
        System.out.println(container.store(msg2) != null ? "REACHED QUORUM!!" : "DIDN'T REACH QUORUM");
        System.out.println(container.store(msg3) != null ? "REACHED QUORUM!!" : "DIDN'T REACH QUORUM");
        System.out.println(container.store(msg4) != null ? "REACHED QUORUM!!" : "DIDN'T REACH QUORUM");*/


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

        MessageWithDigest<ChangeFMessage> msg0 = new MessageWithDigest<>(
                new ChangeFMessage(0 , 0, ChangeFMessage.ADD_REPLICAS, new ReplicaStatus(4, "127.0.0.1", "11040")));
        msg0.sign(privateKey0);
        MessageWithDigest<ChangeFMessage> msg1 = new MessageWithDigest<>(
                new ChangeFMessage(0 , 1, ChangeFMessage.ADD_REPLICAS, new ReplicaStatus(4, "127.0.0.1", "11040")));
        msg1.sign(privateKey1);
        MessageWithDigest<ChangeFMessage> msg2 = new MessageWithDigest<>(
                new ChangeFMessage(0 , 2, ChangeFMessage.ADD_REPLICAS, new ReplicaStatus(4, "127.0.0.1", "11040")));
        msg2.sign(privateKey2);
        MessageWithDigest<ChangeFMessage> msg3 = new MessageWithDigest<>(
                new ChangeFMessage(0 , 3, ChangeFMessage.ADD_REPLICAS, new ReplicaStatus(4, "127.0.0.1", "11040")));
        msg3.sign(privateKey3);

        /*msg0.sign(privateKey0);
        byte[] bytes = MessageSerializer.serialize(msg0);
        MessageWithDigest msg = MessageSerializer.deserialize(bytes);
        System.out.println(msg.verify(publicKey0) ? "VALID" : "INVALID");*/

        BftUtils.sendToReconfiguration(hosts, ports, msg0);
        Thread.sleep(3000);
        BftUtils.sendToReconfiguration(hosts, ports, msg1);
        Thread.sleep(3000);
        BftUtils.sendToReconfiguration(hosts, ports, msg2);
        Thread.sleep(3000);
        BftUtils.sendToReconfiguration(hosts, ports, msg3);
        System.exit(0);

        System.exit(0);
    }
}
