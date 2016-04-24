package bftsmart.demo.adapt.extractors;

import bftsmart.demo.adapt.messages.sensor.SignaturesMessage;
import bftsmart.demo.adapt.util.StreamUtils;

import java.util.List;

public class UseSignaturesExtractor implements ValueExtractor<SignaturesMessage> {
    @Override
    public SignaturesMessage extract(List<SignaturesMessage> messages) {
        messages = StreamUtils.toList(messages.stream().sorted((sm1, sm2) -> sm1.getUseSignatures() - sm2.getUseSignatures()));
        //messages.stream().map(ThreatLevelMessage::getThreatLevel).forEach(System.out::println);

        int targetPosition = messages.size() / 2;
        SignaturesMessage sm = messages.get(targetPosition);
        //System.out.println(sm);
        return sm;
    }
}
