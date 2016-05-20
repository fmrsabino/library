package bftsmart.demo.adapt.rules.policies;

import bftsmart.demo.adapt.messages.MessageWithDigest;
import bftsmart.demo.adapt.messages.adapt.ChangeFMessage;
import bftsmart.demo.adapt.messages.sensor.ReplicaStatus;
import bftsmart.demo.adapt.util.BftUtils;
import bftsmart.demo.adapt.util.Constants;
import bftsmart.demo.adapt.util.Registry;
import bftsmart.demo.adapt.util.SecurityUtils;

import java.io.File;
import java.security.PrivateKey;

public class ExamplePolicy implements AdaptPolicy {
    @Override
    public void execute(int executorId, Registry messages) {
        System.out.println("Executing Example Policy");

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

        PrivateKey pk = SecurityUtils.getPrivateKey(Constants.ADAPT_KEYS_PATH + File.separator + "privatekey" + executorId, "RSA");
        MessageWithDigest<ChangeFMessage> msg = new MessageWithDigest<>(
                new ChangeFMessage(0 , executorId, ChangeFMessage.ADD_REPLICAS, new ReplicaStatus(4, "127.0.0.1", "11040")));
        msg.sign(pk);

        BftUtils.sendToReconfiguration(hosts, ports, msg);
    }
}
