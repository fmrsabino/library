package bftsmart.demo.adapt.messages.sensor;

import java.util.ArrayList;
import java.util.List;

public class ThreatLevelMessage implements SensorMessage {
    private List<ReplicaStatus> activeReplicas = new ArrayList<>();
    private List<ReplicaStatus> inactiveReplicas = new ArrayList<>();
    private int threatLevel;

    public ThreatLevelMessage(List<ReplicaStatus> activeReplicas, List<ReplicaStatus> inactiveReplicas, int threatLevel) {
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
}
