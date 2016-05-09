package bftsmart.demo.adapt.extractors;

import bftsmart.demo.adapt.messages.sensor.SensorMessage;

import java.util.Collection;
import java.util.List;

public interface ValueExtractor<T extends SensorMessage> {
    T extract(Collection<T> messages);
}
