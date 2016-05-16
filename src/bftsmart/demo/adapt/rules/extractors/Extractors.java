package bftsmart.demo.adapt.rules.extractors;

import bftsmart.demo.adapt.messages.sensor.BandwidthMessage;
import bftsmart.demo.adapt.messages.sensor.ThreatLevelMessage;
import bftsmart.demo.adapt.messages.sensor.WorkloadSizeMessage;
import bftsmart.demo.adapt.util.Constants;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Extractors {
    private static final Map<String, ValueExtractor> map = new HashMap<>();
    private static final Configurations configurations = new Configurations();


    static {
        map.put("threatSensor", new MiddleValueExtractor<ThreatLevelMessage>((sm1, sm2) -> sm1.getThreatLevel() - sm2.getThreatLevel()));
        map.put("bandwidthSensor", new MiddleValueExtractor<BandwidthMessage>((sm1, sm2) -> sm1.getBandwidth() - sm2.getBandwidth()));
        map.put("workloadSensor", new MiddleValueExtractor<WorkloadSizeMessage>((sm1, sm2) -> sm1.getWorkload().ordinal() - sm2.getWorkload().ordinal()));
    }

    public static ValueExtractor getCurrentExtractor() {
        try {
            Configuration config = configurations.properties(new File(Constants.ADAPT_CONFIG_PATH));
            return map.get(config.getString(Constants.SENSOR_TYPE));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
