package bftsmart.demo.adapt.util;

import bftsmart.demo.adapt.messages.MessageWithDigest;
import bftsmart.demo.adapt.messages.sensor.SensorMessage;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Registry implements Serializable {
    private Map<SensorMessage.Type, SortedMap<Integer, TimeFrame>> map = new TreeMap<>();

    private int quorum;
    //How many TimeFrames to store per type
    private int maxSize;
    private Map<Integer, PublicKey> publicKeys;

    public Registry(int quorum, int maxSize, Map<Integer, PublicKey> publicKeys) {
        this.quorum = quorum;
        this.maxSize = maxSize;
        this.publicKeys = publicKeys;
    }

    public <T extends SensorMessage> boolean store(MessageWithDigest<T> messageWithDigest) {
        T message = messageWithDigest.getContent();
        if (!verifySignature(messageWithDigest)) {
            System.out.println("[Registry] Message with invalid key! Returning");
            return false;
        }
        SortedMap<Integer, TimeFrame> timeFrames = map.get(message.getType());
        if (timeFrames == null) {
            System.out.println("No type found for the message. Creating type " + message.getType().name());
            timeFrames = new TreeMap<>(Comparator.comparing(Integer::intValue).reversed());
            map.put(message.getType(), timeFrames);
        }
        TimeFrame<T> timeFrame = timeFrames.get(message.getSeqN());
        if (timeFrame == null) {
            System.out.println("No TimeFrame found for the message. Creating TimeFrame T" + message.getSeqN());
            timeFrame = new TimeFrame();
            if (timeFrames.keySet().size() >= maxSize) {
                System.out.println("Reached size limit. Removing oldest key " + timeFrames.lastKey());
                timeFrames.remove(timeFrames.lastKey());
            }
            timeFrames.put(message.getSeqN(), timeFrame);

        }
        return timeFrame.store(message);
    }

    private <T extends SensorMessage> boolean verifySignature(MessageWithDigest<T> messageWithDigest) {
        T contents = messageWithDigest.getContent();
        if (contents != null) {
            PublicKey pk = publicKeys.get(contents.getSender());
            return messageWithDigest.verify(pk);
        }
        return false;
    }

    private <T,U> U cast(T t) {
        return (U) t;
    }

    public <T extends SensorMessage> List<T> extractRecentValues(SensorMessage.Type type, int size, Function<TimeFrame<T>, T> extractor) {
        SortedMap<Integer, TimeFrame> timeFrames = map.get(type);
        if (timeFrames != null) {
            Stream<TimeFrame<T>> st0 = cast(timeFrames.values().stream().limit(size).filter(tf -> tf.sensorMessages.size() >= quorum));
            Stream<T> st1 = st0.map(extractor);
            return st1.collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public class TimeFrame<T extends SensorMessage> {
        private final Set<T> sensorMessages = new HashSet<>();

        //Returns true if timeFrame reached necessary quorum
        private boolean store(T sensorMessage) {
            sensorMessages.add(sensorMessage);
            System.out.println("Adding message to set");
            return reachedQuorum();
        }

        private boolean reachedQuorum() {
            return sensorMessages.size() == quorum;
        }
    }

    public static <T extends SensorMessage> T getMedian(TimeFrame<T> tf) {
        List<T> t = tf.sensorMessages.stream().sorted().collect(Collectors.toList());
        return t.get(t.size()/2);
    }
}
