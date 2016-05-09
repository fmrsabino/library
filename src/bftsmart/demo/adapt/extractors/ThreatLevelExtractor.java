package bftsmart.demo.adapt.extractors;

import bftsmart.demo.adapt.messages.sensor.ThreatLevelMessage;
import bftsmart.demo.adapt.util.StreamUtils;

import java.util.Collection;
import java.util.List;

public class ThreatLevelExtractor implements ValueExtractor<ThreatLevelMessage> {
    @Override
    public ThreatLevelMessage extract(Collection<ThreatLevelMessage> messages) {
        List<ThreatLevelMessage> list = StreamUtils.toList(messages.stream().sorted((sm1, sm2) -> sm1.getThreatLevel() - sm2.getThreatLevel()));
        int targetPosition = list.size() / 2;
        return list.get(targetPosition);
    }
}
