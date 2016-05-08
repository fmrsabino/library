package bftsmart.demo.adapt.messages.adapt;

import bftsmart.demo.adapt.messages.sensor.ReplicaStatus;

import java.util.ArrayList;
import java.util.List;

public class ChangeFMessage implements AdaptMessage {
    public static final int ADD_REPLICAS = 0;
    public static final int REMOVE_REPLICAS = 1;

    private final int command;
    private final List<ReplicaStatus> replicas;

    public ChangeFMessage(int command, List<ReplicaStatus> replicas) {
        this.command = command;
        this.replicas = replicas;
    }

    @Override
    public int hashCode() {
        int result = command;
        result = 31 * result + replicas.hashCode();
        return result;
    }

    public int getCommand() {
        return command;
    }

    public List<ReplicaStatus> getReplicas() {
        return replicas;
    }

    @Override
    public String toString() {
        return "ChangeFMessage{" +
                "command=" + command +
                ", replicas=" + replicas +
                '}';
    }
}
