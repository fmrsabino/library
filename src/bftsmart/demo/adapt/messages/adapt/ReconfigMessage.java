package bftsmart.demo.adapt.messages.adapt;

import bftsmart.demo.adapt.messages.sensor.ReplicaStatus;
import bftsmart.demo.adapt.messages.sensor.SensorMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReconfigMessage implements SensorMessage {
    public static final int ADD_REPLICAS = 0;
    public static final int REMOVE_REPLICAS = 1;

    private int command = -1;
    private List<ReplicaStatus> replicas = new ArrayList<>();

    public ReconfigMessage() {}

    public ReconfigMessage(int command, List<ReplicaStatus> replicas) {
        this.command = command;
        this.replicas = replicas;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        for (ReplicaStatus rs : replicas) {
            hash += rs.hashCode();
        }
        return hash + command;
    }

    public int getCommand() {
        return command;
    }

    public List<ReplicaStatus> getReplicas() {
        return replicas;
    }
}
