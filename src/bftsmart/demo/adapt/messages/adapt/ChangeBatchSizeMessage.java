package bftsmart.demo.adapt.messages.adapt;

import bftsmart.reconfiguration.ViewManager;

public class ChangeBatchSizeMessage implements AdaptMessage {
    private final int newBatchSize;

    public ChangeBatchSizeMessage(int newBatchSize) {
        this.newBatchSize = newBatchSize;
    }

    @Override
    public void execute() {
        ViewManager vm = new ViewManager();
        vm.changeMaxBatchSize(newBatchSize);
        vm.executeUpdates();
        vm.close();
    }
}
