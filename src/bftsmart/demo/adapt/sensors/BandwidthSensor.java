package bftsmart.demo.adapt.sensors;

import bftsmart.demo.adapt.messages.sensor.BandwidthMessage;
import bftsmart.demo.adapt.util.BftUtils;
import bftsmart.demo.adapt.util.Constants;

import java.util.Arrays;

public class BandwidthSensor {
    public static void main(String[] args) {
        System.out.println("Before sensor send the message to Adaptation Manager--");
        byte[] reply = BftUtils.sendMessage(1003, Constants.ADAPT_HOME_FOLDER, new BandwidthMessage(10), true);
        System.out.println("Response: " + Arrays.toString(reply));

    }
}
