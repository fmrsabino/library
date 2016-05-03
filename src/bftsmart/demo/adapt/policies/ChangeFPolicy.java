package bftsmart.demo.adapt.policies;

import bftsmart.demo.adapt.TTP.TTPClient;
import bftsmart.demo.adapt.messages.adapt.ChangeFMessage;
import bftsmart.demo.adapt.messages.sensor.ReplicaStatus;
import bftsmart.demo.adapt.messages.sensor.ThreatLevelMessage;
import bftsmart.demo.adapt.util.BftUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChangeFPolicy implements AdaptPolicy<ThreatLevelMessage> {
    @Override
    public void execute(int executorId, ThreatLevelMessage message) {
        int threatLevel = message.getThreatLevel();
        int f = BftUtils.getF(message.getActiveReplicas().size());
        Collections.sort(message.getActiveReplicas(), (r1, r2) -> (r1.getSmartId() - r2.getSmartId()));
        if (threatLevel == 0 && f == 2) { //remove replicas
            List<ReplicaStatus> replicasToRemove = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                replicasToRemove.add(message.getActiveReplicas().get(message.getActiveReplicas().size() - 1 - i));
            }
            ChangeFMessage changeFMessage = new ChangeFMessage(ChangeFMessage.REMOVE_REPLICAS, replicasToRemove);
            TTPClient.sendMessage(changeFMessage);
            //BftUtils.sendMessage(executorId, "", changeFMessage);
        } else if (threatLevel == 1 && f == 1){ //add replicas
            List<ReplicaStatus> replicasToAdd = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                replicasToAdd.add(message.getInactiveReplicas().get(i));
            }
            ChangeFMessage changeFMessage = new ChangeFMessage(ChangeFMessage.ADD_REPLICAS, replicasToAdd);
            TTPClient.sendMessage(changeFMessage);
            //BftUtils.sendMessage(executorId, "", changeFMessage);
        }
    }
}