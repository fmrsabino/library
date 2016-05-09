package bftsmart.demo.adapt.servers;


import bftsmart.demo.adapt.extractors.Extractors;
import bftsmart.demo.adapt.extractors.ValueExtractor;
import bftsmart.demo.adapt.messages.sensor.SensorMessage;
import bftsmart.demo.adapt.policies.AdaptPolicy;
import bftsmart.demo.adapt.policies.Policies;
import bftsmart.demo.adapt.util.BftUtils;
import bftsmart.demo.adapt.util.Constants;
import bftsmart.demo.adapt.util.MessageSerializer;
import bftsmart.demo.adapt.util.Register;
import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultRecoverable;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

public class AdaptReplica extends DefaultRecoverable {
    private final Configurations configurations = new Configurations();
    private ServiceReplica replica;
    private int id;

    private Map<SensorMessage.Type, Long> currentExecutions = new TreeMap<>();
    private Register register;

    public AdaptReplica(int id) {
        this.id = id;
        register = new Register(getSensorsQuorum());
        replica = new ServiceReplica(id, Constants.ADAPT_HOME_FOLDER, this, this, null);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Use: java AdaptReplica <processId>");
            System.exit(-1);
        }
        new AdaptReplica(Integer.parseInt(args[0]));
    }

    @Override
    public void installSnapshot(byte[] state) {
        try {
            //System.out.println("setState called");
            ByteArrayInputStream bis = new ByteArrayInputStream(state);
            ObjectInput in = new ObjectInputStream(bis);
            currentExecutions =  (Map<SensorMessage.Type, Long>) in.readObject();
            register = (Register) in.readObject();
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
            out.writeObject(currentExecutions);
            out.writeObject(register);
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
            System.out.println("Received message:" + sm);
            Long curr = currentExecutions.get(sm.getType());
            if (curr == null) {
                curr = 0L;
                currentExecutions.put(sm.getType(), curr);
            }
            if (sm.getSequenceNumber() < curr) {
                System.out.println("Sequence number of message already executed. Discarding...");
                return new byte[]{0};
            }
            if (register.store(sm)) {
                executePolicy(sm.getType(), curr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[] {0};
    }

    private void executePolicy(SensorMessage.Type type, long currentExecution) {
        System.out.println(String.format("[T%d] Preparing to execute policy for %s", currentExecution, type.name()));
        ValueExtractor valueExtractor = Extractors.getCurrentExtractor();
        if (valueExtractor != null) {
            SensorMessage result = register.extract(type, currentExecution, valueExtractor);
            if (result == null) {
                System.out.println(String.format("[T%d] Don't have needed quorum for %s", currentExecution, type.name()));
                return;
            }
            AdaptPolicy policy = Policies.getCurrentPolicy();
            if (policy != null) {
                System.out.println(String.format("[T%d] Executing policy with message %s", currentExecution, result));
                policy.execute(id, result);
                currentExecutions.put(type, currentExecution + 1);
                executePolicy(type, currentExecution + 1); //try to execute next sequence (we may already have the needed messages)
            } else {
                System.out.println("[Error] Couldn't find policy!");
            }
        } else {
            System.out.println("[Error] Couldn't find value extractor!");
        }
    }

    private int getSensorsQuorum() {
        try {
            Configuration config = configurations.properties(new File(Constants.ADAPT_CONFIG_PATH));
            int f = BftUtils.getF(config.getInt(Constants.N_SENSORS_KEY));
            return BftUtils.getQuorum(f);
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