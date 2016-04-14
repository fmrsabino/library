package bftsmart.demo.adapt.extractor;

import bftsmart.demo.adapt.messages.ThreatLevelMessage;
import bftsmart.demo.adapt.util.StreamUtils;

import java.util.List;

public class ThreatLevelExtractor implements ValueExtractor<ThreatLevelMessage> {
    @Override
    public ThreatLevelMessage extract(List<ThreatLevelMessage> messages) {
        messages = StreamUtils.toList(messages.stream().sorted((sm1, sm2) -> sm1.getThreatLevel() - sm2.getThreatLevel()));
        //messages.stream().map(ThreatLevelMessage::getThreatLevel).forEach(System.out::println);

        int targetPosition = messages.size() / 2;
        ThreatLevelMessage sm = messages.get(targetPosition);
        //System.out.println(sm);
        return sm;
    }
}
