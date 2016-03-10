package bftsmart.demo.adapt;

import bftsmart.demo.adapt.messages.MessageSerializer;
import bftsmart.tom.ServiceProxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
                List<bftsmart.demo.adapt.messages.ReplicaStatus> activeReplicas = new ArrayList<>();
                List<bftsmart.demo.adapt.messages.ReplicaStatus> inactiveReplicas = new ArrayList<>();
                readStatusFile(
                        PATH_FOLDER + File.separator + FILE_NAME,
                        activeReplicas, inactiveReplicas);
                int threatLevel = readThreatLevel(PATH_FOLDER + File.separator + "threat.status");
                bftsmart.demo.adapt.messages.StatusMessage msg = new bftsmart.demo.adapt.messages.StatusMessage(activeReplicas, inactiveReplicas, threatLevel);
                System.out.println("Sending Message");
                sendMessage(msg);
                System.out.println(msg);
            }
        };

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 0, PERIOD, TimeUnit.SECONDS);
    }

    private static void readStatusFile(String filepath,
                                List<bftsmart.demo.adapt.messages.ReplicaStatus> activeReplicas,
                                List<bftsmart.demo.adapt.messages.ReplicaStatus> inactiveReplicas) {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            for (String line; (line = br.readLine()) != null;) {
                String[] splits = line.split(" ");
                if (splits[3].equals("1")) {
                    activeReplicas.add(new bftsmart.demo.adapt.messages.ReplicaStatus(splits[0], splits[1], splits[2], true));
                } else {
                    inactiveReplicas.add(new bftsmart.demo.adapt.messages.ReplicaStatus(splits[0], splits[1], splits[2], false));
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

    private static void sendMessage(bftsmart.demo.adapt.messages.StatusMessage statusMessage) {
        try {
            ServiceProxy serviceProxy = new ServiceProxy(0, "adapt-config");
            serviceProxy.setInvokeTimeout(0);
            serviceProxy.invokeUnordered(MessageSerializer.serialize(statusMessage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
