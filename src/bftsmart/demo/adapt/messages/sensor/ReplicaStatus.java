package bftsmart.demo.adapt.messages.sensor;

import java.io.Serializable;

public class ReplicaStatus implements Serializable {
    private int smartId;
    private String ip;
    private String port;

    public ReplicaStatus(int smartId, String ip, String port) {
        this.smartId = smartId;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public int hashCode() {
        int result = smartId;
        result = 31 * result + ip.hashCode();
        result = 31 * result + port.hashCode();
        return result;
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
        return "ReplicaStatus{" +
                "smartId=" + smartId +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
