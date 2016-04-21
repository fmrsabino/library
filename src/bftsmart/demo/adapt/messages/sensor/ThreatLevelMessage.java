package bftsmart.demo.adapt.messages.sensor;

import java.util.ArrayList;
import java.util.List;

public class ThreatLevelMessage implements SensorMessage {
    private List<ReplicaStatus> activeReplicas = new ArrayList<>();
    private List<ReplicaStatus> inactiveReplicas = new ArrayList<>();
    private int threatLevel;

    public ThreatLevelMessage() {}

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
        int hash = 1;
        for (ReplicaStatus rs : activeReplicas) {
            hash += rs.hashCode();
        }
        for (ReplicaStatus rs : inactiveReplicas) {
            hash += rs.hashCode();
        }
        return hash + threatLevel;
    }

    @Override
    public String toString() {
        String result = "Active Replicas:\n";
        for (ReplicaStatus rs : activeReplicas) {
            result += rs + "\n";
        }
        result += "\nInactive Replicas:\n";
        for (ReplicaStatus rs : inactiveReplicas) {
            result += rs + "\n";
        }
        result += "\n" + "Threat Level = " + threatLevel + "\n";
        return result;
    }
}
