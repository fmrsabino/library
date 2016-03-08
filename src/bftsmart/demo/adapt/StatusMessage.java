package bftsmart.demo.adapt;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StatusMessage implements Serializable {
    private List<ReplicaStatus> activeReplicas = new ArrayList<>();
    private List<ReplicaStatus> inactiveReplicas = new ArrayList<>();
    private int threatLevel;

    public StatusMessage() {}

    public StatusMessage(List<ReplicaStatus> activeReplicas, List<ReplicaStatus> inactiveReplicas, int threatLevel) {
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

    public static byte[] serialize(StatusMessage statusMessage) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(statusMessage);
        return out.toByteArray();
    }

    public static StatusMessage deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(in);
        return (StatusMessage) ois.readObject();
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

    public static class ReplicaStatus implements Serializable {
        private String smartId;
        private String ip;
        private String port;
        private boolean active;

        public ReplicaStatus(String smartId, String ip, String port, boolean active) {
            this.smartId = smartId;
            this.ip = ip;
            this.port = port;
            this.active = active;
        }

        @Override
        public int hashCode() {
            if (active) {
                return smartId.hashCode() + ip.hashCode() + port.hashCode() + 1;
            } else {
                return smartId.hashCode() + ip.hashCode() + port.hashCode();
            }
        }

        public String getSmartId() {
            return smartId;
        }

        public String getIp() {
            return ip;
        }

        public String getPort() {
            return port;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        @Override
        public String toString() {
            return smartId + " " + ip + " " + port;
        }
    }
}
