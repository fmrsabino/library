package bftsmart.demo.adapt.util;

import bftsmart.demo.adapt.messages.sensor.ThreatLevelMessage;
import bftsmart.demo.adapt.messages.sensor.BandwidthMessage;
import bftsmart.demo.adapt.messages.sensor.SensorMessage;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Registry implements Serializable {
    private Map<SensorMessage.Type, SortedMap<Integer, TimeFrame>> map = new TreeMap<>();

    private int quorum;
    //How many TimeFrames to store per type
    private int maxSize;

    public Registry(int quorum, int maxSize) {
        this.quorum = quorum;
        this.maxSize = maxSize;
    }

    public <T extends SensorMessage> boolean store(T message) {
        SortedMap<Integer, TimeFrame> timeFrames = map.get(message.getType());
        if (timeFrames == null) {
            System.out.println("No type found for the message. Creating type " + message.getType().name());
            timeFrames = new TreeMap<>(Comparator.comparing(Integer::intValue).reversed());
            map.put(message.getType(), timeFrames);
        }
        TimeFrame<T> timeFrame = timeFrames.get(message.getSequenceNumber());
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

    public <T,U> U cast(T t) {
        return (U) t;
    }
    public <T extends SensorMessage> List<T> extractRecentValues(SensorMessage.Type type, int size, Function<TimeFrame<T>, T> extractor) {
        SortedMap<Integer, TimeFrame> timeFrames = map.get(type);
        if (timeFrames != null) {
            Stream<TimeFrame<T>> st0 = cast(timeFrames.values().stream().limit(size));
            Stream<T> st1 = st0.map(extractor);
            return st1.collect(Collectors.toList());
        }
        return null;
    }

    public class TimeFrame<T extends SensorMessage> {
        private int sequenceN;
        private final Set<T> sensorMessages = new HashSet<>();
        private boolean extracted = false;

        public TimeFrame(int sequenceN) {
            this.sequenceN = sequenceN;
        }

        //Returns true if timeFrame reached necessary quorum
        private boolean store(T sensorMessage) {
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

        public int getSequenceN() {
            return sequenceN;
        }
    }

    private Registry() {
        this.quorum = 3;
        this.maxSize = 2;
        SensorMessage sm1 = new BandwidthMessage(0, 0, 10);
        SensorMessage sm2 = new BandwidthMessage(1, 0, 20);
        SensorMessage sm3 = new BandwidthMessage(2, 0, 30);
        SensorMessage sm4 = new BandwidthMessage(0, 1, 50);
        SensorMessage sm5 = new BandwidthMessage(1, 1, 100);
        SensorMessage sm6 = new BandwidthMessage(2, 1, 150);

        List l = new ArrayList<>();
        SensorMessage tm1 = new ThreatLevelMessage(0, 0, l, l, 1);
        SensorMessage tm2 = new ThreatLevelMessage(1, 0, l, l, 2);
        SensorMessage tm3 = new ThreatLevelMessage(2, 0, l, l, 3);
        SensorMessage tm4 = new ThreatLevelMessage(0, 1, l, l, 4);
        SensorMessage tm5 = new ThreatLevelMessage(1, 1, l, l, 5);
        SensorMessage tm6 = new ThreatLevelMessage(2, 1, l, l, 6);

        SensorMessage[] msgs = new SensorMessage[] {
                sm1, sm2, sm3, sm4, sm5, sm6, tm1,tm2, tm3, tm4, tm5, tm6
        };
        for (SensorMessage msg : msgs) {
            store(msg);
        }

        Collection<BandwidthMessage> msgx = extractRecentValues(SensorMessage.Type.BANDWIDTH, 2, Registry::getMedian);
        for (BandwidthMessage bm : msgx) {
            System.out.println(bm);
        }

        Collection<ThreatLevelMessage> msgx1 = extractRecentValues(SensorMessage.Type.THREAT, 2, Registry::getMedian);
        for (ThreatLevelMessage bm : msgx1) {
            System.out.println(bm);
        }

    }

    public static <T extends SensorMessage> T getMedian(TimeFrame<T> tf) {
        List<T> t = tf.sensorMessages.stream().sorted().collect(Collectors.toList());
        return t.get(t.size()/2);
    }

    public static void main(String[] args) {
        new Registry();
    }
}
