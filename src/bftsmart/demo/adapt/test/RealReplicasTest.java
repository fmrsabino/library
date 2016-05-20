package bftsmart.demo.adapt.test;

import bftsmart.demo.adapt.messages.MessageWithDigest;
import bftsmart.demo.adapt.messages.adapt.ChangeFMessage;
import bftsmart.demo.adapt.messages.sensor.ReplicaStatus;
import bftsmart.demo.adapt.servers.ReconfigurableReplica;
import bftsmart.demo.adapt.util.BftUtils;
import bftsmart.demo.adapt.util.FileUtils;
import bftsmart.demo.adapt.util.SecurityUtils;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

public class RealReplicasTest {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        PrivateKey privateKey0 = SecurityUtils.getPrivateKey("adapt-config/keys/privatekey0", "RSA");
        PrivateKey privateKey1 = SecurityUtils.getPrivateKey("adapt-config/keys/privatekey1", "RSA");
        PrivateKey privateKey2 = SecurityUtils.getPrivateKey("adapt-config/keys/privatekey2", "RSA");
        PrivateKey privateKey3 = SecurityUtils.getPrivateKey("adapt-config/keys/privatekey3", "RSA");


        Map<Integer, PublicKey> pks = FileUtils.readPublicKeys("adapt-config/keys", "RSA");

        ReconfigurableReplica.ReconfigurationService.MessagesContainer container
                = new ReconfigurableReplica.ReconfigurationService.MessagesContainer(pks, 2, 4);



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
