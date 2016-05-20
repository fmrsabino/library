package bftsmart.demo.adapt.replicas;


import bftsmart.demo.adapt.messages.MessageWithDigest;
import bftsmart.demo.adapt.messages.PolicyNotification;
import bftsmart.demo.adapt.messages.sensor.SensorMessage;
import bftsmart.demo.adapt.rules.checkers.ValueChecker;
import bftsmart.demo.adapt.rules.checkers.ValueCheckers;
import bftsmart.demo.adapt.rules.policies.AdaptPolicy;
import bftsmart.demo.adapt.rules.policies.Policies;
import bftsmart.demo.adapt.util.*;
import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultRecoverable;
import bftsmart.tom.server.defaultservices.DefaultReplier;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.*;

public class AdaptReplica extends DefaultRecoverable {
    private final Configurations configurations = new Configurations();
    private ServiceReplica replica;
    private int id;

    private Registry registry;
    private MessageMatcher<PolicyNotification> notifications;
    private int policySeqN = 0;

    public AdaptReplica(int id) throws IOException {
        this.id = id;
        registry = new Registry(getSensorsQuorum(), 1000, FileUtils.readPublicKeys(Constants.SENSOR_KEYS_PATH, "RSA"));
        notifications = new MessageMatcher<>(3, 100, FileUtils.readPublicKeys(Constants.ADAPT_KEYS_PATH, "RSA"));
        replica = new ServiceReplica(id, Constants.ADAPT_HOME, this, this, null, new DefaultReplier());
    }

    public static void main(String[] args) throws IOException {
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
            notifications = (MessageMatcher<PolicyNotification>) in.readObject();
            policySeqN = in.readInt();
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
            out.writeObject(notifications);
            out.writeInt(policySeqN);
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
            if (o instanceof MessageWithDigest) {
                MessageWithDigest messageWithDigest = (MessageWithDigest) o;
                Object o2 = messageWithDigest.getContent();
                if (o2 instanceof SensorMessage) {
                    sensorMessageReceived(messageWithDigest);
                } else if (o2 instanceof PolicyNotification) {
                    adaptMessageReceived(messageWithDigest);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[] {0};
    }

    private void sensorMessageReceived(MessageWithDigest messageWithDigest) {
        if (registry.store(messageWithDigest)) { //We have a new value for a TimeFrame
            ValueChecker vc = ValueCheckers.getCurrentChecker(registry, id);
            if (vc != null) {
                vc.check(policySeqN);
            }
        }
    }

    private void adaptMessageReceived(MessageWithDigest messageWithDigest) {
        PolicyNotification policyNotification = notifications.store(messageWithDigest);
        if (policyNotification != null) {
            AdaptPolicy policy = Policies.getCurrentPolicy();
            if (policy != null) {
                policy.execute(policySeqN, id, registry);
                notifications.setAsExecuted(policySeqN);
                policySeqN++;
            } else {
                System.out.println("[Error] Couldn't find policy!");
            }
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