package bftsmart.demo.adapt.util;

import bftsmart.demo.adapt.rules.extractors.ValueExtractor;
import bftsmart.demo.adapt.messages.sensor.BandwidthMessage;
import bftsmart.demo.adapt.messages.sensor.SensorMessage;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Registry implements Serializable {
    private Map<SensorMessage.Type, SortedMap<Integer, TimeFrame>> map = new TreeMap<>();
    private int quorum;
    //How many TimeFrames to store per type
    private int maxSize;

    public Registry(int quorum, int maxSize) {
        this.quorum = quorum;
        this.maxSize = maxSize;
    }

    public boolean store(SensorMessage message) {
        SortedMap<Integer, TimeFrame> timeFrames = map.get(message.getType());
        if (timeFrames == null) {
            System.out.println("No type found for the message. Creating type " + message.getType().name());
            timeFrames = new TreeMap<>(Comparator.comparing(Integer::intValue).reversed());
            map.put(message.getType(), timeFrames);
        }
        TimeFrame timeFrame = timeFrames.get(message.getSequenceNumber());
        if (timeFrame == null) {
            System.out.println("No TimeFrame found for the message. Creating TimeFrame T" + message.getSequenceNumber());
            timeFrame = new TimeFrame(message.getSequenceNumber());
            if (timeFrames.keySet().size() >= maxSize) {
                System.out.println("Reached size limit. Removing oldest key " + timeFrames.lastKey());
                timeFrames.remove(timeFrames.lastKey());
            }
            timeFrames.put(message.getSequenceNumber(), timeFrame);

        }
        return timeFrame.store(message);
    }

    public SensorMessage extract(SensorMessage.Type type, int sequenceNumber, ValueExtractor<SensorMessage> extractor) {
        SortedMap<Integer, TimeFrame> timeFrames = map.get(type);
        if (timeFrames != null) {
            TimeFrame timeFrame = timeFrames.get(sequenceNumber);
            if (timeFrame != null) {
                return timeFrame.extract(extractor);
            }
        }
        return null;
    }

    public Collection<TimeFrame> recentValues(SensorMessage.Type type, int size) {
        SortedMap<Integer, TimeFrame> timeFrames = map.get(type);
        if (timeFrames != null) {
            return timeFrames.values().stream().limit(size).collect(Collectors.toList());
        }
        return null;
    }

    public class TimeFrame {
        private int sequenceN;
        private final Set<SensorMessage> sensorMessages = new HashSet<>();
        private boolean extracted = false;

        public TimeFrame(int sequenceN) {
            this.sequenceN = sequenceN;
        }

        //Returns true if timeFrame reached necessary quorum
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

        public int getSequenceN() {
            return sequenceN;
        }
    }

    private Registry() {
        this.quorum = 3;
        this.maxSize = 2;
        SensorMessage sm1 = new BandwidthMessage(0, 0, 10);
        SensorMessage sm2 = new BandwidthMessage(0, 1, 20);
        SensorMessage sm3 = new BandwidthMessage(0, 2, 30);
        SensorMessage sm4 = new BandwidthMessage(0, 3, 30);
        SensorMessage sm5 = new BandwidthMessage(0, 4, 30);
        SensorMessage sm6 = new BandwidthMessage(0, 5, 30);
        SensorMessage sm7 = new BandwidthMessage(0, 6, 30);
        SensorMessage[] msgs = new SensorMessage[] {
                sm1, sm2, sm3, sm4, sm5, sm6, sm7
        };
        for (SensorMessage msg : msgs) {
            store(msg);
        }
        for (TimeFrame tf : recentValues(SensorMessage.Type.BANDWIDTH, 2)) {
            System.out.println(tf.getSequenceN());
        }
    }

    public static void main(String[] args) {
        new Registry();
    }
}
