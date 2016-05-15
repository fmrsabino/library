package bftsmart.demo.adapt.sensors;

import bftsmart.demo.adapt.messages.sensor.SensorMessage;
import bftsmart.demo.adapt.messages.sensor.WorkloadSizeMessage;
import bftsmart.demo.adapt.util.BftUtils;
import bftsmart.demo.adapt.util.Constants;

public class ChangeBatchTest {
    public static void main(String[] args) {
        WorkloadSizeMessage msg1 = new WorkloadSizeMessage(0, SensorMessage.Type.BATCH_SIZE, 0, WorkloadSizeMessage.Size.LOW);
        WorkloadSizeMessage msg2 = new WorkloadSizeMessage(1, SensorMessage.Type.BATCH_SIZE, 0, WorkloadSizeMessage.Size.LOW);
        WorkloadSizeMessage msg3 = new WorkloadSizeMessage(2, SensorMessage.Type.BATCH_SIZE, 0, WorkloadSizeMessage.Size.LOW);
        BftUtils.sendMessage(1001, Constants.ADAPT_HOME_FOLDER, msg1, true);
        BftUtils.sendMessage(1001, Constants.ADAPT_HOME_FOLDER, msg2, true);
        BftUtils.sendMessage(1001, Constants.ADAPT_HOME_FOLDER, msg3, true);
        System.exit(0);
    }
}
