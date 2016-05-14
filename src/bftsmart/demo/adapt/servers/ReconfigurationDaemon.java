package bftsmart.demo.adapt.servers;

import bftsmart.demo.adapt.messages.adapt.AdaptMessage;

import java.util.ArrayList;
import java.util.List;

public class ReconfigurationDaemon {
    private List<AdaptMessage> msgs = new ArrayList<>();
    private final static int QUORUM = 3;
    private final int smartId;

    public ReconfigurationDaemon(int smartId) {
        this.smartId = smartId;
    }

    public void storeMessage(AdaptMessage msg) {
        msgs.add(msg);
        System.out.println("Adding message to quorum");
        if (msgs.size() == QUORUM) {
            msgs.clear();
            System.out.println("Reached quorum!! Executing message");
                new Thread(() -> {
                    try {
                        Thread.sleep(smartId * 15000);
                        msg.execute();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
        }
    }
}
