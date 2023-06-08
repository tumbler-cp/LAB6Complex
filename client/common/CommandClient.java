package common;

import java.io.Serializable;

/**
 * @author Abdujalol Khodjaev
 * Command request object
 * @see Request
 */
public interface CommandClient extends Serializable {
    /**
     * @return Name of command to find it on server
     */
    String type();

    /**
     * Command request object should contain arguments of command for execution
     * @return Arguments array
     */
    String[] argLine();

    /**
     * Command request object should contain arguments of command for execution
     * This method sets new arguments
     * @param args Arguments to set
     */
    void setArgs(String[] args);

    /**
     * Commands are validated on clients' app
     * @return Is command valid
     */
    boolean validate();

    /**
     * Type of signal that Request shall contain for right execution of command
     * @return Type of signal
     * @see Signal
     */
    Signal signal();
}
