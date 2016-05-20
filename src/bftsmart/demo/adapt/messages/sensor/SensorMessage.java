package bftsmart.demo.adapt.messages.sensor;

import bftsmart.demo.adapt.messages.Message;

public abstract class SensorMessage<T extends SensorMessage> extends Message implements Comparable<T> {
    public enum Type {
        THREAT,
        WORKLOAD
    }

    private final Type type;

    public SensorMessage(int sender, Type type, int seqN) {
        super(seqN, sender);
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
