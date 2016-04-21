package bftsmart.demo.adapt.messages.sensor;

public class BandwidthMessage implements SensorMessage {
    private final int bandwidth;

    public BandwidthMessage(int bandwidth) {
        this.bandwidth = bandwidth;
    }

    public int getBandwidth() {
        return bandwidth;
    }
}
