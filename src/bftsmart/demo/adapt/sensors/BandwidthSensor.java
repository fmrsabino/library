package bftsmart.demo.adapt.sensors;

import bftsmart.demo.adapt.messages.sensor.BandwidthMessage;
import bftsmart.demo.adapt.util.BftUtils;
import bftsmart.demo.adapt.util.Constants;

public class BandwidthSensor {
    public static void main(String[] args) {
        byte[] reply = BftUtils.sendMessage(1003, Constants.ADAPT_HOME_FOLDER, new BandwidthMessage(10));
        System.out.println(reply);
    }
}
