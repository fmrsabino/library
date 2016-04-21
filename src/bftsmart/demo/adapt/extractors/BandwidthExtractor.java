package bftsmart.demo.adapt.extractors;

import bftsmart.demo.adapt.messages.sensor.BandwidthMessage;
import bftsmart.demo.adapt.util.StreamUtils;

import java.util.List;

public class BandwidthExtractor implements ValueExtractor<BandwidthMessage> {
    @Override
    public BandwidthMessage extract(List<BandwidthMessage> messages) {
        messages = StreamUtils.toList(messages.stream().sorted((sm1, sm2) -> sm1.getBandwidth() - sm2.getBandwidth()));
        //messages.stream().map(ThreatLevelMessage::getThreatLevel).forEach(System.out::println);

        int targetPosition = messages.size() / 2;
        BandwidthMessage bw = messages.get(targetPosition);
        //System.out.println(sm);
        return bw;
    }
}
