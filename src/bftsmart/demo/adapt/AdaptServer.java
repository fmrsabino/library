package bftsmart.demo.adapt;


import bftsmart.demo.adapt.extractor.Extractors;
import bftsmart.demo.adapt.extractor.ValueExtractor;
import bftsmart.demo.adapt.messages.SensorMessage;
import bftsmart.demo.adapt.policies.AdaptPolicy;
import bftsmart.demo.adapt.policies.Policies;
import bftsmart.demo.adapt.util.BftUtils;
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

    private List<SensorMessage> sensorMessages = new ArrayList<>();

    public AdaptServer(int id) {
        this.id = id;
        replica = new ServiceReplica(id, Constants.ADAPT_HOME_FOLDER, this, this, null);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Use: java AdaptServer <processId>");
            System.exit(-1);
        }
        new AdaptServer(Integer.parseInt(args[0]));
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
            SensorMessage sm = MessageSerializer.deserialize(command);
            if (sensorMessages.size() <= BftUtils.getQuorum(getSensorsF())) {
                sensorMessages.add(sm);
            }
            if (sensorMessages.size() == BftUtils.getQuorum(getSensorsF())) {
                ValueExtractor valueExtractor = Extractors.getCurrentExtractor();
                if (valueExtractor != null) {
                    SensorMessage sensorMessage = valueExtractor.extract(sensorMessages);
                    AdaptPolicy policy = Policies.getCurrentPolicy();
                    if (policy != null) {
                        policy.execute(sensorMessage);
                    } else {
                        System.err.println("Error: Couldn't find policy.");
                    }
                } else {
                    System.err.println("Error: Couldn't find extractor");
                }
                sensorMessages.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[] {0};
    }

    private int getSensorsF() {
        try {
            Configuration config = configurations.properties(new File(Constants.ADAPT_CONFIG_PATH));
            int n = config.getInt(Constants.N_SENSORS_KEY);
            return BftUtils.getF(n);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public byte[] appExecuteUnordered(byte[] command, MessageContext msgCtx) {
        return new byte[0];
    }
}