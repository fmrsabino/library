package bftsmart.demo.adapt.messages.sensor;

public class BandwidthMessage implements SensorMessage {
    private final int bandwidth;

    public BandwidthMessage(int bandwidth) {
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
}
