package client;

import common.Request;
import common.Signal;
import common.UDPInterface;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;

public class UDPClient implements UDPInterface {
    private final DatagramSocket socket;
    private final SocketAddress serverAddress;
    public UDPClient(String serverHost, int port) throws IOException {
        serverAddress = new InetSocketAddress(serverHost, port);
        socket = new DatagramSocket();
        socket.setReuseAddress(true);
    }
    public void send (byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress);
        try {
            socket.send(packet);
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода");
        }
    }
    public void sendFile (FileInputStream fin) throws IOException {
        byte[] buff = new byte[1024];
        int byt;
        while ((byt = fin.read(buff)) != -1) {
            DatagramPacket packet = new DatagramPacket(buff, byt, serverAddress);
            socket.send(packet);
            buff = new byte[1024];
        }
    }
    public byte[] receive() throws IOException {
        byte[] buffer = new byte[4096];
        ByteArrayOutputStream byteBuilder = new ByteArrayOutputStream();
        DatagramPacket packet = new DatagramPacket(buffer, 4096);
        do {
            socket.receive(packet);
            byteBuilder.write(packet.getData());
        } while (packet.getLength() > buffer.length);
        byte[] ret = byteBuilder.toByteArray();
        return byteTrim(ret);
    }
    public void close() {
        socket.close();
    }


    private byte[] byteTrim(byte[] data) {
        int i = data.length - 1;
        while (i >= 0 && data[i] == 0)
        {
            --i;
        }
        return Arrays.copyOf(data, i + 1);
    }

    @Override
    public void request(Signal signal, Object obj) {
        send(new Request(signal, obj).bytes());
    }
}