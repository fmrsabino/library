package bftsmart.demo.adapt.util;


import bftsmart.demo.adapt.messages.Message;
import bftsmart.demo.adapt.messages.MessageWithDigest;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.*;

public class MessageMatcher<T extends Message> implements Serializable {
    private SortedMap<Integer, Execution<T>> executions;
    private final int limit;
    private final int quorumSize;
    private final Map<Integer, PublicKey> publicKeys;

    public MessageMatcher(int quorumSize, int limit, Map<Integer, PublicKey> publicKeys) {
        this.quorumSize = quorumSize;
        this.limit = limit;
        this.executions = new TreeMap<>();
        this.publicKeys = publicKeys;
    }

    public synchronized T store(MessageWithDigest<T> messageWithDigest) {
        T content = messageWithDigest.getContent();
        if (messageWithDigest.verify(publicKeys.get(content.getSender()))) {
            Execution<T> exec = executions.get(content.getSeqN());
            if (exec == null) {
                if (executions.size() == limit) {
                    Execution<T> oldestExec = executions.get(executions.firstKey());
                    if (oldestExec != null) {
                        if (oldestExec.executed) {
                            executions.remove(executions.firstKey());
                        } else {
                            System.out.println("[MessageMatcher] Discarding message due to size limits.");
                            return null;
                        }
                    }
                }
                exec = new Execution<>();
                executions.put(content.getSeqN(), exec);
            }
            return exec.store(content);
        } else {
            System.out.println("[MessageMatcher] Invalid message signature.");
        }
        return null;
    }

    public void setAsExecuted(int seqN) {
        Execution<T> execution = executions.get(seqN);
        if (execution != null) {
            execution.executed = true;
        }
    }

    private class Execution<M extends Message> {
        private Map<Integer, Set<M>> messages = new HashMap<>();
        private boolean executed = false;

        public M store(M msg) {
            int hash = msg.hashCode();
            Set<M> msgs = messages.get(hash);
            if (msgs == null) {
                msgs = new HashSet<>();
                messages.put(hash, msgs);
            }
            if (msgs.size() >= quorumSize) {
                System.out.println("[MessageMatcher] Already reached quorum for msg. Discarding...");
                return null;
            }
            msgs.add(msg);
            System.out.println(String.format("Execution %d. Hash=%d. Size=%d", msg.getSeqN(), hash, msgs.size()));
            System.out.println("Reached quorum? - " + (msgs.size() >= quorumSize));
            return msgs.size() >= quorumSize ? msg : null;
        }
    }
}
