package common;

import java.io.IOException;

public interface UDPInterface {
    void send(byte[] data);

    byte[] receive() throws IOException;

    void close() throws IOException;
    void request (Signal signal, Object obj);
}