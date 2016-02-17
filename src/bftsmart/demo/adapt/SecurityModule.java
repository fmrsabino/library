package bftsmart.demo.adapt;

import bftsmart.tom.ServiceProxy;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class SecurityModule {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java ...SecurityModule <process id> <threatlevel>");
            System.out.println("       if <increment> equals 0 the request will be read-only");
            System.out.println("       default <number of operations> equals 1000");
            System.exit(-1);
        }

        int processId = Integer.parseInt(args[0]);
        int threatLevel = Integer.parseInt(args[1]);

        ServiceProxy serviceProxy = new ServiceProxy(processId);
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
