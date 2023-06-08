package client.commands;

import common.CommandClient;
import common.Signal;

/**
 * @author Abdujalol Khodjaev
 * Object to make 'clear' request
 * Implements CommandClient
 * @see CommandClient
 */
public class ClearClient implements CommandClient {
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public String type() {
        return "clear";
    }
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public String[] argLine() {
        return new String[0];
    }
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public void setArgs(String[] args) {

    }
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public boolean validate() {
        return true;
    }
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public Signal signal() {
        return Signal.COMMAND;
    }
}
