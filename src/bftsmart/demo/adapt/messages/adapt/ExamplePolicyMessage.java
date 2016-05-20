package bftsmart.demo.adapt.messages.adapt;

import bftsmart.demo.adapt.messages.sensor.ReplicaStatus;
import bftsmart.reconfiguration.ViewManager;

import java.util.ArrayList;
import java.util.List;

public class ExamplePolicyMessage extends AdaptMessage {
    public static final int ADD_REPLICAS = 0;
    public static final int REMOVE_REPLICAS = 1;
    public static final int DO_NOTHING = -1;

    private int command;
    private List<ReplicaStatus> replicas = new ArrayList<>();
    private int batchSize = 0;

    public ExamplePolicyMessage(int seqN, int sender) {
        super(seqN, sender);
    }

    /**
     * equals is used to add the message to the MessageMatcher set.
     * You should compare every date field (including sequence number and sender
     **/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExamplePolicyMessage that = (ExamplePolicyMessage) o;

        if (command != that.command) return false;
        if (batchSize != that.batchSize) return false;
        if (getSeqN() != that.getSeqN()) return false;
        if (getSender() != that.getSender()) return false;
        return replicas.equals(that.replicas);
    }

    /**
     * This hash code is used by the MessageMatcher to determine
     * if the data of the message is the same
     * IMPORTANT: the same message from different senders should generate the same hash code
     **/
    @Override
    public int hashCode() {
        int result = command;
        result = 31 * result + replicas.hashCode();
        result = 31 * result + batchSize;
        result = 31 * result + getSeqN();
        return result;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public List<ReplicaStatus> getReplicas() {
        return replicas;
    }

    public void setReplicas(List<ReplicaStatus> replicas) {
        this.replicas = replicas;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    public void execute() {
        System.out.println("EXECUTING POLICY MESSAGE");
        try {
            ViewManager vm = new ViewManager();

            switch (command) {
                case ADD_REPLICAS:
                    for (ReplicaStatus r : replicas) {
                        vm.addServer(r.getSmartId(), r.getIp(), Integer.parseInt(r.getPort()));
                        vm.executeUpdates();
                    }
                    break;
                case REMOVE_REPLICAS:
                    for (ReplicaStatus r : replicas) {
                        vm.removeServer(r.getSmartId());
                        vm.executeUpdates();
                    }
                    break;
            }

            if (batchSize == 0) {
                vm.changeMaxBatchSize(400);
            } else if (batchSize < 0) {
                vm.changeMaxBatchSize(600);
            } else if (batchSize > 0) {
                vm.changeMaxBatchSize(200);
            }

            vm.executeUpdates();
            vm.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
