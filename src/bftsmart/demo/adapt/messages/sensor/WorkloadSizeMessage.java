package bftsmart.demo.adapt.messages.sensor;

public class WorkloadSizeMessage extends SensorMessage {
    private final Size workload;

    public enum Size {
        LOW,
        MEDIUM,
        HIGH
    }

    public WorkloadSizeMessage(int sensor, Type type, long sequenceNumber, Size workload) {
        super(sensor, type, sequenceNumber);
        this.workload = workload;
    }

    public Size getWorkload() {
        return workload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        WorkloadSizeMessage that = (WorkloadSizeMessage) o;

        return workload == that.workload;
    }
}
