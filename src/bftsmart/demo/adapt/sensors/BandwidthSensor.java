package bftsmart.demo.adapt.sensors;

import bftsmart.demo.adapt.messages.sensor.BandwidthMessage;
import bftsmart.demo.adapt.util.BftUtils;
import bftsmart.demo.adapt.util.Constants;

public class BandwidthSensor {
    public static void main(String[] args) throws Exception {
        System.out.println("Before sensor send the message to Adaptation Manager--");
        BandwidthMessage msg1 = new BandwidthMessage(0, 0, 10);
        BandwidthMessage msg2 = new BandwidthMessage(0, 0, 12);
        BandwidthMessage msg3 = new BandwidthMessage(0, 0, 14);
        BftUtils.sendMessage(1003, Constants.ADAPT_HOME, msg1, true);
        BftUtils.sendMessage(1003, Constants.ADAPT_HOME, msg2, true);
        BftUtils.sendMessage(1003, Constants.ADAPT_HOME, msg3, true);
        Thread.sleep(25000);
        BandwidthMessage msg4 = new BandwidthMessage(0, 1, 10);
        BandwidthMessage msg5 = new BandwidthMessage(0, 1, 12);
        BandwidthMessage msg6 = new BandwidthMessage(0, 1, 14);
        BftUtils.sendMessage(1003, Constants.ADAPT_HOME, msg4, true);
        BftUtils.sendMessage(1003, Constants.ADAPT_HOME, msg5, true);
        BftUtils.sendMessage(1003, Constants.ADAPT_HOME, msg6, true);

        /*ThreatLevelMessage msg1 = new ThreatLevelMessage(0, 0, new ArrayList<>(), new ArrayList<>(), 1);
        BftUtils.sendMessage(1001, Constants.ADAPT_HOME, msg1, true);
        Thread.sleep(2000);
        ThreatLevelMessage msg2 = new ThreatLevelMessage(1, 0, new ArrayList<>(), new ArrayList<>(), 1);
        BftUtils.sendMessage(1001, Constants.ADAPT_HOME, msg2, true);
        Thread.sleep(2000);
        ThreatLevelMessage msg3 = new ThreatLevelMessage(2, 0, new ArrayList<>(), new ArrayList<>(), 1);
        BftUtils.sendMessage(1001, Constants.ADAPT_HOME, msg3, true);
        //Thread.sleep(2000);*//*

        int[] ports = new int[] {
                11005, 11015, 11025, 11035, 11045, 11055, 11065
        };
        String[] hosts = new String[] {
                "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"
        };*/

       /* ChangeFMessage msg = new ChangeFMessage(ChangeFMessage.ADD_REPLICAS, new ReplicaStatus(4, "127.0.0.1", "11040"));

        BftUtils.sendToReconfiguration(hosts, ports, msg);
        Thread.sleep(3000);
        BftUtils.sendToReconfiguration(hosts, ports, msg);
        Thread.sleep(3000);
        BftUtils.sendToReconfiguration(hosts, ports, msg);
        Thread.sleep(3000);
        BftUtils.sendToReconfiguration(hosts, ports, msg);
        System.exit(0);*/

        /*ChangeBatchSizeMessage msg1 = new ChangeBatchSizeMessage(200);
        ChangeBatchSizeMessage msg2 = new ChangeBatchSizeMessage(200);
        ChangeBatchSizeMessage msg3 = new ChangeBatchSizeMessage(200);
        ChangeBatchSizeMessage msg4 = new ChangeBatchSizeMessage(200);
        BftUtils.sendToReconfiguration(hosts, ports, msg1);
        BftUtils.sendToReconfiguration(hosts, ports, msg2);
        BftUtils.sendToReconfiguration(hosts, ports, msg3);
        BftUtils.sendToReconfiguration(hosts, ports, msg4);*/
    }
}
