package bftsmart.demo.adapt.extractors;

import bftsmart.demo.adapt.messages.sensor.SensorMessage;
import bftsmart.demo.adapt.util.StreamUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

class MiddleValueExtractor<T extends SensorMessage> implements ValueExtractor<T> {
    private final Comparator<T> comparator;

    MiddleValueExtractor(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    public T extract(Collection<T> messages) {
        if (messages == null || messages.isEmpty()) {
            return null;
        }
        List<T> list = StreamUtils.toList(messages.stream().sorted(comparator));
        return list.get(list.size() / 2);
    }
}
