package bftsmart.demo.adapt.extractor;

import bftsmart.demo.adapt.messages.SensorMessage;

import java.util.List;

public interface ValueExtractor<T extends SensorMessage> {
    T extract(List<T> messages);
}
