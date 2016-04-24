package bftsmart.demo.adapt.servers;

import bftsmart.demo.adapt.messages.adapt.AdaptMessage;
import bftsmart.demo.adapt.messages.adapt.ChangeTimeoutMessage;
import bftsmart.demo.adapt.messages.adapt.ChangeUseSignaturesMessage;
import bftsmart.demo.adapt.util.MessageMatcher;
import bftsmart.demo.adapt.util.MessageSerializer;
import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultRecoverable;

import java.io.*;

public class RealServer extends DefaultRecoverable {
    private ServiceReplica replica;
    private long currentTimeout;
    private int useSignatures;
    private MessageMatcher<AdaptMessage> messageMatcher = new MessageMatcher<>(3);

    @Override
    public void installSnapshot(byte[] state) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(state);
            ObjectInput in = new ObjectInputStream(bis);
            currentTimeout =  in.readLong();
            useSignatures = in.readInt();
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
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeLong(currentTimeout);
            out.writeInt(useSignatures);
            out.flush();
            bos.flush();
            out.close();
            bos.close();
            return bos.toByteArray();
        } catch (IOException ioe) {
            System.err.println("[ERROR] Error serializing state: "
                    + ioe.getMessage());
            return "ERROR".getBytes();
        }
    }

    @Override
    public byte[][] appExecuteBatch(byte[][] commands, MessageContext[] msgCtxs) {
        byte [][] replies = new byte[commands.length][];
        for (int i = 0; i < commands.length; i++) {
            if(msgCtxs != null && msgCtxs[i] != null) {
                replies[i] = executeSingle(commands[i],msgCtxs[i]);
            }
            else executeSingle(commands[i],null);
        }
        return replies;
    }

    private byte[] executeSingle(byte[] command, MessageContext msgCtx) {
        try {
            AdaptMessage adaptMessage = MessageSerializer.deserialize(command);
            AdaptMessage result = messageMatcher.insertMessage(adaptMessage);
            if (result != null) {
                return executeAdaptRequest(adaptMessage);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Message is not from Adapt System. It's a client request");
            return executeClientRequest(command, msgCtx);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[]{0};
    }

    private byte[] executeAdaptRequest(AdaptMessage message) {
        if (message instanceof ChangeTimeoutMessage) {
            ChangeTimeoutMessage timeoutMessage = (ChangeTimeoutMessage) message;
            System.out.println("Received a ChangeTimeoutMessage");
            currentTimeout = timeoutMessage.getTimeoutValue();
            replica.setRequestTimeout(currentTimeout);
        } else if (message instanceof ChangeUseSignaturesMessage) {
            ChangeUseSignaturesMessage signaturesMessage = (ChangeUseSignaturesMessage) message;
            System.out.println("Received a ChangeUseSignaturesMessage");
            useSignatures = signaturesMessage.getUseSignatures();
            setUseSignatures(useSignatures);

        }
        return new byte[]{0};
    }

    private byte[] executeClientRequest(byte[] command, MessageContext msgCtx) {
        return new byte[]{0};
    }

    @Override
    public byte[] appExecuteUnordered(byte[] command, MessageContext msgCtx) {
        return new byte[]{0};
    }

    public RealServer(int id) {
        replica = new ServiceReplica(id, this, this);
    }

    public static void main(String[] args){
        if(args.length < 1) {
            System.out.println("Use: java RealServer <processId>");
            System.exit(-1);
        }
        new RealServer(Integer.parseInt(args[0]));
    }
}
