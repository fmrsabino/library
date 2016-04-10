package bftsmart.demo.adapt;


import bftsmart.demo.adapt.messages.StatusMessage;
import bftsmart.demo.adapt.policies.AdaptPolicy;
import bftsmart.demo.adapt.policies.MiddleValue;
import bftsmart.demo.adapt.util.MessageSerializer;
import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultRecoverable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdaptServer extends DefaultRecoverable {
    public static final int N_SENSORS = 4;
    public final static String ADAPT_CONFIG_HOME = "adapt-config";
    private int internalState = 0;
    private ServiceReplica replica;
    private int id;

    private List<StatusMessage> statusMessages = new ArrayList<>();

    public AdaptServer(int id) {
        this.id = id;
        replica = new ServiceReplica(id, ADAPT_CONFIG_HOME, this, this, null);
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
        byte [][] replies = new byte[commands.length][];
        for (int i = 0; i < commands.length; i++) {
            if (msgCtxs != null && msgCtxs[i] != null) {
                replies[i] = executeSingle(commands[i], msgCtxs[i]);
            }
        }
        return replies;
    }

    private byte[] executeSingle(byte[] command, MessageContext msgCtx) {
        try {
            StatusMessage sm = MessageSerializer.deserialize(command);
            int f = replica.getSVController().getCurrentViewF();
            if (statusMessages.size() <= 2*f+1) {
                statusMessages.add(sm);
                if (statusMessages.size() == 2*f+1) {
                    AdaptPolicy<StatusMessage> adaptPolicy = new MiddleValue();
                    adaptPolicy.execute(statusMessages);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[] {0};
    }

    @Override
    public byte[] appExecuteUnordered(byte[] command, MessageContext msgCtx) {
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