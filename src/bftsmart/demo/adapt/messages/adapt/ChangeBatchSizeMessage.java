package bftsmart.demo.adapt.messages.adapt;

import bftsmart.reconfiguration.ViewManager;

public class ChangeBatchSizeMessage extends AdaptMessage {
    private final int newBatchSize;

    public ChangeBatchSizeMessage(int seqN, int sender, int newBatchSize) {
        super(seqN, sender);
        this.newBatchSize = newBatchSize;
    }

    @Override
    public boolean equals(Object o) {
        //A message with the same sequence number and sender is considered the same
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + newBatchSize;
        return result;
    }

    @Override
    public void execute() {
        ViewManager vm = new ViewManager();
        vm.changeMaxBatchSize(newBatchSize);
        vm.executeUpdates();
        vm.close();
    }
}
