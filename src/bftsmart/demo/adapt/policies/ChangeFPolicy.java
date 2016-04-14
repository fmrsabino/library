package bftsmart.demo.adapt.policies;

import bftsmart.demo.adapt.messages.ReplicaStatus;
import bftsmart.demo.adapt.messages.ThreatLevelMessage;
import bftsmart.demo.adapt.util.BftUtils;
import bftsmart.reconfiguration.VMServices;

public class ChangeFPolicy implements AdaptPolicy<ThreatLevelMessage> {
    @Override
    public void execute(ThreatLevelMessage message) {
        int threatLevel = message.getThreatLevel();
        int f = BftUtils.getF(message.getActiveReplicas().size());
        if (threatLevel == 0 && f == 2) { //remove replicas
            for (int i = 0; i < 3; i++) {
                ReplicaStatus replica = message.getActiveReplicas().get(message.getActiveReplicas().size() - 1 - i);
                try {
                    VMServices.main(new String[]{replica.getSmartId()});
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (threatLevel == 1 && f == 1){ //add replicas
            for (int i = 0; i < 3; i++) {
                ReplicaStatus replica = message.getInactiveReplicas().get(i);
                try {
                    VMServices.main(new String[]{replica.getSmartId(), replica.getIp(), replica.getPort()});
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}