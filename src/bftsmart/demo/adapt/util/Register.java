package bftsmart.demo.adapt.util;

import bftsmart.demo.adapt.extractors.ValueExtractor;
import bftsmart.demo.adapt.messages.sensor.BandwidthMessage;
import bftsmart.demo.adapt.messages.sensor.SensorMessage;

import java.io.Serializable;
import java.util.*;

public class Register implements Serializable {
    private Map<SensorMessage.Type, SortedMap<Long, TimeFrame>> map = new TreeMap<>();
    private int quorum;

    public Register(int quorum) {
        this.quorum = quorum;
    }

    public boolean store(SensorMessage message) {
        SortedMap<Long, TimeFrame> timeFrames = map.get(message.getType());
        if (timeFrames == null) {
            System.out.println("No type found for the message. Creating type " + message.getType().name());
            timeFrames = new TreeMap<>();
            map.put(message.getType(), timeFrames);
        }
        TimeFrame timeFrame = timeFrames.get(message.getSequenceNumber());
        if (timeFrame == null) {
            System.out.println("No timeframe found for the message. Creating timeframe T" + message.getSequenceNumber());
            timeFrame = new TimeFrame();
            timeFrames.put(message.getSequenceNumber(), timeFrame);
        }
        return timeFrame.store(message);
    }

    public SensorMessage extract(SensorMessage.Type type, long sequenceNumber, ValueExtractor<SensorMessage> extractor) {
        SortedMap<Long, TimeFrame> timeframes = map.get(type);
        if (timeframes != null) {
            TimeFrame timeFrame = timeframes.get(sequenceNumber);
            if (timeFrame != null) {
                return timeFrame.extract(extractor);
            }
        }
        return null;
    }

    //Removes the timeframes. Should be used after executing the policy on the given timeframes
    public void clear(SensorMessage.Type type, long sequenceNumber) {
        SortedMap<Long, TimeFrame> timeFrames = map.get(type);
        if (timeFrames != null) {
            timeFrames.remove(sequenceNumber);
        }
    }

    public void clearRange(SensorMessage.Type type, long startSequenceNumber, long endSequenceNumber) {
        SortedMap<Long, TimeFrame> timeFrames = map.get(type);
        if (timeFrames != null) {
            for (long i = startSequenceNumber; i <= endSequenceNumber; i++) {
                timeFrames.remove(i);
            }
        }
    }

    private class TimeFrame {
        private final Set<SensorMessage> sensorMessages = new HashSet<>();
        private boolean extracted = false;

        //Returns true if timeframe reached necessary quorum
        private boolean store(SensorMessage sensorMessage) {
            if (extracted) {
                System.out.println("Sequence already extracted! Ignoring message.");
            }
            sensorMessages.add(sensorMessage);
            System.out.println("Adding message to set");
            return reachedQuorum();
        }

        private boolean reachedQuorum() {
            return sensorMessages.size() == quorum;
        }

        private SensorMessage extract(ValueExtractor<SensorMessage> extractor) {
            if (sensorMessages.size() == quorum) {
                extracted = true;
                return extractor.extract(sensorMessages);
            }
            return null;
        }
    }

    private Register() {
        this.quorum = 3;
        SensorMessage sm1 = new BandwidthMessage(0, 0, 10);
        SensorMessage sm2 = new BandwidthMessage(1, 0, 10);
        SensorMessage sm3 = new BandwidthMessage(2, 0, 10);
        SensorMessage[] msgs = new SensorMessage[] {
                sm1, sm2, sm3
        };
        for (SensorMessage msg : msgs) {
            if (store(msg)) {
                System.out.println("Reached quorum!");
            } else {
                System.out.println("Not reached quorum!");
            }
        }
    }

    public static void main(String[] args) {
        new Register();
    }
}
