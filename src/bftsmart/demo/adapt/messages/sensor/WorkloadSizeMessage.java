package bftsmart.demo.adapt.messages.sensor;

public class WorkloadSizeMessage extends SensorMessage<WorkloadSizeMessage> {
    private final int workload;

    public WorkloadSizeMessage(int sensor, int sequenceNumber, int workload) {
        super(sensor, Type.WORKLOAD, sequenceNumber);
        this.workload = workload;
    }

    public int getWorkload() {
        return workload;
    }

    @Override
    public int compareTo(WorkloadSizeMessage that) {
        return this.workload - that.workload;
    }
}
