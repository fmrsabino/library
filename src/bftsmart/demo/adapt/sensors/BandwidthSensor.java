package bftsmart.demo.adapt.sensors;

import bftsmart.demo.adapt.messages.sensor.BandwidthMessage;
import bftsmart.demo.adapt.util.BftUtils;
import bftsmart.demo.adapt.util.Constants;

public class BandwidthSensor {
    public static void main(String[] args) {

        new Thread(){
            public void run(){
                while(true){
                    System.out.println("BandwidthSensor.run before sending message");
                    byte[] reply = BftUtils.sendMessage(1003, Constants.ADAPT_HOME_FOLDER, new BandwidthMessage(10), true);
                    System.out.println("Response: " + reply);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();



        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
