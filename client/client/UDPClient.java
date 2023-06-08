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

/**
 * Client networks realisation
 * @author Abdujalol Khodjaev
 * @see ClientHandler
 * @see ClientTerminal
 * @see UDPInterface
 */
public class UDPClient implements UDPInterface {
    private final DatagramSocket socket;
    private final SocketAddress serverAddress;

    /**
     * Default constructor
     * @param serverHost Host to connect to
     * @param port Port to connect to
     * @throws IOException May be thrown by socket
     */
    public UDPClient(String serverHost, int port) throws IOException {
        serverAddress = new InetSocketAddress(serverHost, port);
        socket = new DatagramSocket();
        socket.setReuseAddress(true);
    }

    /**
     * Implemented method.
     * Method to send data. Sends data only to server.
     * @param data Data to send
     */
    public void send (byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress);
        try {
            socket.send(packet);
        } catch (IOException e) {
            Client.logger.error("Ошибка ввода/вывода");
        }
    }

    /**
     * Method for sending files
     * @param fin File Input Stream to read data one by one
     * @throws IOException May be thrown by fin
     */
    public void sendFile (FileInputStream fin) throws IOException {
        byte[] buff = new byte[1024];
        int byt;
        while ((byt = fin.read(buff)) != -1) {
            DatagramPacket packet = new DatagramPacket(buff, byt, serverAddress);
            socket.send(packet);
            buff = new byte[1024];
        }
    }

    /**
     * Method to receive data
     * @return byte array
     * @throws IOException May be thrown while receiving
     */
    public byte[] receive() throws IOException {
        byte[] buffer = new byte[4096];
        ByteArrayOutputStream byteBuilder = new ByteArrayOutputStream();
        DatagramPacket packet = new DatagramPacket(buffer, 4096);
        do {
            Arrays.fill(buffer, (byte) 0);
            socket.receive(packet);
            byteBuilder.write(buffer);
        } while (packet.getLength() > buffer.length);
        byte[] ret = byteBuilder.toByteArray();
        return byteTrim(ret);
    }
    /**
     * Method to close socket
     */
    public void close() {
        socket.close();
    }

    /**
     * Byte array trimmer. Removes NULLS from byte array.
     * @param data byte array to trim
     * @return trimmed byte array
     */
    private byte[] byteTrim(byte[] data) {
        int i = data.length - 1;
        while (i >= 0 && data[i] == 0)
        {
            --i;
        }
        return Arrays.copyOf(data, i + 1);
    }

    /**
     * Sends Request object
     * @param signal Type of request (Not Command)
     * @param obj Object of request
     * @see Request
     * @see Signal
     */
    @Override
    public void request(Signal signal, Object obj) {
        send(new Request(signal, obj).bytes());
    }
    /*
    private byte[] sum(byte[] b1, byte[] b2) {
        int size = b1.length + b2.length;
        byte[] ret = new byte[size];
        System.arraycopy(b1, 0, ret, 0, b1.length);
        System.arraycopy(b2, 0, ret,b1.length, b2.length);
        return ret;
    }
    */
}