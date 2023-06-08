package common;

import java.io.IOException;

/**
 * @author Abdujalol Khodjaev
 * Just logical interface to keep simple structure of app.
 */
public interface UDPInterface {
    /**
     * Data sender
     * @param data Data to send
     */
    void send(byte[] data);

    /**
     * Data receiver
     * @return Received data
     * @throws IOException May be thrown while receiving
     */
    byte[] receive() throws IOException;

    /**
     * Close DatagramChannel or DatagramSocket
     * @throws IOException May be thrown while closing
     */
    void close() throws IOException;

    /**
     * Request sender. Should create request object and send it
     * @param signal Request signal
     * @param obj Request object
     * @see Request
     */
    void request (Signal signal, Object obj);
}