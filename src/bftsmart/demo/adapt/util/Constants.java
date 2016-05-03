package bftsmart.demo.adapt.util;

import java.io.File;

public class Constants {
    //BFT-SMaRt Home Locations
    public static final String ADAPT_HOME_FOLDER = "adapt-config";
    public static final String SENSORS_HOME = "sensor";

    //Files paths
    public static final String ADAPT_CONFIG_PATH = ADAPT_HOME_FOLDER + File.separator + "adapt.properties";
    public static final String THREAT_LEVEL_PATH = SENSORS_HOME + File.separator + "threat.status";
    public static final String HOSTS_STATUS_PATH = SENSORS_HOME + File.separator + "hosts.properties";

    //Config keys
    public static final String N_SENSORS_KEY = "n.sensors";
    public static final String ADAPT_POLICY_KEY = "adapt.policy";
    public static final String SENSOR_TYPE = "adapt.sensor.type";

    public static final String HOSTS_KEY = "hosts";
    public static final String TIMEOUT_KEY = "timeout";
    public static final String SENSOR_PORT_KEY = "sensorPort";
    public static final String SENSOR_PING_PERIOD_KEY = "pingPeriod";
}
