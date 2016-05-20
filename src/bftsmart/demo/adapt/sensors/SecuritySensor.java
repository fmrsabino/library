package bftsmart.demo.adapt.sensors;

import bftsmart.demo.adapt.messages.sensor.PingMessage;
import bftsmart.demo.adapt.messages.sensor.ReplicaStatus;
import bftsmart.demo.adapt.util.BftUtils;
import bftsmart.demo.adapt.util.Constants;
import bftsmart.demo.adapt.util.FileUtils;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Periodically reads the system model and the security model
 * and notifies the adapt system of the new values
 */
public class SecuritySensor {
    private static int port;
    private static int period;
    private static long timeout; //ms
    private static List<ReplicaStatus> hosts;

    public static void main(String[] args) {
        init();
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new PingReceiver(), 0, period, TimeUnit.SECONDS);
    }

    private static void init() {
        try {
            Parameters params = new Parameters();
            FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                    new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                            .configure(params.properties()
                                    .setFileName(Constants.HOSTS_STATUS_PATH)
                                    .setListDelimiterHandler(new DefaultListDelimiterHandler(',')));
            Configuration config = builder.getConfiguration();
            port = config.getInt(Constants.SENSOR_PORT_KEY);
            hosts = parseHosts(config.getStringArray(Constants.HOSTS_KEY));
            period = config.getInt(Constants.SENSOR_PING_PERIOD_KEY); //s to ms
            timeout = config.getLong(Constants.TIMEOUT_KEY) * 1000; //s to ms
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class PingReceiver extends Thread {
        @Override
        public void run() {
            try {
                List<ReplicaStatus> activeReplicas = new ArrayList<>();
                List<ReplicaStatus> inactiveReplicas = new ArrayList<>(hosts);
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Started Sensor Receiver");
                new PingSender().start();
                new TimeoutThread(serverSocket).start();
                while (true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        int smartId = clientSocket.getInputStream().read();
                        System.out.println("[Sensor] Received message from " + smartId);
                        for (ReplicaStatus r : inactiveReplicas) {
                            if (r.getSmartId() == smartId) {
                                activeReplicas.add(r);
                                inactiveReplicas.remove(r);
                                break;
                            }
                        }
                        clientSocket.close();
                    } catch (SocketException e) {
                        break;
                    }
                }
                System.out.println("Reached timeout.\nActiveReplicas="+activeReplicas+"\nInactiveReplicas="+inactiveReplicas);
                //send message to adapt
                //ThreatLevelMessage msg = new ThreatLevelMessage(0, 0, 0, activeReplicas, inactiveReplicas, readThreatLevel(Constants.THREAT_LEVEL_PATH));
                //BftUtils.sendMessage(1001, Constants.ADAPT_HOME, msg, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class PingSender extends Thread {
        @Override
        public void run() {
            try {
                System.out.println("Sending ping to replicas");
                BftUtils.sendMessage(1001, "", new PingMessage(InetAddress.getLocalHost(), port), false);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }

    private static class TimeoutThread extends Thread {
        private final ServerSocket socketToClose;

        public TimeoutThread(ServerSocket socketToClose) {
            this.socketToClose = socketToClose;
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            while (true) {
                if ((System.currentTimeMillis() - startTime) > timeout) {
                    if (socketToClose != null && !socketToClose.isClosed()) {
                        try {
                            socketToClose.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        }
    }

    private static List<ReplicaStatus> parseHosts(String[] hosts) {
        List<ReplicaStatus> replicas = new ArrayList<>();
        Arrays.stream(hosts).forEach(h -> {
            String[] splits = h.split(" ");
            if (splits.length == 3) {
                replicas.add(
                        Integer.parseInt(splits[0]),
                        new ReplicaStatus(Integer.parseInt(splits[0]), splits[1], splits[2]));
            }
        });
        return replicas;
    }

    private static int readThreatLevel(String filepath) {
        List<String> lines = FileUtils.readFileLines(filepath);
        return lines.isEmpty() ? -1 : Integer.parseInt(lines.get(0));
    }
}
