package bftsmart.demo.adapt.messages.sensor;

public class PingMessage implements SensorMessage {
    private String ip;
    private int port;

    public PingMessage(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
