package common;

import org.apache.commons.lang3.SerializationUtils;

/**
 * Signal is need to know how to interact with object of request.
 * TEXT - Object is String
 * COMMAND, BIG_COMMAND - Object is client command request
 * FILE_COMMAND - If server accepts this signal it turns it back to Client.
 * Client sends required file
 * CLOSING - Closing signal. Is used for different types of exiting some loop etc.
 * ASK - Only client receives this signal. It means that server need client in data.
 * WAIT - Only client receives this signal. Means that server is interacting with another client.
 */

public enum Signal {
    TEXT,
    COMMAND,
    BIG_COMMAND,
    FILE_COMMAND,
    CLOSING,
    ASK,
    WAIT;

    /**
     * Signal serialization
     * @return Byte array
     */

    public byte[] bytes() {
        return SerializationUtils.serialize(this);
    }
}
