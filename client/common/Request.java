package common;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

public class Request implements Serializable {
    private final Object obj;
    private final Signal signal;
    public Request(Signal signal, Object object) {
        this.obj = object;
        this.signal = signal;
    }
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
    public byte[] bytes() {
        return SerializationUtils.serialize(this);
    }

    public Signal getSignal() {
        return signal;
    }

    public Object getObj() {
        return obj;
    }
}
