package bftsmart.demo.adapt;


import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultRecoverable;

public class AdaptServer extends DefaultRecoverable {

    private ServiceReplica replica;

    public AdaptServer(int id) {
        replica = new ServiceReplica(id, this, this);
    }

    @Override
    public void installSnapshot(byte[] state) {

    }

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
