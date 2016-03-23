package bftsmart.demo.adapt;

import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultRecoverable;

import java.io.*;

public class RealServer extends DefaultRecoverable {

    private ServiceReplica replica;
    private int id;
    private int internalState = 0;

    public RealServer(int id) {
        this.id = id;
        replica = new ServiceReplica(id, this, this);
    }

    @Override
    public void installSnapshot(byte[] state) {
        try {
            //System.out.println("setState called");
            ByteArrayInputStream bis = new ByteArrayInputStream(state);
            ObjectInput in = new ObjectInputStream(bis);
            internalState =  in.readInt();
            in.close();
            bis.close();
        } catch (Exception e) {
            System.err.println("[ERROR] Error deserializing state: "
                    + e.getMessage());
        }
    }

    @Override
    public byte[] getSnapshot() {
        try {
            //System.out.println("getState called");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeInt(internalState);
            out.flush();
            bos.flush();
            out.close();
            bos.close();
            return bos.toByteArray();
        } catch (Exception e) {
            System.err.println("[ERROR] Error serializing state: "
                    + e.getMessage());
            return "ERROR".getBytes();
        }
    }

    @Override
    public byte[][] appExecuteBatch(byte[][] commands, MessageContext[] msgCtxs) {
        return new byte[0][];
    }

    @Override
    public byte[] appExecuteUnordered(byte[] command, MessageContext msgCtx) {
        //System.out.println("[RealServer] RECEIVED!");
        /*if (id != 0) { return new byte[0];}
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
        }*/
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
