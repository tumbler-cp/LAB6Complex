package server;

import common.Request;
import common.Signal;
import common.UDPInterface;
import org.apache.commons.lang3.SerializationUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

public class UDPServer implements UDPInterface {
    private final DatagramChannel channel;
    private SocketAddress clientAddress;
    public boolean inProcess = false;
    private boolean sending = false;
    private Signal signalBuff = null;
    public UDPServer(String host, int port) throws IOException {
        channel = DatagramChannel.open();
        SocketAddress address = new InetSocketAddress(host, port);
        channel.bind(address);
        channel.configureBlocking(false);
    }
    public void sendHandler(byte[] data) {
        try {
            Signal sign = SerializationUtils.deserialize(data);
            if (sign == Signal.ASK || sign == Signal.TEXT) {
                sending = true;
                signalBuff = sign;
                return;
            }
        } catch (ClassCastException s){
            System.out.println("Отправка данных...");
        }
        if (data.length > 1024 && sending) {
            byte[] buffer = new byte[1024];
            for (int i = 0; i < data.length; i++) {
                if (i % 1024 == 0) {
                    System.arraycopy(data, i, buffer, 0, i+1023);
                    if (data.length == i + 1023) {
                        signal(signalBuff);
                        send(buffer);
                        break;
                    }
                    signal(Signal.TEXT);
                    send(buffer);
                }
            }
        }
        else send(data);
    }
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
    public byte[] receive() throws IOException {
        var bytes = ByteBuffer.allocate(1024);
        SocketAddress remoteAdd;
        do {
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
    public void receiveFile(String path) throws IOException {
        FileOutputStream fout = new FileOutputStream(path);
        byte[] buffer;
        System.out.println("Получение файла " + path);
        while (true) {
            buffer = new byte[1024];
            buffer = receive();
            System.out.println(Arrays.toString(buffer));
            fout.write(buffer);
            if (buffer.length < 1024) break;
        }
        System.out.println("Файл получен " + path);
    }
    public void disconnect() throws IOException {
        channel.disconnect();
    }
    public void close() throws IOException {
        channel.close();
    }

    @Override
    public void request(Signal signal, Object obj) {
        this.sendHandler(new Request(signal, obj).bytes());
    }
    public void signal(Signal signal) {
        this.sendHandler(signal.bytes());
    }
}