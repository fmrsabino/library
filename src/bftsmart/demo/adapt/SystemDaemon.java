package bftsmart.demo.adapt;

import bftsmart.demo.adapt.messages.AdaptMessage;
import bftsmart.demo.adapt.messages.StatusMessage;
import bftsmart.tom.ServiceProxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Periodically reads the system model and the security model
 * and notifies the adapt system of the new values
 */
public class SystemDaemon {

    private static final String PATH_FOLDER = "status";
    private static final String FILE_NAME = "hosts.status";

    //interval in seconds
    private static final int PERIOD = 2;

    public static void main(String[] args) {
        //Runnable runnable = new Runnable() {
        //    @Override
          //  public void run() {
                List<bftsmart.demo.adapt.messages.ReplicaStatus> activeReplicas = new ArrayList<>();
                List<bftsmart.demo.adapt.messages.ReplicaStatus> inactiveReplicas = new ArrayList<>();
                readStatusFile(
                        PATH_FOLDER + File.separator + FILE_NAME,
                        activeReplicas, inactiveReplicas);
                //int threatLevel = readThreatLevel(PATH_FOLDER + File.separator + "threat.status");
                int threatLevel = Integer.parseInt(args[0]);
                AdaptMessage msg = new StatusMessage();
                System.out.println("Sending Message");
                sendMessage(msg);
                //System.out.println(msg);
          //  }
        //};

        //ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        //service.scheduleAtFixedRate(runnable, 0, PERIOD, TimeUnit.SECONDS);
        System.exit(0);
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

    private static void sendMessage(AdaptMessage adaptMessage) {
        ServiceProxy serviceProxy = null;
        try {
            serviceProxy = new ServiceProxy(1001, AdaptServer.ADAPT_CONFIG_HOME);
            byte[] reply = serviceProxy.invokeOrdered(new byte[] {1});
            System.out.println("Reply: " +  reply);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serviceProxy != null) {
                serviceProxy.close();
            }
        }
    }
}
