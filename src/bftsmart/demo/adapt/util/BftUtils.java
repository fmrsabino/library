package bftsmart.demo.adapt.util;

import bftsmart.tom.ServiceProxy;

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
}
