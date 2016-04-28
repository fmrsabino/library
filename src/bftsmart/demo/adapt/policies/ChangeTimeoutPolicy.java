package bftsmart.demo.adapt.policies;

import bftsmart.demo.adapt.messages.adapt.ChangeTimeoutMessage;
import bftsmart.demo.adapt.messages.sensor.BandwidthMessage;
import bftsmart.demo.adapt.util.BftUtils;

public class ChangeTimeoutPolicy implements AdaptPolicy<BandwidthMessage> {
    @Override
    public void execute(int executorId, BandwidthMessage message) {
        ChangeTimeoutMessage msg = new ChangeTimeoutMessage(message.getBandwidth()*1000);
        BftUtils.sendMessage(executorId, "", msg, true);
    }
}
