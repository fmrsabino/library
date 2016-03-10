package bftsmart.demo.adapt;


import bftsmart.demo.adapt.messages.MessageSerializer;
import bftsmart.demo.adapt.messages.ReconfigMessage;
import bftsmart.demo.adapt.messages.StatusMessage;
import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceProxy;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.ServiceReplicaQ;
import bftsmart.tom.server.defaultservices.DefaultRecoverable;

import java.util.ArrayList;
import java.util.List;

public class AdaptServer extends DefaultRecoverable {
    public final static String ADAPT_CONFIG_HOME = "adapt-config";
    private ServiceReplica replica;
    private int id;

    public AdaptServer(int id) {
        this.id = id;
        replica = new ServiceReplicaQ(id, ADAPT_CONFIG_HOME, this, this, null);
    }

    @Override
    public void installSnapshot(byte[] state) {}

    @Override
    public byte[] getSnapshot() {
        return new byte[0];
    }

    @Override
    public byte[][] appExecuteBatch(byte[][] commands, MessageContext[] msgCtxs) {
        return new byte[0][];
    }

    @Override
    public byte[] appExecuteUnordered(byte[] command, MessageContext msgCtx) {
        try {
            StatusMessage statusMessage = MessageSerializer.deserialize(command);
            List<bftsmart.demo.adapt.messages.ReplicaStatus> activeReplicas = statusMessage.getActiveReplicas();
            List<bftsmart.demo.adapt.messages.ReplicaStatus> inactiveReplicas = statusMessage.getInactiveReplicas();

            ReconfigMessage reconfigMessage = null;
            int n = statusMessage.getActiveReplicas().size();
            int f = (int) Math.ceil((n-1)/3);
            if (f > statusMessage.getThreatLevel()) { //decrement f
                System.out.println("Decrement F");
                List<bftsmart.demo.adapt.messages.ReplicaStatus> replicasToRemove = new ArrayList<>(3);
                if (activeReplicas.size() >= 3) {
                    replicasToRemove.add(activeReplicas.remove(activeReplicas.size() - 1));
                    replicasToRemove.add(activeReplicas.remove(activeReplicas.size() - 1));
                    replicasToRemove.add(activeReplicas.remove(activeReplicas.size() - 1));
                    reconfigMessage = new ReconfigMessage(ReconfigMessage.REMOVE_REPLICAS, replicasToRemove);
                } else {
                    System.out.println("Not enough replicas to decrement F");
                }
            } else if (f < statusMessage.getThreatLevel()) { //increment f
                System.out.println("Increment F");
                if (inactiveReplicas.size() >= 3) {
                    List<bftsmart.demo.adapt.messages.ReplicaStatus> replicasToAdd = new ArrayList<>(3);
                    replicasToAdd.add(inactiveReplicas.remove(0));
                    replicasToAdd.add(inactiveReplicas.remove(0));
                    replicasToAdd.add(inactiveReplicas.remove(0));
                    reconfigMessage = new ReconfigMessage(ReconfigMessage.ADD_REPLICAS, replicasToAdd);
                } else {
                    System.out.println("Not enough replicas to increment F");
                }
            } else {
                System.out.println("Do Nothing");
            }
            if (reconfigMessage != null) {
                ServiceProxy serviceProxy = new ServiceProxy(10, "config");
                serviceProxy.setInvokeTimeout(0);
                serviceProxy.invokeUnordered(MessageSerializer.serialize(reconfigMessage));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Use: java AdaptServer <processId>");
            System.exit(-1);
        }
        new AdaptServer(Integer.parseInt(args[0]));
    }
}
