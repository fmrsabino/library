package bftsmart.demo.adapt.messages.sensor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ThreatLevelMessage extends SensorMessage implements Comparable <ThreatLevelMessage> {
    private List<ReplicaStatus> activeReplicas = new ArrayList<>();
    private List<ReplicaStatus> inactiveReplicas = new ArrayList<>();
    private int threatLevel;

    public ThreatLevelMessage(int sensor, int sequenceNumber,
                              List<ReplicaStatus> activeReplicas, List<ReplicaStatus> inactiveReplicas,
                              int threatLevel) {
        super(sensor, Type.THREAT, sequenceNumber);
        this.activeReplicas = activeReplicas;
        this.inactiveReplicas = inactiveReplicas;
        this.threatLevel = threatLevel;
    }

    public List<ReplicaStatus> getActiveReplicas() {
        return activeReplicas;
    }

    public List<ReplicaStatus> getInactiveReplicas() {
        return inactiveReplicas;
    }

    public int getThreatLevel() {
        return threatLevel;
    }

    @Override
    public int hashCode() {
        int result = activeReplicas.hashCode();
        result = 31 * result + inactiveReplicas.hashCode();
        result = 31 * result + threatLevel;
        return result;
    }

    @Override
    public String toString() {
        return "ThreatLevelMessage{" +
                "activeReplicas=" + activeReplicas +
                ", inactiveReplicas=" + inactiveReplicas +
                ", threatLevel=" + threatLevel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ThreatLevelMessage that = (ThreatLevelMessage) o;

        if (threatLevel != that.threatLevel) return false;
        if (!activeReplicas.equals(that.activeReplicas)) return false;
        return inactiveReplicas.equals(that.inactiveReplicas);
    }

    @Override
    public int compareTo(ThreatLevelMessage that) {
        return this.getThreatLevel() - that.getThreatLevel();
    }
}
