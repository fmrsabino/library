package bftsmart.demo.adapt.messages.sensor;

import java.io.Serializable;

public class SensorMessage implements Serializable {
    public enum Type {
        CPU,
        BANDWIDTH,
        THREAT,
        WORKLOAD
    }

    private final int sensor;
    private final Type type;
    private final int sequenceNumber;

    public SensorMessage(int sensor, Type type, int sequenceNumber) {
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

    public int getSequenceNumber() {
        return sequenceNumber;
    }
}
