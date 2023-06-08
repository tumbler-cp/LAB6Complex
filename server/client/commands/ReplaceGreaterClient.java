package client.commands;

import common.CommandClient;
import common.Signal;
import common.UDPInterface;
/**
 * @author Abdujalol Khodjaev
 * Object to make 'replace_if_greater' request
 * Implements CommandClient
 * @see CommandClient
 */
public class ReplaceGreaterClient implements CommandClient {
    String[] args = new String[0];
    UDPInterface network;
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public String type() {
        return "replace_if_greater";
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
        return args.length == 3 || args.length == 1;
    }
    /**
     * Implemented method
     * @see CommandClient
     */
    @Override
    public Signal signal(){
        return Signal.BIG_COMMAND;
    }
}
