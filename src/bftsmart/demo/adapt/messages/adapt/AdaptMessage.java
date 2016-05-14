package bftsmart.demo.adapt.messages.adapt;

import java.io.Serializable;

public interface AdaptMessage extends Serializable {
    //executes the reconfiguration
    void execute();
}
