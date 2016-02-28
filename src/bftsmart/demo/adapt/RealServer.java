package bftsmart.demo.adapt;

import bftsmart.reconfiguration.VMServices;
import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplicaQ;
import bftsmart.tom.server.defaultservices.DefaultRecoverable;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class RealServer extends DefaultRecoverable {

    private ServiceReplicaQ replica;

    public RealServer(int id) {
        replica = new ServiceReplicaQ(id, this, this);
    }

    @Override
    public void installSnapshot(byte[] state) {}

    @Override
    public byte[] getSnapshot() {
        return new byte[0];
    }

    @Override
    public byte[][] appExecuteBatch(byte[][] commands, MessageContext[] msgCtxs) {
        return new byte[0][];
    }

    @Override
    public byte[] appExecuteUnordered(byte[] command, MessageContext msgCtx) {
        System.out.println("[RealServer] RECEIVED!");
        try {
            if (command != null) {
                //DataInputStream dis = new DataInputStream(new ByteArrayInputStream(command));
                //String line = dis.readUTF();
                //dis.close();
                //System.out.println(line);
                String line = new String(command, "UTF-8");
                String[] parts = line.split(" ");
                /*for (int i = 0; i < parts.length; i++) {
                    System.out.println(i + " - " + parts[i]);
                }*/
                if (parts.length == 4) {
                    System.out.println("Sending ADD command");
                    VMServices.main(new String[] {parts[1], parts[2], parts[3]});
                } else if (parts.length == 2) {
                    System.out.println("Sending RM command");
                    VMServices.main(new String[] {parts[1]});
                } else {
                    System.out.println("else");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Use: java AdaptServer <processId>");
            System.exit(-1);
        }
        new RealServer(Integer.parseInt(args[0]));
    }
}
