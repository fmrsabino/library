package bftsmart.demo.adapt.replicas;

import bftsmart.demo.adapt.messages.MessageWithDigest;
import bftsmart.demo.adapt.messages.adapt.AdaptMessage;
import bftsmart.demo.adapt.util.Constants;
import bftsmart.demo.adapt.util.FileUtils;
import bftsmart.demo.adapt.util.MessageMatcher;
import bftsmart.tom.server.defaultservices.DefaultRecoverable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class ReconfigurableReplica extends DefaultRecoverable {
    public ReconfigurableReplica(String host, int port, int quorum) throws IOException {
        ReconfigurationService reconfigurationService = new ReconfigurationService(host, port, quorum);
        reconfigurationService.start();
    }

    public class ReconfigurationService extends Thread {
        private final String host;
        private final int port;
        private final MessageMatcher<AdaptMessage> messageMatcher;

        private ReconfigurationService(String host, int port, int quorum) throws IOException {
            this.host = host;
            this.port = port;
            this.messageMatcher = new MessageMatcher<>(quorum, 5, FileUtils.readPublicKeys(Constants.ADAPT_KEYS_PATH, "RSA"));
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
                            MessageWithDigest<AdaptMessage> adaptMessage = (MessageWithDigest<AdaptMessage>) new ObjectInputStream(clientSocket.getInputStream()).readObject();
                            AdaptMessage result = messageMatcher.store(adaptMessage);
                            if (result != null) {
                                new Thread(result::execute).start();
                                messageMatcher.setAsExecuted(result.getSeqN());
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
    }
}
