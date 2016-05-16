package bftsmart.demo.adapt.util;

import bftsmart.demo.adapt.messages.MessageWithDigest;
import bftsmart.demo.adapt.messages.adapt.AdaptMessage;
import bftsmart.tom.ServiceProxy;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class BftUtils {
    public static int getQuorum(int f) {
        return 2 * f + 1;
    }

    public static int getF(int n) {
        return (n - 1) / 3;
    }

    public static byte[] sendMessage(int clientId, String homeFolder, Object message, boolean ordered) {
        ServiceProxy serviceProxy = null;
        byte[] reply = null;
        try {
            serviceProxy = new ServiceProxy(clientId, homeFolder);
            reply = ordered ?
                    serviceProxy.invokeOrdered(MessageSerializer.serialize(message)) :
                    serviceProxy.invokeUnordered(MessageSerializer.serialize(message));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serviceProxy != null) {
                serviceProxy.close();
            }
        }
        return reply;
    }

    public static <T extends AdaptMessage> void sendToReconfiguration(String[] hosts, int[] ports, MessageWithDigest<T> msg) {
        try {
            if (hosts.length != ports.length) {
                return;
            }
            for (int i = 0; i < hosts.length; i++) {
                Socket socket = new Socket(hosts[i], ports[i]);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
