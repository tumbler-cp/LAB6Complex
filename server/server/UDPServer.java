package server;

import common.Request;
import common.Signal;
import common.UDPInterface;
import exceptions.FileLoadException;
import exceptions.TimeOutException;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

/**
 * @author Abdujalol Khodjaev
 * UDP Interface of server
 * @see UDPInterface
 * @see ServerTerminal
 */
public class UDPServer implements UDPInterface {
    private DatagramChannel channel;
    private SocketAddress clientAddress;
    public boolean inProcess = false;

    /**
     * Default constructor
     * @param host Host
     * @param port Port
     * @throws IOException May be thrown by channel
     */
    public UDPServer(String host, int port) throws IOException {
        SocketAddress address;
        while (true) {
            try {
                channel = DatagramChannel.open();
                address = new InetSocketAddress(host, port);
                channel.bind(address);
                channel.configureBlocking(false);
            } catch (PortUnreachableException e) {
                System.out.println("Указанный порт недоступен: " + port);
                Server.logger.error("Порт недоступен: " + port);
                port = 6000;
                port++;
                continue;
            }catch (SocketException s){
                System.out.println("Указанный порт недоступен: " + port);
                Server.logger.error("Порт недоступен: " + port);
                port = 6000;
                port++;
                continue;
            }
            System.out.println("Начало работы на " + address);
            break;
        }

    }

    /**
     * Data sender
     * @param data Data to send
     */
    public void send(byte[] data) {
        ByteBuffer bytes;
        bytes = ByteBuffer.wrap(data);
        System.out.println(new String(bytes.array()));
        try {
            channel.send(bytes, clientAddress);
            System.out.println("Отправлен ответ клиенту по адресу: " + clientAddress);
        } catch (IOException e) {
            System.out.println("Ошибка отправки через сетевой канал!");
        }
    }

    /**
     * Data receiver
     * @return Received data
     * @throws IOException May be thrown while receiving
     * @throws TimeOutException When there's no request from anyone
     */
    public byte[] receive() throws IOException, TimeOutException {
        var bytes = ByteBuffer.allocate(1024);
        SocketAddress remoteAdd = null;
        long startTime = System.currentTimeMillis();
        do {
            if (remoteAdd == null) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime >= 100) {
                    throw new TimeOutException();
                }
            }
            remoteAdd = channel.receive(bytes);
        } while (remoteAdd == null);
        if (!inProcess) clientAddress = remoteAdd;
        else {
            while (!clientAddress.equals(remoteAdd)) {
                channel.send(ByteBuffer.wrap(new Request(Signal.WAIT, "").bytes()), remoteAdd);
                bytes = ByteBuffer.allocate(1024);
                do {
                    remoteAdd = channel.receive(bytes);
                } while (remoteAdd == null);
                System.out.println(remoteAdd);
                System.out.println(clientAddress);
            }
        }
        bytes.flip();
        byte[] ret = new byte[bytes.remaining()];
        bytes.get(ret);
        System.out.println("Получен запрос от клиента на порту: " + clientAddress);
        return ret;
    }

    /**
     * File receiver
     * @param path Path to save file
     * @throws IOException May be thrown while receiving
     * @throws FileLoadException May be thrown if client had problems with file and sent CLOSING Signal
     * @see FileLoadException
     */
    public void receiveFile(String path) throws IOException, FileLoadException {
        FileOutputStream fout = new FileOutputStream(path);
        byte[] buffer;
        System.out.println("Получение файла " + path);
        while (true) {
            try {
                buffer = receive();
            } catch (TimeOutException time) {
                continue;
            }
            try {
                Signal s = SerializationUtils.deserialize(buffer);
                if (s == Signal.CLOSING) {
                    Server.logger.error("Ошибка получения файла");
                    throw new FileLoadException();
                }
            } catch (ClassCastException | SerializationException ignored) {}
            System.out.println(Arrays.toString(buffer));
            fout.write(buffer);
            if (buffer.length < 1024) break;
        }
        System.out.println("Файл получен " + path);
    }

    /**
     * Disconnect channel
     * @throws IOException May be thrown by channel
     */
    public void disconnect() throws IOException {
        channel.disconnect();
    }

    /**
     * Close channel
     * @throws IOException May be thrown by channel
     */
    public void close() throws IOException {
        channel.close();
    }

    /**
     * Request sender. Creates request object and sends it.
     * @param signal Request signal
     * @param obj Request object
     */
    @Override
    public void request(Signal signal, Object obj) {
        this.send(new Request(signal, obj).bytes());
        Server.logger.info("Отправлен запроса с сигналом " + signal);
    }

    /**
     * Need to save history
     * @return Client address
     */
    public SocketAddress getClientAddress() {
        return clientAddress;
    }
}