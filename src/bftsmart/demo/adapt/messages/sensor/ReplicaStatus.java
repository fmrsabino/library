package bftsmart.demo.adapt.messages.sensor;

import java.io.Serializable;

public class ReplicaStatus implements Serializable {
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
