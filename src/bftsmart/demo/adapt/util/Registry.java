package bftsmart.demo.adapt.util;

import bftsmart.demo.adapt.messages.MessageWithDigest;
import bftsmart.demo.adapt.messages.sensor.BandwidthMessage;
import bftsmart.demo.adapt.messages.sensor.SensorMessage;

import java.io.IOException;
import java.io.Serializable;
import java.security.PrivateKey;
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

    private <T extends SensorMessage> boolean verifySignature(MessageWithDigest<T> messageWithDigest) {
        T contents = messageWithDigest.getContent();
        if (contents != null) {
            PublicKey pk = publicKeys.get(contents.getSensor());
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
        return null;
    }

    public class TimeFrame<T extends SensorMessage> {
        private int sequenceN;
        private final Set<T> sensorMessages = new HashSet<>();

        public TimeFrame(int sequenceN) {
            this.sequenceN = sequenceN;
        }

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

    private Registry() {
        this.quorum = 3;
        this.maxSize = 2;

        PrivateKey privateKey0 = SecurityUtils.getPrivateKey("sensor/keys/privatekey0", "RSA");
        PrivateKey privateKey1 = SecurityUtils.getPrivateKey("sensor/keys/privatekey1", "RSA");
        PrivateKey privateKey2 = SecurityUtils.getPrivateKey("sensor/keys/privatekey2", "RSA");
        PrivateKey privateKey3 = SecurityUtils.getPrivateKey("sensor/keys/privatekey3", "RSA");

        try {
            this.publicKeys = FileUtils.readPublicKeys("sensor/keys", "RSA");
        } catch (IOException e) {
            e.printStackTrace();
        }

        MessageWithDigest<BandwidthMessage> bm0 = new MessageWithDigest<>(new BandwidthMessage(0, 0, 10));
        bm0.sign(privateKey0);
        MessageWithDigest<BandwidthMessage> bm1 = new MessageWithDigest<>(new BandwidthMessage(1, 0, 20));
        bm1.sign(privateKey1);
        MessageWithDigest<BandwidthMessage> bm2 = new MessageWithDigest<>(new BandwidthMessage(2, 0, 30));
        bm2.sign(privateKey2);
        MessageWithDigest<BandwidthMessage> bm3 = new MessageWithDigest<>(new BandwidthMessage(0, 1, 50));
        bm3.sign(privateKey0);
        MessageWithDigest<BandwidthMessage> bm4 = new MessageWithDigest<>(new BandwidthMessage(1, 1, 100));
        bm4.sign(privateKey1);
        MessageWithDigest<BandwidthMessage> bm5 = new MessageWithDigest<>(new BandwidthMessage(2, 1, 150));
        bm5.sign(privateKey2);


        /*List l = new ArrayList<>();
        SensorMessage tm1 = new ThreatLevelMessage(0, 0, l, l, 1);
        SensorMessage tm2 = new ThreatLevelMessage(1, 0, l, l, 2);
        SensorMessage tm3 = new ThreatLevelMessage(2, 0, l, l, 3);
        SensorMessage tm4 = new ThreatLevelMessage(0, 1, l, l, 4);
        SensorMessage tm5 = new ThreatLevelMessage(1, 1, l, l, 5);
        SensorMessage tm6 = new ThreatLevelMessage(2, 1, l, l, 6);*/

        MessageWithDigest[] msgs = new MessageWithDigest[] {
                bm0, bm1, bm2, bm3, bm4, bm5
        };
        for (MessageWithDigest msg : msgs) {
            store(msg);
        }

        Collection<BandwidthMessage> msgx = extractRecentValues(SensorMessage.Type.BANDWIDTH, 2, Registry::getMedian);
        for (BandwidthMessage bm : msgx) {
            System.out.println(bm);
        }

        /*Collection<ThreatLevelMessage> msgx1 = extractRecentValues(SensorMessage.Type.THREAT, 2, Registry::getMedian);
        for (ThreatLevelMessage bm : msgx1) {
            System.out.println(bm);
        }*/

    }

    public static <T extends SensorMessage> T getMedian(TimeFrame<T> tf) {
        List<T> t = tf.sensorMessages.stream().sorted().collect(Collectors.toList());
        return t.get(t.size()/2);
    }

    public static void main(String[] args) {
        new Registry();
    }
}
