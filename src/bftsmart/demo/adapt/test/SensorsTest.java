package bftsmart.demo.adapt.test;

import bftsmart.demo.adapt.messages.MessageWithDigest;
import bftsmart.demo.adapt.messages.sensor.WorkloadSizeMessage;
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

        /*MessageWithDigest<ThreatLevelMessage> bm0 = new MessageWithDigest<>(new ThreatLevelMessage(0, 0, 10));
        bm0.sign(privateKey0);
        MessageWithDigest<ThreatLevelMessage> bm1 = new MessageWithDigest<>(new ThreatLevelMessage(1, 0, 10));
        bm1.sign(privateKey1);
        MessageWithDigest<ThreatLevelMessage> bm2 = new MessageWithDigest<>(new ThreatLevelMessage(2, 0, 15));
        bm2.sign(privateKey2);
        System.out.println(BftUtils.sendMessage(1001, Constants.ADAPT_HOME, bm0, true));
        System.out.println(BftUtils.sendMessage(1001, Constants.ADAPT_HOME, bm1, true));
        System.out.println(BftUtils.sendMessage(1001, Constants.ADAPT_HOME, bm2, true));*/


        MessageWithDigest<WorkloadSizeMessage> bm0 = new MessageWithDigest<>(new WorkloadSizeMessage(0, 0, 10));
        bm0.sign(privateKey0);
        MessageWithDigest<WorkloadSizeMessage> bm1 = new MessageWithDigest<>(new WorkloadSizeMessage(1, 0, 10));
        bm1.sign(privateKey1);
        MessageWithDigest<WorkloadSizeMessage> bm2 = new MessageWithDigest<>(new WorkloadSizeMessage(2, 0, 15));
        bm2.sign(privateKey2);
        System.out.println(BftUtils.sendMessage(1001, Constants.ADAPT_HOME, bm0, true));
        System.out.println(BftUtils.sendMessage(1001, Constants.ADAPT_HOME, bm1, true));
        System.out.println(BftUtils.sendMessage(1001, Constants.ADAPT_HOME, bm2, true));

        /*Thread.sleep(30000);

        MessageWithDigest<BandwidthMessage> bm3 = new MessageWithDigest<>(new BandwidthMessage(0, 1, -10));
        bm3.sign(privateKey0);
        MessageWithDigest<BandwidthMessage> bm4 = new MessageWithDigest<>(new BandwidthMessage(1, 1, -100));
        bm4.sign(privateKey1);
        MessageWithDigest<BandwidthMessage> bm5 = new MessageWithDigest<>(new BandwidthMessage(2, 1, -150));
        bm5.sign(privateKey2);
        System.out.println(BftUtils.sendMessage(1001, Constants.ADAPT_HOME, bm3, true));
        System.out.println(BftUtils.sendMessage(1001, Constants.ADAPT_HOME, bm4, true));
        System.out.println(BftUtils.sendMessage(1001, Constants.ADAPT_HOME, bm5, true));*/

        System.exit(0);
    }
}
