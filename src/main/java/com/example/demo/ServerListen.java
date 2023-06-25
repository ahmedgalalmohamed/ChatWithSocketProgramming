package com.example.demo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerListen implements Runnable {
    private final int port;
    private String message = "";

    public ServerListen(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(port);
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                message = new String(packet.getData()).trim();
                Home home = new Home();
                home.getMessage(message);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
