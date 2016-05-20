package bftsmart.demo.adapt.rules.checkers;


import bftsmart.demo.adapt.util.Constants;
import bftsmart.demo.adapt.util.Registry;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ValueCheckers {
    private static final Map<String, ValueChecker> map = new HashMap<>();
    private static final Configurations configurations = new Configurations();

    private static String previousCheckerName;

    static {
        map.put("timedChecker", new TimedValueChecker());
        map.put("hybridChecker", new HybridValueChecker());
        map.put("basicChecker", new BasicValueChecker());
    }

    public static ValueChecker getCurrentChecker(Registry registry, int replicaId) {
        try {
            Configuration config = configurations.properties(new File(Constants.ADAPT_CONFIG_PATH));
            String newCheckerName = config.getString(Constants.CHECKER_TYPE);
            ValueChecker newChecker = map.get(newCheckerName);
            if (!newCheckerName.equals(previousCheckerName)) {
                System.out.println("Changed values checkers!");
                ValueChecker oldChecker = map.get(previousCheckerName);
                if (oldChecker != null) {
                    oldChecker.terminate();
                }
                newChecker.setup(registry, replicaId);
            }
            previousCheckerName = newCheckerName;
            return newChecker;
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
