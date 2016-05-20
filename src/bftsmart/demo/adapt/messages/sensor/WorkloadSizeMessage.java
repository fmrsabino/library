package bftsmart.demo.adapt.messages.sensor;

public class WorkloadSizeMessage extends SensorMessage<WorkloadSizeMessage> {
    private final Size workload;

    public enum Size {
        LOW,
        MEDIUM,
        HIGH
    }

    public WorkloadSizeMessage(int sensor, Type type, int sequenceNumber, Size workload) {
        super(sensor, type, sequenceNumber);
        this.workload = workload;
    }

    public Size getWorkload() {
        return workload;
    }

    @Override
    public int compareTo(WorkloadSizeMessage that) {
        return this.workload.ordinal() - that.workload.ordinal();
    }
}
