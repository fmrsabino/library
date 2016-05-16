package bftsmart.demo.adapt.servers;


import bftsmart.demo.adapt.messages.adapt.AdaptMessage;
import bftsmart.demo.adapt.messages.sensor.SensorMessage;
import bftsmart.demo.adapt.rules.checkers.ValueChecker;
import bftsmart.demo.adapt.rules.checkers.ValueCheckers;
import bftsmart.demo.adapt.rules.policies.AdaptPolicy;
import bftsmart.demo.adapt.rules.policies.Policies;
import bftsmart.demo.adapt.util.BftUtils;
import bftsmart.demo.adapt.util.Constants;
import bftsmart.demo.adapt.util.MessageSerializer;
import bftsmart.demo.adapt.util.Registry;
import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultRecoverable;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.*;

public class AdaptReplica extends DefaultRecoverable {
    private final Configurations configurations = new Configurations();
    private ServiceReplica replica;
    private int id;

    private Registry registry;

    public AdaptReplica(int id) {
        this.id = id;
        registry = new Registry(getSensorsQuorum(), 1000);
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
            registry = (Registry) in.readObject();
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
            out.writeObject(registry);
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
            Object o = MessageSerializer.deserialize(command);
            if (o instanceof SensorMessage) {
                SensorMessage sm = (SensorMessage) o;
                sensorMessageReceived(sm);
            } else if (o instanceof AdaptMessage) {
                AdaptMessage am = (AdaptMessage) o;
                adaptMessageReceived(am);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[] {0};
    }

    private void sensorMessageReceived(SensorMessage sm) {
        if (registry.store(sm)) { //We have a new value for a TimeFrame
            ValueChecker vc = ValueCheckers.getCurrentChecker(registry);
            if (vc != null) {
                vc.check();
            }
        }
    }

    private void adaptMessageReceived(AdaptMessage am) {
        AdaptPolicy policy = Policies.getCurrentPolicy();
        if (policy != null) {
            policy.execute(id, registry);
        } else {
            System.out.println("[Error] Couldn't find policy!");
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
        return new byte[] {0};
    }
}