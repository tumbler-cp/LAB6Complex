package client.commands;

import common.CommandClient;
import common.Signal;
/**
 * @author Abdujalol Khodjaev
 * Object to make 'info' request
 * Implements CommandClient
 * @see CommandClient
 */
public class InfoClient implements CommandClient {
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public String type() {
        return "info";
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
