package common;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

/**
 * @author Abdujalol Khodjaev
 * Request object. Contains signal and object to send
 * @see Serializable
 * @see Signal
 * @see client.ClientHandler
 * @see client.UDPClient
 */
public class Request implements Serializable {
    private final Object obj;
    private final Signal signal;

    /**
     * Default constructor
     * @param signal Signal of request
     * @param object Object of request
     */
    public Request(Signal signal, Object object) {
        this.obj = object;
        this.signal = signal;
    }

    /**
     * Request validation. Signal should fit object.
     * @return result of validation
     */
    public boolean validate(){
        switch (signal) {
            case COMMAND -> {
                return obj instanceof CommandClient && ((CommandClient) obj).validate();
            }
            case TEXT, ASK -> {
                return obj instanceof String;
            }
            case CLOSING -> {
                return obj == null;
            }
            default -> {
                return true;
            }
        }
    }

    /**
     * Request serialization
     * @return Byte array
     */
    public byte[] bytes() {
        return SerializationUtils.serialize(this);
    }

    /**
     * Signal getter
     * @return signal
     */
    public Signal getSignal() {
        return signal;
    }

    /**
     * Object getter
     * @return object
     */
    public Object getObj() {
        return obj;
    }
}
