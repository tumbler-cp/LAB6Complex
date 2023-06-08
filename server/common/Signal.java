package common;

import org.apache.commons.lang3.SerializationUtils;

public enum Signal {
    TEXT,
    COMMAND,
    BIG_COMMAND,
    FILE_COMMAND,
    CLOSING,
    ASK,
    LOCAL,
    WAIT;

    public byte[] bytes() {
        return SerializationUtils.serialize(this);
    }
}
