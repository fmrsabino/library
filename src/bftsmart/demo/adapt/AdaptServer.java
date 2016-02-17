package bftsmart.demo.adapt;


import bftsmart.reconfiguration.VMServices;
import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultRecoverable;
import bftsmart.tom.util.Logger;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileReader;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;

public class AdaptServer extends DefaultRecoverable {

    private ServiceReplica replica;
    private List<ReplicaStatus> activeReplicas = new ArrayList<>();
    private List<ReplicaStatus> inactiveReplicas = new ArrayList<>();

    public AdaptServer(int id) {
        replica = new ServiceReplica(id, this, this);
        readStatusFile("config/hosts.status");
    }

    @Override
    public void installSnapshot(byte[] state) {}

    @Override
    public byte[] getSnapshot() {
        return new byte[0];
    }

    @Override
    public byte[][] appExecuteBatch(byte[][] commands, MessageContext[] msgCtxs) {
        return new byte[0][];
    }

    @Override
    public byte[] appExecuteUnordered(byte[] command, MessageContext msgCtx) {
        System.out.println("RECEIVED!");
        try {
            int threatLevel = new DataInputStream(new ByteArrayInputStream(command)).readInt();
            if (threatLevel == 1) { //add replica
                ReplicaStatus status = inactiveReplicas.remove(0);
                status.setActive(true);
                activeReplicas.add(status);
                String[] args = new String[] {"./runscripts/smartrun.sh bftsmart.reconfiguration.VMServices",
                    ""+status.getSmartId()+" "+status.getIp()+" "+status.getPort()+"\n"};
                //VMServices.main(new String[]{status.getSmartId(), status.getIp(), status.getPort()});
            } else { //remove replica
                ReplicaStatus status = activeReplicas.remove(0);
                status.setActive(false);
                inactiveReplicas.add(status);
                //VMServices.main(new String[]{status.getSmartId()});
            }
            System.out.println("Threat Level = " + threatLevel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Use: java AdaptServer <processId>");
            System.exit(-1);
        }
        new AdaptServer(Integer.parseInt(args[0]));
    }

    private void readStatusFile(String filepath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            System.out.println("BEGIN READ STATUS");
            for (String line; (line = br.readLine()) != null;) {
                System.out.println(line);
                String[] splits = line.split(" ");
                if (splits[3].equals("1")) {
                    activeReplicas.add(new ReplicaStatus(splits[0], splits[1], splits[2], true));
                } else {
                    inactiveReplicas.add(new ReplicaStatus(splits[0], splits[1], splits[2], false));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ReplicaStatus {
        private String smartId;
        private String ip;
        private String port;
        private boolean active;

        public ReplicaStatus(String smartId, String ip, String port, boolean active) {
            this.smartId = smartId;
            this.ip = ip;
            this.port = port;
            this.active = active;
        }

        public String getSmartId() {
            return smartId;
        }

        public String getIp() {
            return ip;
        }

        public String getPort() {
            return port;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        @Override
        public String toString() {
            return "ReplicaStatus{" +
                    "smartId='" + smartId + '\'' +
                    ", ip='" + ip + '\'' +
                    ", port='" + port + '\'' +
                    ", active=" + active +
                    '}';
        }
    }
}
