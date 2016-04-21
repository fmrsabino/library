package bftsmart.demo.adapt.policies;

import bftsmart.demo.adapt.util.Constants;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Policies {
    private static final Map<String, AdaptPolicy> map = new HashMap<>();
    private static final Configurations configurations = new Configurations();


    static {
        map.put("changeF", new ChangeFPolicy());
        map.put("changeTimeout", new ChangeTimeoutPolicy());
    }

    public static AdaptPolicy getCurrentPolicy() {
        try {
            Configuration config = configurations.properties(new File(Constants.ADAPT_CONFIG_PATH));
            return map.get(config.getString(Constants.ADAPT_POLICY_KEY));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
