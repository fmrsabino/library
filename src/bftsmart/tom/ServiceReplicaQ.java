package bftsmart.tom;

import bftsmart.tom.core.messages.TOMMessage;
import bftsmart.tom.core.messages.TOMMessageType;
import bftsmart.tom.server.Executable;
import bftsmart.tom.server.Recoverable;
import bftsmart.tom.server.RequestVerifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Replica that waits for quorums of messages before delivering to the application
 */
public class ServiceReplicaQ extends ServiceReplica {

    private List<TOMMessage> messages = new ArrayList<>();
    private static final int QUORUM_SIZE = 3;

    public ServiceReplicaQ
            (int id, Executable executor, Recoverable recoverer) {
        super(id, executor, recoverer);
    }

    public ServiceReplicaQ
            (int id, String configHome, Executable executor, Recoverable recoverer, RequestVerifier verifier) {
        super(id, configHome, false, executor, recoverer, verifier);
    }

    @Override
    public void receiveReadonlyMessage(TOMMessage message, MessageContext msgCtx) {
        try {
            String content = new String(message.getContent(), "UTF-8");
            String[] splits = content.split(" ");
            if (splits.length == 0) {
                super.receiveReadonlyMessage(message, msgCtx);
            } else if (!splits[0].equalsIgnoreCase("RECONFIG")) {
                System.out.println("Message is not RECONFIG");
                super.receiveReadonlyMessage(message, msgCtx);
            } else {
                System.out.println("Message is RECONFIG, adding to list.");
                messages.add(message);
                if (messages.size() >= QUORUM_SIZE) {
                    System.out.println("QUORUM OF RESPONSES REACHED!");
                    super.receiveReadonlyMessage(message, msgCtx);
                }
            }
        } catch (Exception e) {

        }
    }
}
