package bftsmart.demo.adapt;

import bftsmart.tom.ServiceProxy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Periodically reads the system model and the security model
 * and notifies the adapt system of the new values
 */
public class SystemDaemon {

    private static final String PATH_FOLDER = "status";
    private static final String FILE_NAME = "hosts.status";

    //interval in seconds
    private static final int PERIOD = 10;

    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<StatusMessage.ReplicaStatus> activeReplicas = new ArrayList<>();
                List<StatusMessage.ReplicaStatus> inactiveReplicas = new ArrayList<>();
                readStatusFile(
                        PATH_FOLDER + File.separator + FILE_NAME,
                        activeReplicas, inactiveReplicas);
                int threatLevel = readThreatLevel(PATH_FOLDER + File.separator + "threat.status");
                StatusMessage msg = new StatusMessage(activeReplicas, inactiveReplicas, threatLevel);
                System.out.println("Sending Message");
                sendMessage(msg);
                System.out.println(msg);
            }
        };

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 0, PERIOD, TimeUnit.SECONDS);
    }

    private static void readStatusFile(String filepath,
                                List<StatusMessage.ReplicaStatus> activeReplicas,
                                List<StatusMessage.ReplicaStatus> inactiveReplicas) {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            for (String line; (line = br.readLine()) != null;) {
                String[] splits = line.split(" ");
                if (splits[3].equals("1")) {
                    activeReplicas.add(new StatusMessage.ReplicaStatus(splits[0], splits[1], splits[2], true));
                } else {
                    inactiveReplicas.add(new StatusMessage.ReplicaStatus(splits[0], splits[1], splits[2], false));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int readThreatLevel(String filepath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            for (String line; (line = br.readLine()) != null;) {
                return Integer.parseInt(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static void sendMessage(StatusMessage statusMessage) {
        try {
            ServiceProxy serviceProxy = new ServiceProxy(0, "adapt-config");
            serviceProxy.setInvokeTimeout(0);
            serviceProxy.invokeUnordered(StatusMessage.serialize(statusMessage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
