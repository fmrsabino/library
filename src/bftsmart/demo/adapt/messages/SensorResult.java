package bftsmart.demo.adapt.messages;

import bftsmart.communication.SystemMessage;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.List;

public class SensorResult extends SystemMessage implements AdaptMessage, Serializable {
    private List<Integer> sensorResults;

    public SensorResult() {}

    public SensorResult(int sender, List<Integer> sensorResults) {
        super(sender);
        this.sensorResults = sensorResults;
    }

    public List<Integer> getSensorResults() {
        return sensorResults;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(sensorResults);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        sensorResults = (List<Integer>) in.readObject();
    }
}
