package bftsmart.demo.adapt.TTP;

import bftsmart.demo.adapt.messages.adapt.ChangeFMessage;
import bftsmart.demo.adapt.messages.sensor.ReplicaStatus;
import bftsmart.demo.adapt.util.MessageMatcher;
import bftsmart.reconfiguration.VMServices;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TTPServer extends ChannelInboundHandlerAdapter implements MessageListener<ChangeFMessage> {
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 7000;
    private MessageMatcher<ChangeFMessage> messageMatcher = new MessageMatcher<>(3);

    public static void main(String[] args) throws InterruptedException {
        new TTPServer().run();
    }

    private void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Started TTPServer Server");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepting client connection");
                TTPThread thread = new TTPThread(clientSocket);
                thread.setListener(this);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(ChangeFMessage message) {
        ChangeFMessage msg = messageMatcher.insertMessage(message);
        if (msg != null) {
            System.out.println("Reached Quorum of AdaptMessages");
            executeReconfiguration(msg);
        }
    }

    private void executeReconfiguration(ChangeFMessage message) {
        if (message.getCommand() == ChangeFMessage.ADD_REPLICAS) {
            System.out.println("ADDING REPLICAS");
            for (ReplicaStatus r : message.getReplicas()) {
                try {
                    VMServices.main(new String[] {r.getSmartId(), r.getIp(), r.getPort()});
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else if (message.getCommand() == ChangeFMessage.REMOVE_REPLICAS){
            System.out.println("REMOVING REPLICAS");
            for (ReplicaStatus r : message.getReplicas()) {
                try {
                    VMServices.main(new String[] {r.getSmartId()});
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
