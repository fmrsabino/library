package bftsmart.demo.adapt;

import bftsmart.reconfiguration.VMServices;
import bftsmart.tom.ServiceProxy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
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
            if (reply != null) {
                DataInputStream dis = new DataInputStream(new ByteArrayInputStream(reply));
                String line = dis.readUTF();
                dis.close();
                System.out.println(line);
                String[] parts = line.toString().split(" ");
                for (int i = 0; i < parts.length; i++) {
                    System.out.println(i + " - " + parts[i]);
                }
                if (parts.length == 3) {
                    System.out.println("Sending ADD command");
                    VMServices.main(new String[] {parts[0], parts[1], parts[2]});
                } else if (parts.length == 1) {
                    System.out.println("Sending RM command");
                    VMServices.main(new String[] {parts[0]});
                } else {
                    System.out.println("else");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
