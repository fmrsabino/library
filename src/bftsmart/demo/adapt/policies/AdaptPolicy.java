package bftsmart.demo.adapt.policies;

import bftsmart.demo.adapt.messages.sensor.SensorMessage;

public interface AdaptPolicy<T extends SensorMessage> {
    void execute(int executorId, T message);
}
