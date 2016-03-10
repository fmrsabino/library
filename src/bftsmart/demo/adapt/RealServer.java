package bftsmart.demo.adapt;

import bftsmart.demo.adapt.messages.*;
import bftsmart.demo.adapt.messages.ReplicaStatus;
import bftsmart.reconfiguration.VMServices;
import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplicaQ;
import bftsmart.tom.server.defaultservices.DefaultRecoverable;

public class RealServer extends DefaultRecoverable {

    private ServiceReplicaQ replica;
    private int id;

    public RealServer(int id) {
        this.id = id;
        replica = new ServiceReplicaQ(id, this, this);
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
        System.out.println("[RealServer] RECEIVED!");
        if (id != 0) { return new byte[0];}
        try {
            ReconfigMessage reconfigMessage = MessageSerializer.deserialize(command);
            if (reconfigMessage.getCommand() == ReconfigMessage.ADD_REPLICAS) {
                for (ReplicaStatus replicaStatus : reconfigMessage.getReplicas()) {
                    VMServices.main(new String[]{replicaStatus.getSmartId(), replicaStatus.getIp(), replicaStatus.getPort()});
                }
            } else if (reconfigMessage.getCommand() == ReconfigMessage.REMOVE_REPLICAS) {
                for (ReplicaStatus replicaStatus : reconfigMessage.getReplicas()) {
                    VMServices.main(new String[]{replicaStatus.getSmartId()});
                }
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
        new RealServer(Integer.parseInt(args[0]));
    }
}
