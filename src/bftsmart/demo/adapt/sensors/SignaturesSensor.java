package bftsmart.demo.adapt.sensors;

import bftsmart.demo.adapt.messages.sensor.SignaturesMessage;
import bftsmart.demo.adapt.util.BftUtils;
import bftsmart.demo.adapt.util.Constants;

public class SignaturesSensor {
    public static void main(String[] args) {
        byte[] reply = BftUtils.sendMessage(1003, Constants.ADAPT_HOME_FOLDER, new SignaturesMessage(1));
        System.out.println(reply);
    }
}
