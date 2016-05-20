package bftsmart.demo.adapt.messages.sensor;

public class ThreatLevelMessage extends SensorMessage<ThreatLevelMessage> {
    private int threatLevel;

    public ThreatLevelMessage(int sensor, int sequenceNumber, int threatLevel) {
        super(sensor, Type.THREAT, sequenceNumber);
        this.threatLevel = threatLevel;
    }

    public int getThreatLevel() {
        return threatLevel;
    }

    @Override
    public int compareTo(ThreatLevelMessage that) {
        return this.getThreatLevel() - that.getThreatLevel();
    }
}
