package bftsmart.demo.adapt.extractors;

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
        map.put("threatSensor", new ThreatLevelExtractor());
        map.put("bandwidthSensor", new BandwidthExtractor());
        map.put("signaturesSensor", new UseSignaturesExtractor());
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
