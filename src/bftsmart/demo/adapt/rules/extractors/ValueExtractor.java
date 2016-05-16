package bftsmart.demo.adapt.rules.extractors;

import bftsmart.demo.adapt.messages.sensor.SensorMessage;

import java.util.Collection;

public interface ValueExtractor<T extends SensorMessage> {
    T extract(Collection<T> messages);
}
