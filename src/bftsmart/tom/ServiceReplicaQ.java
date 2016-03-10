package bftsmart.tom;

import bftsmart.demo.adapt.messages.MessageSerializer;
import bftsmart.demo.adapt.messages.QuorumMessage;
import bftsmart.tom.core.messages.TOMMessage;
import bftsmart.tom.server.Executable;
import bftsmart.tom.server.Recoverable;
import bftsmart.tom.server.RequestVerifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service Replica that waits for quorums of messages before delivering to the application
 */
public class ServiceReplicaQ extends ServiceReplica {
    private static final int QUORUM_SIZE = 3;
    private Map<Integer, List<QuorumMessage>> msgsReceived = new HashMap<>();

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
            QuorumMessage statusMessage = MessageSerializer.deserialize(message.getContent());
            if (updateMap(statusMessage) != null) {
                msgsReceived.clear();
                System.out.println("Quorum reached!");
                super.receiveReadonlyMessage(message, msgCtx);
            }
        } catch (Exception e) {
            e.printStackTrace();
            super.receiveReadonlyMessage(message, msgCtx);
        }
    }

    //Returns StatusMessage if the quorum for that message has been reached (null otherwise)
    private QuorumMessage updateMap(QuorumMessage statusMessage) {
        int msgHash = statusMessage.hashCode();
        List<QuorumMessage> msgs;
        if (!msgsReceived.containsKey(msgHash)) {
            msgs = new ArrayList<>();
            msgs.add(statusMessage);
            msgsReceived.put(msgHash, msgs);
        } else {
            msgs = msgsReceived.get(statusMessage.hashCode());
            msgs.add(statusMessage);
        }

        if (msgs.size() >= QUORUM_SIZE) {
            return statusMessage;
        } else {
            return null;
        }
    }
}
