package bftsmart.demo.adapt.TTP;

import bftsmart.demo.adapt.messages.adapt.ChangeFMessage;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class TTPClient {
    public static void sendMessage(ChangeFMessage changeFMessage) {
        try {
            Socket socket = new Socket(TTPServer.HOST, TTPServer.PORT);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(changeFMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
