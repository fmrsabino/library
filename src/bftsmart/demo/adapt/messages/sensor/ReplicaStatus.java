package bftsmart.demo.adapt.messages.sensor;

import java.io.Serializable;

public class ReplicaStatus implements Serializable {
    private int smartId;
    private String ip;
    private String port;
    private boolean active;

    public ReplicaStatus(int smartId, String ip, String port) {
        this.smartId = smartId;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public int hashCode() {
        if (active) {
            return smartId + ip.hashCode() + port.hashCode() + 1;
        } else {
            return smartId + ip.hashCode() + port.hashCode();
        }
    }

    public int getSmartId() {
        return smartId;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    @Override
    public String toString() {
        return smartId + " " + ip + " " + port;
    }
}
