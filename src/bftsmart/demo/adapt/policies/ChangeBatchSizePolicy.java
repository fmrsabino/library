package bftsmart.demo.adapt.policies;

import bftsmart.demo.adapt.messages.adapt.ChangeBatchSizeMessage;
import bftsmart.demo.adapt.messages.sensor.WorkloadSizeMessage;
import bftsmart.demo.adapt.util.BftUtils;
import bftsmart.demo.adapt.util.Constants;

class ChangeBatchSizePolicy implements AdaptPolicy<WorkloadSizeMessage> {
    private final static String TAG = "ChangeBatchSizePolicy";

    @Override
    public void execute(int executorId, WorkloadSizeMessage message) {
        switch (message.getWorkload()) {
            case MEDIUM:
                System.out.println(String.format("[%s] Normal workload. Setting size %d.", TAG, 400));
                BftUtils.sendMessage(executorId, Constants.ADAPT_HOME_FOLDER, new ChangeBatchSizeMessage(400), false);
                break;
            case HIGH:
                System.out.println(String.format("[%s] Normal workload. Setting size %d.", TAG, 600));
                BftUtils.sendMessage(executorId, Constants.ADAPT_HOME_FOLDER, new ChangeBatchSizeMessage(600), false);
                break;
            case LOW:
                System.out.println(String.format("[%s] Normal workload. Setting size %d.", TAG, 200));
                BftUtils.sendMessage(executorId, Constants.ADAPT_HOME_FOLDER, new ChangeBatchSizeMessage(200), false);
                break;
        }
    }
}
