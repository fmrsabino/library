package bftsmart.demo.adapt;


import bftsmart.demo.adapt.messages.StatusMessage;
import bftsmart.demo.adapt.policies.AdaptPolicy;
import bftsmart.demo.adapt.policies.Policies;
import bftsmart.demo.adapt.util.Constants;
import bftsmart.demo.adapt.util.MessageSerializer;
import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultRecoverable;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdaptServer extends DefaultRecoverable {
    public final static Configurations configurations = new Configurations();
    private int internalState = 0;
    private ServiceReplica replica;
    private int id;

    private List<StatusMessage> statusMessages = new ArrayList<>();

    public AdaptServer(int id) {
        this.id = id;
        replica = new ServiceReplica(id, Constants.ADAPT_HOME_FOLDER, this, this, null);
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
            if (statusMessages.size() <= getSensorsQuorum()) {
                statusMessages.add(sm);
                if (statusMessages.size() == getSensorsQuorum()) {
                    AdaptPolicy<StatusMessage> policy = Policies.getCurrentPolicy();
                    if (policy != null) {
                        policy.execute(statusMessages);
                    } else {
                        System.err.println("Error: Couldn't find policy.");
                    }
                    statusMessages.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[] {0};
    }

    private int getSensorsQuorum() {
        return 2 * getSensorsF() + 1;
    }

    private int getSensorsF() {
        try {
            Configuration config = configurations.properties(new File(Constants.ADAPT_CONFIG_PATH));
            int n = config.getInt(Constants.N_SENSORS_KEY);
            return (n - 1) / 3;
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return -1;
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