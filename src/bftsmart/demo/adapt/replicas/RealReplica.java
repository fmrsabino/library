package bftsmart.demo.adapt.replicas;

import bftsmart.demo.adapt.util.BftUtils;
import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;

import java.io.*;

public class RealReplica extends ReconfigurableReplica {
    private ServiceReplica replica;
    private int id = -1;

    @Override
    public void installSnapshot(byte[] state) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(state);
            ObjectInput in = new ObjectInputStream(bis);
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
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.flush();
            bos.flush();
            out.close();
            bos.close();
            return bos.toByteArray();
        } catch (IOException ioe) {
            System.err.println("[ERROR] Error serializing state: "
                    + ioe.getMessage());
            return "ERROR".getBytes();
        }
    }

    @Override
    public byte[][] appExecuteBatch(byte[][] commands, MessageContext[] msgCtxs) {
        byte [][] replies = new byte[commands.length][];
        for (int i = 0; i < commands.length; i++) {
            if(msgCtxs != null && msgCtxs[i] != null) {
                replies[i] = executeSingle(commands[i],msgCtxs[i]);
            }
            else executeSingle(commands[i],null);
        }

        return replies;
    }

    private byte[] executeSingle(byte[] command, MessageContext msgCtx) {
        return new byte[]{0};
    }

    @Override
    public byte[] appExecuteUnordered(byte[] command, MessageContext msgCtx) {
        return new byte[]{0};
    }

    private RealReplica(int id) throws IOException {
        super("127.0.0.1", 11000 + (id * 10) + 5, BftUtils.getF(4) + 1);
        this.id = id;
        replica = new ServiceReplica(id, this, this);
    }

    public static void main(String[] args) throws IOException {
        if(args.length < 1) {
            System.out.println("Use: java RealReplica <processId>");
            System.exit(-1);
        }
        new RealReplica(Integer.parseInt(args[0]));
    }
}
