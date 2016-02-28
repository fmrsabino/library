package bftsmart.demo.adapt;

import bftsmart.tom.ServiceProxy;
import bftsmart.tom.core.messages.TOMMessageType;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class SecurityModule {

    private static int processId;
    private static int threatLevel;

    private static final int ADD_REPLICA = 1;
    private static final int REMOVE_REPLICA = -1;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java ...SecurityModule <process id> <threatlevel>");
            System.out.println("       if <increment> equals 0 the request will be read-only");
            System.out.println("       default <number of operations> equals 1000");
            System.exit(-1);
        }

        processId = Integer.parseInt(args[0]);
        threatLevel = Integer.parseInt(args[1]);

        sendThreatLevel("adapt-config");
        //sendThreatLevel("");
    }

    private static void sendThreatLevel(String configHome) {
        ServiceProxy serviceProxy = new ServiceProxy(processId, configHome);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(4);
            new DataOutputStream(out).writeInt(threatLevel);

            System.out.println("Sending threat level of: " + threatLevel);
            byte[] reply = serviceProxy.invokeUnordered(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
