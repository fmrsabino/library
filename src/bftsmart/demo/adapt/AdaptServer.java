package bftsmart.demo.adapt;


import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceProxy;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.core.messages.TOMMessageType;
import bftsmart.tom.server.defaultservices.DefaultRecoverable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdaptServer extends DefaultRecoverable {
    public final static String ADAPT_CONFIG_HOME = "adapt-config";
    private ServiceReplica replica;
    private int id;
    private List<ReplicaStatus> activeReplicas = new ArrayList<>();
    private List<ReplicaStatus> inactiveReplicas = new ArrayList<>();

    public AdaptServer(int id) {
        this.id = id;
        replica = new ServiceReplica(id, ADAPT_CONFIG_HOME, this, this, null);
        readStatusFile(ADAPT_CONFIG_HOME + "/hosts.status");
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
        System.out.println("[AdaptServer] RECEIVED!");
        ServiceProxy serviceProxy = new ServiceProxy(2000+id, "");
        String result = "RECONFIG ";
        try {
            int threatLevel = new DataInputStream(new ByteArrayInputStream(command)).readInt();
            if (threatLevel == 1) { //add replica
                ReplicaStatus status = inactiveReplicas.remove(0);
                status.setActive(true);
                activeReplicas.add(status);
                result += status.getSmartId() + " " + status.getIp() + " " + status.getPort();
            } else { //remove replica
                ReplicaStatus status = activeReplicas.remove(activeReplicas.size()-1);
                status.setActive(false);
                inactiveReplicas.add(0, status);
                result += status.getSmartId();
            }
            serviceProxy.invokeUnordered(result.getBytes("UTF-8"));
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
            return smartId + " " + ip + " " + port;
        }
    }
}
