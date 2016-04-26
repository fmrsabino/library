package bftsmart.demo.adapt.util;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MessageMatcher<T> {
    private final ConcurrentHashMap<Integer, List<T>> map = new ConcurrentHashMap<>();
    private final int quorumSize;

    public MessageMatcher(int quorumSize) {
        this.quorumSize = quorumSize;
    }


    /**
     * Inserts the message
     * If the specified message has the needed quorum
     * that message is returned and the map is cleared
     */
    public T insertMessage(T t) {
        int hash = t.hashCode();
        List<T> list;
        if (map.get(hash) == null) {
            list = new ArrayList<>();
            list.add(t);
            map.put(hash, list);
        } else {
            list = map.get(hash);
            list.add(t);
        }
        if (list.size() == quorumSize) {
            map.clear();
            return t;
        }
        return null;
    }
}
