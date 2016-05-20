package bftsmart.demo.adapt.messages.adapt;

import bftsmart.demo.adapt.messages.Message;

public abstract class AdaptMessage extends Message {

    public AdaptMessage(int seqN, int sender) {
        super(seqN, sender);
    }

    /**
     * Executes the reconfiguration using the reconfiguration API of the managed system
     * This method is executed by the system being managed
     * */
    public abstract void execute();
}
