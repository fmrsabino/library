package bftsmart.demo.adapt.messages.sensor;

import java.io.Serializable;
import java.net.InetAddress;

public class PingMessage implements Serializable {
    private InetAddress ip;
    private int port;

    public PingMessage(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public InetAddress getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
