package client.commands;

import common.CommandClient;
import common.Signal;
/**
 * @author Abdujalol Khodjaev
 * Object to make 'remove_key' request
 * Implements CommandClient
 * @see CommandClient
 */
public class RemoveKeyClient implements CommandClient {
    String[] args = new String[0];
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public String type() {
        return "remove_key";
    }
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public String[] argLine() {
        return args;
    }
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public void setArgs(String[] args) {
        this.args = args;
    }
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public boolean validate() {
        return args.length == 1;
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
