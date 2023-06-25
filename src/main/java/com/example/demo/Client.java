package com.example.demo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;


public class Client {
    public String Connect(byte[] buffer, String type) throws IOException {
        int port = 1236;
        InetAddress address = InetAddress.getLocalHost();
        DatagramSocket socket = new DatagramSocket();
        DatagramPacket packet_send = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet_send);
        byte[] msg = new byte[1024];
        if (type.equals("chat") || type.equals("get_megs")) return "";
        DatagramPacket packet_receive = new DatagramPacket(msg, msg.length);
        socket.receive(packet_receive);
        String rep = new String(packet_receive.getData());
        return rep.trim();
    }
}
