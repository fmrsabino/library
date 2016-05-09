package bftsmart.demo.adapt.extractors;

import bftsmart.demo.adapt.messages.sensor.BandwidthMessage;
import bftsmart.demo.adapt.util.StreamUtils;

import java.util.Collection;
import java.util.List;

public class BandwidthExtractor implements ValueExtractor<BandwidthMessage> {
    @Override
    public BandwidthMessage extract(Collection<BandwidthMessage> messages) {
        List<BandwidthMessage> list = StreamUtils.toList(messages.stream().sorted((sm1, sm2) -> sm1.getBandwidth() - sm2.getBandwidth()));
        int targetPosition = list.size() / 2;
        return list.get(targetPosition);
    }
}
