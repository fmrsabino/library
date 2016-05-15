package bftsmart.demo.adapt.messages.sensor;

import java.io.Serializable;

public class SensorMessage implements Serializable {
    public enum Type {
        CPU,
        BANDWIDTH,
        THREAT,
        BATCH_SIZE
    }

    private final int sensor;
    private final Type type;
    private final long sequenceNumber;

    public SensorMessage(int sensor, Type type, long sequenceNumber) {
        this.sensor = sensor;
        this.type = type;
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SensorMessage that = (SensorMessage) o;

        if (sensor != that.sensor) return false;
        if (sequenceNumber != that.sequenceNumber) return false;
        return type == that.type;

    }

    public int getSensor() {
        return sensor;
    }

    public Type getType() {
        return type;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }
}
