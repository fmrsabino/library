package bftsmart.demo.adapt.test;

import bftsmart.demo.adapt.messages.MessageWithDigest;
import bftsmart.demo.adapt.messages.sensor.BandwidthMessage;
import bftsmart.demo.adapt.util.BftUtils;
import bftsmart.demo.adapt.util.Constants;
import bftsmart.demo.adapt.util.SecurityUtils;

import java.io.IOException;
import java.security.PrivateKey;

public class SensorsTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        PrivateKey privateKey0 = SecurityUtils.getPrivateKey("sensor/keys/privatekey0", "RSA");
        PrivateKey privateKey1 = SecurityUtils.getPrivateKey("sensor/keys/privatekey1", "RSA");
        PrivateKey privateKey2 = SecurityUtils.getPrivateKey("sensor/keys/privatekey2", "RSA");
        PrivateKey privateKey3 = SecurityUtils.getPrivateKey("sensor/keys/privatekey3", "RSA");

        //Map<Integer, PublicKey> publicKeys = FileUtils.readPublicKeys("sensor/keys", "RSA");

        MessageWithDigest<BandwidthMessage> bm0 = new MessageWithDigest<>(new BandwidthMessage(0, 0, 10));
        bm0.sign(privateKey0);
        MessageWithDigest<BandwidthMessage> bm1 = new MessageWithDigest<>(new BandwidthMessage(1, 0, 100));
        bm1.sign(privateKey1);
        MessageWithDigest<BandwidthMessage> bm2 = new MessageWithDigest<>(new BandwidthMessage(2, 0, 150));
        bm2.sign(privateKey2);
        System.out.println(BftUtils.sendMessage(1001, Constants.ADAPT_HOME, bm0, true));
        System.out.println(BftUtils.sendMessage(1001, Constants.ADAPT_HOME, bm1, true));
        System.out.println(BftUtils.sendMessage(1001, Constants.ADAPT_HOME, bm2, true));
        //Thread.sleep(5000);


        System.exit(0);
    }
}
