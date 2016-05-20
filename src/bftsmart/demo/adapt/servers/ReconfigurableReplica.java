package bftsmart.demo.adapt.servers;

import bftsmart.demo.adapt.messages.MessageWithDigest;
import bftsmart.demo.adapt.messages.adapt.AdaptMessage;
import bftsmart.demo.adapt.util.Constants;
import bftsmart.demo.adapt.util.FileUtils;
import bftsmart.tom.server.defaultservices.DefaultRecoverable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class ReconfigurableReplica extends DefaultRecoverable {
    public ReconfigurableReplica(String host, int port, int nAdaptReplicas) throws IOException {
        ReconfigurationService reconfigurationService = new ReconfigurationService(host, port, nAdaptReplicas);
        reconfigurationService.start();
    }

    public static class ReconfigurationService extends Thread {
        private final String host;
        private final int port;
        private final MessagesContainer container;

        private ReconfigurationService(String host, int port, int nAdaptReplicas) throws IOException {
            this.host = host;
            this.port = port;
            this.container = new MessagesContainer(FileUtils.readPublicKeys(Constants.ADAPT_KEYS_PATH, "RSA"), 5, nAdaptReplicas);
        }

        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println(String.format("[ReconfigurationService] Started on %s:%d", host, port));
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("[ReconfigurationService] Accepting client connection");
                    new Thread(() -> {
                        try {
                            MessageWithDigest adaptMessage = (MessageWithDigest) new ObjectInputStream(clientSocket.getInputStream()).readObject();
                            AdaptMessage result = container.store(adaptMessage);
                            if (result != null) {
                                result.execute();
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public static class MessagesContainer {
            private int maximumSize;
            private int quorumSize;
            private Map<Integer, SequenceN> map = new HashMap<>();
            private Map<Integer, PublicKey> publicKeys;

            public MessagesContainer(Map<Integer, PublicKey> publicKeys, int maximumSize, int quorumSize) {
                this.publicKeys = publicKeys;
                this.maximumSize = maximumSize;
                this.quorumSize = quorumSize;
            }

            public <T extends AdaptMessage> T store(MessageWithDigest<T> messageWithDigest) {
                T msg = messageWithDigest.getContent();
                PublicKey pk = publicKeys.get(msg.getSender());
                if (!messageWithDigest.verify(pk)) {
                    System.out.println("[ReconfigurationService] Invalid message signature");
                    return null;
                }
                int seqN = msg.getSeqN();
                SequenceN sequenceN = map.get(seqN);
                if (sequenceN == null) {
                    //Check map size
                    if (map.keySet().size() >= maximumSize) {
                        System.out.println("[ReconfigurationService] Reached container limits. Discarding message");
                        return null;
                    }
                    sequenceN = new SequenceN();
                    map.put(seqN, sequenceN);
                }
                T result = sequenceN.store(msg);
                if (result != null) {
                    map.remove(result.getSeqN());
                }
                return result;
            }

            private class SequenceN {
                private Set<AdaptMessage> messages = new HashSet<>();

                public <T extends AdaptMessage> T store(T msg) {
                    messages.add(msg);
                    return messages.size() == quorumSize ? msg : null;
                }
            }
        }
    }
}
