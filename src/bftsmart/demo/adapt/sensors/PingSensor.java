package bftsmart.demo.adapt.sensors;

import bftsmart.demo.adapt.messages.sensor.PingMessage;
import bftsmart.demo.adapt.util.BftUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PingSensor {
    private static String host = "127.0.0.1";
    private static int port = 7003;

    public static void main(String[] args) {
        new Receiver().start();
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new Sender(), 0, 7, TimeUnit.SECONDS);
    }

    public static class Receiver extends Thread {
        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Started Sensor Receiver");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("[Sensor] Received message from " + clientSocket.getInputStream().read());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Sender implements Runnable {
        @Override
        public void run() {
            BftUtils.sendMessage(1001, "", new PingMessage(host, port), false);
        }
    }
}
