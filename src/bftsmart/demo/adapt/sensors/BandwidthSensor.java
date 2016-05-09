package bftsmart.demo.adapt.sensors;

import bftsmart.demo.adapt.messages.sensor.BandwidthMessage;
import bftsmart.demo.adapt.messages.sensor.SensorMessage;
import bftsmart.demo.adapt.util.BftUtils;
import bftsmart.demo.adapt.util.Constants;

import java.util.Arrays;

public class BandwidthSensor {
    public static void main(String[] args) throws Exception {
        System.out.println("Before sensor send the message to Adaptation Manager--");
        BandwidthMessage msg1 = new BandwidthMessage(0, 0, 10);
        BandwidthMessage msg2 = new BandwidthMessage(0, 0, 12);
        BandwidthMessage msg3 = new BandwidthMessage(0, 0, 14);
        BftUtils.sendMessage(1003, Constants.ADAPT_HOME_FOLDER, msg1, true);
        BftUtils.sendMessage(1003, Constants.ADAPT_HOME_FOLDER, msg2, true);
        BftUtils.sendMessage(1003, Constants.ADAPT_HOME_FOLDER, msg3, true);
        Thread.sleep(10000);
        BandwidthMessage msg4 = new BandwidthMessage(0, 1, 10);
        BandwidthMessage msg5 = new BandwidthMessage(0, 1, 12);
        BandwidthMessage msg6 = new BandwidthMessage(0, 1, 14);
        BftUtils.sendMessage(1003, Constants.ADAPT_HOME_FOLDER, msg4, true);
        BftUtils.sendMessage(1003, Constants.ADAPT_HOME_FOLDER, msg5, true);
        BftUtils.sendMessage(1003, Constants.ADAPT_HOME_FOLDER, msg6, true);

        //System.out.println("Response: " + Arrays.toString(reply));
    }
}
