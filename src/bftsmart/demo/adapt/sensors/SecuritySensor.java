package bftsmart.demo.adapt.sensors;

import bftsmart.demo.adapt.messages.sensor.SensorMessage;
import bftsmart.demo.adapt.messages.sensor.ThreatLevelMessage;
import bftsmart.demo.adapt.messages.sensor.ReplicaStatus;
import bftsmart.demo.adapt.util.BftUtils;
import bftsmart.demo.adapt.util.Constants;
import bftsmart.demo.adapt.util.FileUtil;
import bftsmart.demo.adapt.util.MessageSerializer;
import bftsmart.tom.ServiceProxy;

import java.util.ArrayList;
import java.util.List;

/**
 * Periodically reads the system model and the security model
 * and notifies the adapt system of the new values
 */
public class SecuritySensor {
    //interval in seconds
    private static final int PERIOD = 2;

    public static void main(String[] args) {
        //Runnable runnable = new Runnable() {
        //    @Override
          //  public void run() {
                List<ReplicaStatus> activeReplicas = new ArrayList<>();
                List<ReplicaStatus> inactiveReplicas = new ArrayList<>();
                readStatusFile(Constants.HOSTS_STATUS_PATH, activeReplicas, inactiveReplicas);
                int threatLevel = readThreatLevel(Constants.THREAT_LEVEL_PATH);
                //threatLevel = (int) (Math.random() * 100);
                ThreatLevelMessage msg = new ThreatLevelMessage(activeReplicas, inactiveReplicas, threatLevel);
                System.out.println("Sending Message");
                BftUtils.sendMessage(1001, Constants.ADAPT_HOME_FOLDER, msg, true);
                //System.out.println(msg);
          //  }
        //};

        //ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        //service.scheduleAtFixedRate(runnable, 0, PERIOD, TimeUnit.SECONDS);
        System.exit(0);
    }

    private static void readStatusFile(String filepath,
                                List<ReplicaStatus> activeReplicas,
                                List<ReplicaStatus> inactiveReplicas) {
        FileUtil.readFileLines(filepath).stream().forEach(l -> {
            String[] splits = l.split(" ");
            if (splits.length >= 4) {
                if (splits[3].equals("1")) {
                    activeReplicas.add(new ReplicaStatus(splits[0], splits[1], splits[2], true));
                } else {
                    inactiveReplicas.add(new ReplicaStatus(splits[0], splits[1], splits[2], false));
                }

            }
        });
    }

    private static int readThreatLevel(String filepath) {
        List<String> lines = FileUtil.readFileLines(filepath);
        return lines.isEmpty() ? -1 : Integer.parseInt(lines.get(0));
    }
}
