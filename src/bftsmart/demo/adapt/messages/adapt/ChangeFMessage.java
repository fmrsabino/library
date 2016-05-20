package bftsmart.demo.adapt.messages.adapt;

import bftsmart.demo.adapt.messages.sensor.ReplicaStatus;
import bftsmart.reconfiguration.VMServices;

public class ChangeFMessage extends AdaptMessage {
    public static final int ADD_REPLICAS = 0;
    public static final int REMOVE_REPLICAS = 1;

    private final int command;
    private final ReplicaStatus replica;

    public ChangeFMessage(int seqN, int sender, int command, ReplicaStatus replica) {
        super(seqN, sender);
        this.command = command;
        this.replica = replica;
    }

    @Override
    public boolean equals(Object o) {
        //A message with the same sequence number and sender is considered the same
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + command;
        result = 31 * result + replica.hashCode();
        return result;
    }

    public int getCommand() {
        return command;
    }

    public ReplicaStatus getReplica() {
        return replica;
    }

    @Override
    public String toString() {
        return "ChangeFMessage{" +
                "command=" + command +
                ", replica=" + replica +
                '}';
    }

    @Override
    public void execute() {
        try {
            if (command == ADD_REPLICAS) {
                VMServices.main(new String[] {replica.getSmartId()+"", replica.getIp(), replica.getPort()});
            } else if (command == REMOVE_REPLICAS) {
                VMServices.main(new String[] {replica.getSmartId()+""});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
