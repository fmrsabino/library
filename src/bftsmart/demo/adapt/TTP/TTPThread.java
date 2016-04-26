package bftsmart.demo.adapt.TTP;

import bftsmart.demo.adapt.messages.adapt.ChangeFMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class TTPThread extends Thread {
    private final Socket clientSocket;
    private MessageListener<ChangeFMessage> listener;

    public TTPThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void setListener(MessageListener<ChangeFMessage> listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            ChangeFMessage adaptMessage = (ChangeFMessage) new ObjectInputStream(clientSocket.getInputStream()).readObject();
            if (listener != null) {
                listener.onMessageReceived(adaptMessage);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
