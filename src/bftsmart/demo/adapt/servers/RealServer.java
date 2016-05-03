package bftsmart.demo.adapt.servers;

import bftsmart.demo.adapt.messages.adapt.AdaptMessage;
import bftsmart.demo.adapt.messages.adapt.ChangeTimeoutMessage;
import bftsmart.demo.adapt.messages.sensor.PingMessage;
import bftsmart.demo.adapt.util.MessageMatcher;
import bftsmart.demo.adapt.util.MessageSerializer;
import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultRecoverable;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class RealServer extends DefaultRecoverable {
    private ServiceReplica replica;
    private long currentTimeout;
    private MessageMatcher<AdaptMessage> messageMatcher = new MessageMatcher<>(3);
    private int id = -1;

    @Override
    public void installSnapshot(byte[] state) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(state);
            ObjectInput in = new ObjectInputStream(bis);
            currentTimeout =  in.readLong();
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
        }
        return new byte[]{0};
    }

    private byte[] executeClientRequest(byte[] command, MessageContext msgCtx) {
        return new byte[]{0};
    }

    @Override
    public byte[] appExecuteUnordered(byte[] command, MessageContext msgCtx) {
        try {
            new SensorReplier(MessageSerializer.deserialize(command)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{0};
    }

    private class SensorReplier extends Thread {
        private final PingMessage pingMessage;

        public SensorReplier(PingMessage pingMessage) {
            this.pingMessage = pingMessage;
        }

        @Override
        public void run() {
            try {
                Socket socket = new Socket(pingMessage.getIp(), pingMessage.getPort());
                OutputStream os = socket.getOutputStream();
                os.write(id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public RealServer(int id) {
        this.id = id;
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
