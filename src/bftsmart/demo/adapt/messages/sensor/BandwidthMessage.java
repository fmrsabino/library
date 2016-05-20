package bftsmart.demo.adapt.messages.sensor;

public class BandwidthMessage extends SensorMessage<BandwidthMessage> {
    private final int bandwidth;

    public BandwidthMessage(int sensor, int sequenceNumber, int bandwidth) {
        super(sensor, Type.BANDWIDTH, sequenceNumber);
        this.bandwidth = bandwidth;
    }

    public int getBandwidth() {
        return bandwidth;
    }

    @Override
    public int hashCode() {
        return bandwidth;
    }

    @Override
    public String toString() {
        return "BandwidthMessage{" +
                "bandwidth=" + bandwidth +
                '}';
    }

    @Override
    public int compareTo(BandwidthMessage that) {
        return this.bandwidth - that.bandwidth;
    }
}
