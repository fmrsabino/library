package bftsmart.demo.adapt.policies;

import bftsmart.demo.adapt.messages.SensorMessage;

public interface AdaptPolicy<T extends SensorMessage> {
    void execute(T message);
}
